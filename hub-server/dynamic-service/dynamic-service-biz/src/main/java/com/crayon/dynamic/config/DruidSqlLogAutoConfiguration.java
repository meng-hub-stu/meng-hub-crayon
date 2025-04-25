package com.crayon.dynamic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.crayon.common.data.mybatis.DruidSqlLogFilter;
import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Mengdl
 * @date 2025/04/25
 */
//@Configuration
//@AutoConfigureAfter({DynamicDataSourceAutoConfiguration.class})
public class DruidSqlLogAutoConfiguration {

    private final DynamicRoutingDataSource dynamicRoutingDataSource;
    private final DruidSqlLogFilter druidSqlLogFilter;


    public DruidSqlLogAutoConfiguration(DynamicRoutingDataSource dynamicRoutingDataSource,
                                        DruidSqlLogFilter druidSqlLogFilter) {
        this.dynamicRoutingDataSource = dynamicRoutingDataSource;
        this.druidSqlLogFilter = druidSqlLogFilter;
        System.out.println("DynamicRoutingDataSource injected: " + dynamicRoutingDataSource);
        System.out.println("druidSqlLogFilter injected: " + druidSqlLogFilter);
    }

    @PostConstruct
    public void dynamicRoutingDataSource01() {
        List<DataSource> dataSources = dynamicRoutingDataSource.getDataSources().values().stream().toList();
        for (DataSource ds : dataSources) {
            if (ds instanceof DruidDataSource druidDataSource) {
                druidDataSource.getProxyFilters().add(druidSqlLogFilter);
            }
        }
    }

}
