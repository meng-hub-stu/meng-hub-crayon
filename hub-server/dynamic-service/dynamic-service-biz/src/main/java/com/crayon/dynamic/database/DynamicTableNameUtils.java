package com.crayon.dynamic.database;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static com.crayon.dynamic.constant.DynamicConstant.DYNAMIC_TABLE_NAME_PREFIX;
import static com.crayon.dynamic.constant.DynamicConstant.SKIP_DYNAMIC_TABLE_NAME;

/**
 * 动态生成表和查询sql
 *
 * @author Mengdl
 * @date 2025/04/19
 */
public class DynamicTableNameUtils {

    private static final ThreadLocal<String> DYNAMIC_TABLE_NAME = new ThreadLocal<>();

    @Getter
    private static final Map<String, Boolean> IS_EXISTS_TABLE = Maps.newConcurrentMap();

    /**
     * 设置表明
     *
     * @param tableName 表名
     */
    public static void applyTable(String tableName) {
        DYNAMIC_TABLE_NAME.set("`" + DYNAMIC_TABLE_NAME_PREFIX + tableName.toLowerCase() + "`");
    }

    /**
     * 跳过动态表名
     */
    public static void skip() {
        DYNAMIC_TABLE_NAME.set(SKIP_DYNAMIC_TABLE_NAME);
    }

    /**
     * 获取当前表名
     *
     * @return 表名
     */
    public static String getCurrentTable() {
        return DYNAMIC_TABLE_NAME.get();
    }

    /**
     * 清除当前表名
     */
    public static void clear() {
        DYNAMIC_TABLE_NAME.remove();
    }

    /**
     * 查询和插入时使用
     * 获取当前表名
     *
     * @param tableName 表名
     * @return 表名
     */
    public static String getDdlTable(String tableName) {
        return "`" + getDmlTable(tableName) + "`";
    }

    /**
     * 创建表和校验是否存在表时使用
     *
     * @param tableName 表名
     * @return 表名
     */

    public static String getDmlTable(String tableName) {
        return DYNAMIC_TABLE_NAME_PREFIX + tableName;
    }

}
