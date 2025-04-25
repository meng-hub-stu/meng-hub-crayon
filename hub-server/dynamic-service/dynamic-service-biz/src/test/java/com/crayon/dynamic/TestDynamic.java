package com.crayon.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.crayon.dynamic.entity.model.ManDynamic;
import com.crayon.dynamic.service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = DynamicApplication.class)
public class TestDynamic {

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DynamicRoutingDataSource  dynamicRoutingDataSource;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testSave() {
        ManDynamic manDynamic = ManDynamic.builder()
                .id(1L)
                .name("Mengdl")
                .tableName("test")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .delFlag(0)
                .build();
        Boolean result = dynamicService.save(manDynamic);
        System.out.println("执行结果" + result);
    }


    @Test
    public void detail() {
        ManDynamic detail = dynamicService.detail(1L, "Mengdl");
        ManDynamic detail1 = dynamicService.detailMt4(1L, "mt5", "Xujing");
//        ManDynamic detail2 = dynamicService.detailMt5(3L, "mt5", "Mengdl");
        System.out.println(detail);
        System.out.println(detail1);
//        System.out.println(detail2);
    }

    @Test
    public void checkPoolConfig() throws SQLException {
        if (dataSource instanceof DynamicRoutingDataSource) {
            DynamicRoutingDataSource dynamicDataSource = (DynamicRoutingDataSource) dataSource;

            // 获取具体数据源（例如默认的 primary 数据源）
            Map<String, DataSource> dataSources = dynamicDataSource.getDataSources();
            for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
                DataSource realDataSource = entry.getValue();
//                if (realDataSource instanceof DruidDataSource) {
//                    DruidDataSource druid = (DruidDataSource) realDataSource;
//                    log.info(String.format(
//                            "Druid 连接池参数: \n" +
//                                    "maxActive=%d\n" +
//                                    "initialSize=%d\n" +
//                                    "minIdle=%d\n" +
//                                    "maxWait=%d\n" +
//                                    "poolingCount=%d (当前活跃连接数)\n" +
//                                    "activeCount=%d (正在使用的连接数)",
//                            druid.getMaxActive(),
//                            druid.getInitialSize(),
//                            druid.getMinIdle(),
//                            druid.getMaxWait(),
//                            druid.getPoolingCount(),
//                            druid.getActiveCount()
//                    ));
//                }
                // 穿透 ItemDataSource 获取真实数据源
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
                    }
                }
            }
            log.info("未使用动态数据源");
        }
    }

    @Test
    public void checkDataSource() throws SQLException {
        boolean exists = applicationContext.getBeanNamesForType(DynamicRoutingDataSource.class).length > 0;
        log.info("DynamicRoutingDataSource exists: " + exists);
    }

}

