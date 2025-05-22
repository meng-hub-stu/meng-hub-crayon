package com.crayon.dynamic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.crayon.common.data.mybatis.DruidSqlLogFilter;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @author Mengdl
 * @date 2025/04/25
 */
@Slf4j
@Configuration
@AutoConfigureAfter({DynamicDataSourceAutoConfiguration.class})
@RequiredArgsConstructor
public class DataSourceConfiguration {

    private final DataSource dataSource;
    private final DruidSqlLogFilter druidSqlLogFilter;
    private final ApplicationContext applicationContext;

//    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }

    @PostConstruct
    public void initDynamic() {
        log.info("动态数据源初始化中...");
        DynamicRoutingDataSource dynamicRoutingDataSource = getDynamicRouting();
        if (dynamicRoutingDataSource == null) {
            return;
        }
        Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            DataSource realDataSource = entry.getValue();
            if (realDataSource instanceof ItemDataSource itemDs) {
                DataSource realDs = itemDs.getRealDataSource();
                log.info("真实连接池类型: {}", realDs.getClass().getName());
                if (realDs instanceof DruidDataSource druid) {
                    log.info("Druid 连接池参数: \n" +
                            "maxActive={}\n" +
                            "initialSize={}\n" +
                            "minIdle={}\n" +
                            "maxWait={}\n" +
                            "poolingCount={} (当前活跃连接数)\n" +
                            "activeCount={} (正在使用的连接数)", druid.getMaxActive(), druid.getInitialSize(), druid.getMinIdle(), druid.getMaxWait(), druid.getPoolingCount(), druid.getActiveCount());
                    druid.getProxyFilters().add(druidSqlLogFilter);
                    druid.getProxyFilters().stream().filter(filter -> filter instanceof WallFilter)
                            .forEach(filter -> {
                                WallConfig config = ((WallFilter) filter).getConfig();
                                config.setMultiStatementAllow(true);
                            });
                }
            }
        }
        log.info("动态数据源初始化完成");
    }

    public void addDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = getDynamicRouting();
        if (dynamicRoutingDataSource == null) {
            return;
        }
        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
        DataSourceConfig config = new DataSourceConfig();
        String beanName = config.getName();
        if (!beanFactory.containsBean(beanName)) {
            Properties properties = new Properties();
            properties.setProperty("url", config.getUrl());
            properties.setProperty("username", config.getUsername());
            properties.setProperty("password", config.getPassword());
            properties.setProperty("driverClassName", config.getDriverClassName());
            // 可选配置
            properties.setProperty("initialSize", "5");
            properties.setProperty("minIdle", "5");
            properties.setProperty("maxActive", "20");
            properties.setProperty("maxWait", "60000");
            properties.setProperty("validationQuery", "SELECT 1");
            properties.setProperty("testWhileIdle", "true");
            properties.setProperty("testOnBorrow", "false");
            properties.setProperty("testOnReturn", "false");

            try {
                DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
                dataSource.getProxyFilters().add(druidSqlLogFilter);
                dataSource.init();
                //注册为bean
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(DruidDataSource.class);
                beanDefinition.setInstanceSupplier((Supplier<DruidDataSource>) () -> (DruidDataSource) dataSource);
                beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
                beanFactory.registerBeanDefinition(beanName, beanDefinition);
                dynamicRoutingDataSource.addDataSource(beanName, dataSource);
                log.info("添加自定义数据源 Bean: {}", beanName);
                Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
                log.info("动态数据源数量: {}", dataSources.size());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取动态数据源
     *
     * @return DynamicRoutingDataSource
     */
    public DynamicRoutingDataSource getDynamicRouting() {
        if (dataSource instanceof DynamicRoutingDataSource dynamicRoutingDataSource) {
            return dynamicRoutingDataSource;
        }
        return null;
    }

    /**
     * 测试数据
     */
    @Data
    public static class DataSourceConfig {
        private String name = "mt5-crayon";
        private String url = "jdbc:mysql://52.206.241.22:3308/mt5_vfx_test3";
        private String username = "app-mt5-ro";
        private String password = "ZvffWukKZZOKMyx5fgG!";
        private String driverClassName = "com.mysql.cj.jdbc.Driver";
        private String type = "com.alibaba.druid.pool.DruidDataSource";
    }

}
