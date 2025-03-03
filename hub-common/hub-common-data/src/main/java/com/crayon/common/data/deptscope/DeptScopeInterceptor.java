package com.crayon.common.data.deptscope;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.crayon.common.core.util.R;
import com.crayon.student.entity.model.Student;
import com.crayon.student.feign.StudentFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;

/**
 * 数据权限抽象
 *
 * @author hytech
 * @date 2022/8/9
 */
@Slf4j
@RequiredArgsConstructor
public class DeptScopeInterceptor extends JsqlParserSupport implements ImportAware, InnerInterceptor {

    @Setter
    private StudentFeignClient studentFeignClient;

    private String column;

    private List<String> includeTables = new ArrayList<>();

    private static final Long deptId = 123456789L;

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        Map<String, Object> enableAttrs = annotationMetadata.getAnnotationAttributes(EnableDeptInterceptor.class.getName());
        column = valueOf(requireNonNull(enableAttrs).get("column"));
        String[] values = (String[]) enableAttrs.get("value");
        if (values != null && values.length > 0) {
            includeTables.addAll(Arrays.asList(values));
        }
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // TODO 判断不让走下面的数据权限
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), null));
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        String whereSegment = (String) obj;
        this.processSelectBody(select, whereSegment);
        List<WithItem<?>> withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach((withItem) -> {
                this.processSelectBody(withItem.getSelect(), whereSegment);
            });
        }
    }

    protected void processSelectBody(Select selectBody, final String whereSegment) {
        if (selectBody != null) {
            if (selectBody instanceof PlainSelect) {
                this.processPlainSelect((PlainSelect) selectBody, whereSegment);
            } else if (selectBody instanceof ParenthesedSelect) {
                ParenthesedSelect parenthesedSelect = (ParenthesedSelect) selectBody;
                this.processSelectBody(parenthesedSelect.getSelect(), whereSegment);
            } else if (selectBody instanceof SetOperationList) {
                SetOperationList operationList = (SetOperationList) selectBody;
                List<Select> selectBodyList = operationList.getSelects();
                if (CollectionUtils.isNotEmpty(selectBodyList)) {
                    selectBodyList.forEach((body) -> {
                        this.processSelectBody(body, whereSegment);
                    });
                }
            }
        }
    }

    protected void processPlainSelect(final PlainSelect plainSelect, final String whereSegment) {
        List<SelectItem<?>> selectItems = plainSelect.getSelectItems();
        if (CollectionUtils.isNotEmpty(selectItems)) {
            selectItems.forEach((selectItem) -> {
                this.processSelectItem(selectItem, whereSegment);
            });
        }
        Expression where = plainSelect.getWhere();
        this.processWhereSubSelect(where, whereSegment);
        FromItem fromItem = plainSelect.getFromItem();
        List<Table> list = this.processFromItem(fromItem, whereSegment);
        List<Table> mainTables = new ArrayList(list);
        List<Join> joins = plainSelect.getJoins();
        if (CollectionUtils.isNotEmpty(joins)) {
            this.processJoins(mainTables, joins, whereSegment);
        }

        if (CollectionUtils.isNotEmpty(mainTables)) {
            plainSelect.setWhere(this.builderExpression(where, mainTables, whereSegment));
        }

    }

    private List<Table> processFromItem(FromItem fromItem, final String whereSegment) {
        List<Table> mainTables = new ArrayList<>();
        // 无 join 时的处理逻辑
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            mainTables.add(fromTable);
        } else if (fromItem instanceof ParenthesedFromItem) {
            // SubJoin 类型则还需要添加上 where 条件
            List<Table> tables = processSubJoin((ParenthesedFromItem) fromItem, whereSegment);
            mainTables.addAll(tables);
        } else {
            // 处理下 fromItem
            processOtherFromItem(fromItem, whereSegment);
        }
        return mainTables;
    }

    protected void processWhereSubSelect(Expression where, final String whereSegment) {
        if (where == null) {
            return;
        }
        if (where instanceof FromItem) {
            processOtherFromItem((FromItem) where, whereSegment);
            return;
        }
        if (where.toString().indexOf("SELECT") > 0) {
            // 有子查询
            if (where instanceof BinaryExpression) {
                // 比较符号 , and , or , 等等
                BinaryExpression expression = (BinaryExpression) where;
                processWhereSubSelect(expression.getLeftExpression(), whereSegment);
                processWhereSubSelect(expression.getRightExpression(), whereSegment);
            } else if (where instanceof InExpression) {
                // in
                InExpression expression = (InExpression) where;
                Expression inExpression = expression.getRightExpression();
                if (inExpression instanceof Select) {
                    processSelectBody(((Select) inExpression), whereSegment);
                }
            } else if (where instanceof ExistsExpression) {
                // exists
                ExistsExpression expression = (ExistsExpression) where;
                processWhereSubSelect(expression.getRightExpression(), whereSegment);
            } else if (where instanceof NotExpression) {
                // not exists
                NotExpression expression = (NotExpression) where;
                processWhereSubSelect(expression.getExpression(), whereSegment);
            } else if (where instanceof ParenthesedExpressionList) {
                ParenthesedExpressionList<Expression> expression = (ParenthesedExpressionList) where;
                processWhereSubSelect(expression.get(0), whereSegment);
            }
        }
    }

    protected void processSelectItem(SelectItem selectItem, final String whereSegment) {
        Expression expression = selectItem.getExpression();
        if (expression instanceof Select) {
            processSelectBody(((Select) expression), whereSegment);
        } else if (expression instanceof Function) {
            processFunction((Function) expression, whereSegment);
        } else if (expression instanceof ExistsExpression) {
            ExistsExpression existsExpression = (ExistsExpression) expression;
            processSelectBody((Select) existsExpression.getRightExpression(), whereSegment);
        }
    }

    protected void processFunction(Function function, final String whereSegment) {
        ExpressionList<?> parameters = function.getParameters();
        if (parameters != null) {
            parameters.forEach(expression -> {
                if (expression instanceof Select) {
                    processSelectBody(((Select) expression), whereSegment);
                } else if (expression instanceof Function) {
                    processFunction((Function) expression, whereSegment);
                } else if (expression instanceof EqualsTo) {
                    if (((EqualsTo) expression).getLeftExpression() instanceof Select) {
                        processSelectBody(((Select) ((EqualsTo) expression).getLeftExpression()), whereSegment);
                    }
                    if (((EqualsTo) expression).getRightExpression() instanceof Select) {
                        processSelectBody(((Select) ((EqualsTo) expression).getRightExpression()), whereSegment);
                    }
                }
            });
        }
    }

    protected void processOtherFromItem(FromItem fromItem, final String whereSegment) {
        // 去除括号
        while (fromItem instanceof ParenthesedFromItem) {
            fromItem = ((ParenthesedFromItem) fromItem).getFromItem();
        }
        if (fromItem instanceof ParenthesedSelect) {
            Select subSelect = (Select) fromItem;
            processSelectBody(subSelect, whereSegment);
        }
    }

    private List<Table> processSubJoin(ParenthesedFromItem subJoin, final String whereSegment) {
        while (subJoin.getJoins() == null && subJoin.getFromItem() instanceof ParenthesedFromItem) {
            subJoin = (ParenthesedFromItem) subJoin.getFromItem();
        }
        List<Table> tableList = processFromItem(subJoin.getFromItem(), whereSegment);
        List<Table> mainTables = new ArrayList<>(tableList);
        if (subJoin.getJoins() != null) {
            processJoins(mainTables, subJoin.getJoins(), whereSegment);
        }
        return mainTables;
    }

    private List<Table> processJoins(List<Table> mainTables, List<Join> joins, final String whereSegment) {
        // join 表达式中最终的主表
        Table mainTable = null;
        // 当前 join 的左表
        Table leftTable = null;

        if (mainTables.size() == 1) {
            mainTable = mainTables.get(0);
            leftTable = mainTable;
        }

        //对于 on 表达式写在最后的 join，需要记录下前面多个 on 的表名
        Deque<List<Table>> onTableDeque = new LinkedList<>();
        for (Join join : joins) {
            // 处理 on 表达式
            FromItem joinItem = join.getRightItem();

            // 获取当前 join 的表，subJoint 可以看作是一张表
            List<Table> joinTables = null;
            if (joinItem instanceof Table) {
                joinTables = new ArrayList<>();
                joinTables.add((Table) joinItem);
            } else if (joinItem instanceof ParenthesedFromItem) {
                joinTables = processSubJoin((ParenthesedFromItem) joinItem, whereSegment);
            }

            if (joinTables != null && !joinTables.isEmpty()) {

                // 如果是隐式内连接
                if (join.isSimple()) {
                    mainTables.addAll(joinTables);
                    continue;
                }

                // 当前表是否忽略
                Table joinTable = joinTables.get(0);

                List<Table> onTables = null;
                // 如果不要忽略，且是右连接，则记录下当前表
                if (join.isRight()) {
                    mainTable = joinTable;
                    mainTables.clear();
                    if (leftTable != null) {
                        onTables = Collections.singletonList(leftTable);
                    }
                } else if (join.isInner()) {
                    if (mainTable == null) {
                        onTables = Collections.singletonList(joinTable);
                    } else {
                        onTables = Arrays.asList(mainTable, joinTable);
                    }
                    mainTable = null;
                    mainTables.clear();
                } else {
                    onTables = Collections.singletonList(joinTable);
                }

                if (mainTable != null && !mainTables.contains(mainTable)) {
                    mainTables.add(mainTable);
                }

                // 获取 join 尾缀的 on 表达式列表
                Collection<Expression> originOnExpressions = join.getOnExpressions();
                // 正常 join on 表达式只有一个，立刻处理
                if (originOnExpressions.size() == 1 && onTables != null) {
                    List<Expression> onExpressions = new LinkedList<>();
                    onExpressions.add(builderExpression(originOnExpressions.iterator().next(), onTables, whereSegment));
                    join.setOnExpressions(onExpressions);
                    leftTable = mainTable == null ? joinTable : mainTable;
                    continue;
                }
                // 表名压栈，忽略的表压入 null，以便后续不处理
                onTableDeque.push(onTables);
                // 尾缀多个 on 表达式的时候统一处理
                if (originOnExpressions.size() > 1) {
                    Collection<Expression> onExpressions = new LinkedList<>();
                    for (Expression originOnExpression : originOnExpressions) {
                        List<Table> currentTableList = onTableDeque.poll();
                        if (CollectionUtils.isEmpty(currentTableList)) {
                            onExpressions.add(originOnExpression);
                        } else {
                            onExpressions.add(builderExpression(originOnExpression, currentTableList, whereSegment));
                        }
                    }
                    join.setOnExpressions(onExpressions);
                }
                leftTable = joinTable;
            } else {
                processOtherFromItem(joinItem, whereSegment);
                leftTable = null;
            }
        }

        return mainTables;
    }

    /**
     * 处理条件
     */
    protected Expression builderExpression(Expression currentExpression, List<Table> tables, final String whereSegment) {
        // 没有表需要处理直接返回
        if (CollectionUtils.isEmpty(tables)) {
            return currentExpression;
        }
        // 构造每张表的条件
        List<Expression> expressions = tables.stream()
                .map(item -> buildTableExpression(item, currentExpression, whereSegment))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 没有表需要处理直接返回
        if (CollectionUtils.isEmpty(expressions)) {
            return currentExpression;
        }

        // 注入的表达式
        Expression injectExpression = expressions.get(0);
        // 如果有多表，则用 and 连接
        if (expressions.size() > 1) {
            for (int i = 1; i < expressions.size(); i++) {
                injectExpression = new AndExpression(injectExpression, expressions.get(i));
            }
        }

        if (currentExpression == null) {
            return injectExpression;
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new ParenthesedExpressionList<>(currentExpression), injectExpression);
        } else {
            return new AndExpression(currentExpression, injectExpression);
        }
    }

    public Expression buildTableExpression(final Table table, final Expression where, final String whereSegment) {
        if (this.ignoreTable(table.getName())) {
            return null;
        }
        //TODO 如果不是忽略的表，那就进行查询就好了
        R<Student> info = studentFeignClient.getInfo(1895064576700153857L);
        log.info("info: {}", info);
        return new EqualsTo(getAliasColumn(table), new LongValue(this.deptId));
    }

    private boolean ignoreTable(String tableName) {
        return !includeTables.contains(tableName.replace("`", ""));
    }

    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(StringPool.DOT);
        }
        column.append(this.column);
        return new Column(column.toString());
    }

}
