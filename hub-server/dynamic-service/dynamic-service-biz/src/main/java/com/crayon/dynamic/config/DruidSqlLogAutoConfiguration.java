package com.crayon.dynamic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.crayon.common.data.mybatis.DruidSqlLogFilter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/04/25
 */
@Slf4j
@Configuration
@AutoConfigureAfter({DynamicDataSourceAutoConfiguration.class})
public class DruidSqlLogAutoConfiguration {

    private final DruidSqlLogFilter druidSqlLogFilter;
    private final DataSource dataSource;


    public DruidSqlLogAutoConfiguration(DataSource dataSource,
                                        DruidSqlLogFilter druidSqlLogFilter) {
        this.dataSource = dataSource;
        this.druidSqlLogFilter = druidSqlLogFilter;
    }

    @PostConstruct
    public void initDynamic() {
        if (dataSource instanceof DynamicRoutingDataSource dynamicDataSource) {
            Map<String, DataSource> dataSources = dynamicDataSource.getDataSources();
            for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
                DataSource realDataSource = entry.getValue();
                if (realDataSource instanceof ItemDataSource itemDs) {
                    DataSource realDs = itemDs.getRealDataSource();
                    log.info("真实连接池类型: " + realDs.getClass().getName());
                    if (realDs instanceof DruidDataSource druid) {
                        log.info(String.format(
                                "Druid 连接池参数: \n" +
                                        "maxActive=%d\n" +
                                        "initialSize=%d\n" +
                                        "minIdle=%d\n" +
                                        "maxWait=%d\n" +
                                        "poolingCount=%d (当前活跃连接数)\n" +
                                        "activeCount=%d (正在使用的连接数)",
                                druid.getMaxActive(),
                                druid.getInitialSize(),
                                druid.getMinIdle(),
                                druid.getMaxWait(),
                                druid.getPoolingCount(),
                                druid.getActiveCount()
                        ));
                        druid.getProxyFilters().add(druidSqlLogFilter);
                    }
                }
            }
        }
    }

}
