package com.crayon.dynamic.database;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Mengdl
 * @description mybatis动态数据源配置
 * @date 2025/04/19
 */
@Configuration
public class MybatisDynamicConfig {

    @Bean
    @Primary
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页支持
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 支持乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //动态表名
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        //需要重置一下表名
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> tableName);
//        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) ->
//                Optional.ofNullable(DynamicTableNameUtils.getCurrentTable())
//                        .filter(t -> t.contains(DYNAMIC_TABLE_NAME_PREFIX))
//                        .orElse(tableName));
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }

}
