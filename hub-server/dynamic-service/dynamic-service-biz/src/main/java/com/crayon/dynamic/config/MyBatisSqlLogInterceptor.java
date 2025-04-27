//package com.crayon.dynamic.config;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.ArrayUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.druid.DbType;
//import com.alibaba.druid.sql.SQLUtils;
//import com.alibaba.druid.sql.ast.SQLStatement;
//import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
//import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
//import com.alibaba.druid.stat.TableStat;
//import com.crayon.common.data.mybatis.DruidSqlLogProperties;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.SystemMetaObject;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.time.temporal.TemporalAccessor;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Mengdl
// * @date 2025/04/23
// */
//@Slf4j
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//@Component
//public class MyBatisSqlLogInterceptor implements Interceptor {
//
//    public static final SQLUtils.FormatOption FORMAT_OPTION = new SQLUtils.FormatOption(false, false);
//
//    private final DruidSqlLogProperties properties;
//
//    public MyBatisSqlLogInterceptor(DruidSqlLogProperties properties) {
//        this.properties = properties;
//    }
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        long startTime = System.nanoTime();
//
//        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
//        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
//
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        String sqlId = mappedStatement.getId();
//        BoundSql boundSql = statementHandler.getBoundSql();
//        String sql = boundSql.getSql();
//        Object parameterObject = boundSql.getParameterObject();
//
//        // 支持动态开启
//        if (!properties.isShowSql()) {
//            return invocation.proceed();
//        }
//
//        // 是否开启调试
//        if (!log.isInfoEnabled()) {
//            return invocation.proceed();
//        }
//
//        // 判断表名是配置了匹配过滤
//        if (CollUtil.isNotEmpty(properties.getSkipTable())) {
//            List<String> skipTableList = properties.getSkipTable();
//            List<String> tableNameList = getTablesByDruid(sql, mappedStatement.getConfiguration().getDatabaseId());
//            if (tableNameList.stream()
//                    .anyMatch(tableName -> StrUtil.containsAnyIgnoreCase(tableName,
//                            ArrayUtil.toArray(skipTableList, String.class)))) {
//                return invocation.proceed();
//            }
//        }
//
//        List<Object> parameters = new ArrayList<>();
//        if (parameterObject != null) {
//            if (parameterObject instanceof Map) {
//                Map<String, String> paramMap = (Map) parameterObject;
//                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//                    parameters.add(getJdbcParameter(entry.getValue()));
//                }
//            }else {
//                parameters.add(parameterObject);
//            }
//        }
//
//        String formattedSql = SQLUtils.format(sql, DbType.of(mappedStatement.getConfiguration().getDatabaseId()), parameters, FORMAT_OPTION);
//        Object result = invocation.proceed();
//        long endTime = System.nanoTime();
//        long executeTime = endTime - startTime;
//        printSql(formattedSql, sqlId, executeTime);
//        return result;
//    }
//
//    private static Object getJdbcParameter(Object jdbcParam) {
//        if (jdbcParam == null) {
//            return null;
//        }
//        // 处理 java8 时间
//        if (jdbcParam instanceof TemporalAccessor) {
//            return jdbcParam.toString();
//        }
//        return jdbcParam;
//    }
//
//    private static void printSql(String sql, String sqlId, long executeTime) {
//        // 打印 sql 和执行时间
//        String sqlLogger = "\n\n======= Sql Logger ======================" + "\n{}"
//                + "\n======= Sql ID: {} ======="
//                + "\n======= Sql Execute Time: {} =======\n";
//        log.info(sqlLogger, sql.trim(), sqlId, format(executeTime));
//    }
//
//    private static String format(long nanos) {
//        if (nanos < 1) {
//            return "0ms";
//        }
//        double millis = (double) nanos / (1000 * 1000);
//        // 不够 1 ms，最小单位为 ms
//        if (millis > 1000) {
//            return String.format("%.3fs", millis / 1000);
//        } else {
//            return String.format("%.3fms", millis);
//        }
//    }
//
//    /**
//     * 从SQL中提取表名(sql中出现的所有表)
//     *
//     * @param sql    sql语句
//     * @param dbType dbType
//     * @return List<String>
//     */
//    public static List<String> getTablesByDruid(String sql, String dbType) {
//        List<String> result = new ArrayList<>();
//        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
//        for (SQLStatement stmt : stmtList) {
//            // 也可以用更精确的解析器，如MySqlSchemaStatVisitor
//            SchemaStatVisitor visitor;
//            // SQL server 数据库特殊处理
//            if (DbType.sqlserver.name().equalsIgnoreCase(dbType)) {
//                visitor = new SQLServerSchemaStatVisitor();
//            } else {
//                visitor = new SchemaStatVisitor();
//            }
//            stmt.accept(visitor);
//            Map<TableStat.Name, TableStat> tables = visitor.getTables();
//            for (TableStat.Name name : tables.keySet()) {
//                result.add(name.getName());
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//}
