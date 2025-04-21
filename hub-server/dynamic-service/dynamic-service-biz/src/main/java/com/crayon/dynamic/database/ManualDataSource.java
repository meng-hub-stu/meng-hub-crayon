package com.crayon.dynamic.database;

import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.crayon.dynamic.constant.DynamicConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ManualDataSource {

    private final MysqlWriteProperties properties;

    private static final String SQL = """
            CREATE TABLE
            IF NOT EXISTS `%s` (
            		id BIGINT(64) NOT NULL COMMENT '主键',
            		name VARCHAR (16) DEFAULT NULL COMMENT '刻度',
            		table_name VARCHAR (16) DEFAULT NULL COMMENT '刻度',
            		create_time datetime(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
            		update_time datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
            		del_flag tinyint(1) DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）' ,
            		PRIMARY KEY (id)
            	)""";

    public void createTable(String tableName) {
        try {
            DataSource dataSource = new SimpleDataSource(
                    properties.getMysqlUrl(),
                    properties.getMysqlUser(),
                    properties.getMysqlPwd()
            );
            Db db = Db.use(dataSource);
            String format = String.format(SQL, DynamicConstant.DYNAMIC_TABLE_NAME_PREFIX + tableName.toLowerCase());
            int execute = db.execute(format);
            log.info("create table success {}: {}", tableName, execute);
        } catch (SQLException e) {
            log.info("create table error {}: {}", tableName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
