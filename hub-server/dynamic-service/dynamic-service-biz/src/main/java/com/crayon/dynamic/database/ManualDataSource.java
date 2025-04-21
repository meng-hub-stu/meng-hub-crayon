package com.crayon.dynamic.database;

import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.lock.annotation.Lock4j;
import com.crayon.dynamic.constant.DynamicConstant;
import com.crayon.dynamic.mapper.DynamicMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.crayon.dynamic.database.DynamicTableNameUtils.getDmlTable;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ManualDataSource {

    private final MysqlWriteProperties properties;
    private final DynamicMapper dynamicMapper;

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

    /**
     * 创建表
     *
     * @param tableName 表名
     */
    public void createTable(String tableName) {
        try {
            DataSource dataSource = new SimpleDataSource(
                    properties.getMysqlUrl(),
                    properties.getMysqlUser(),
                    properties.getMysqlPwd()
            );
            Db db = Db.use(dataSource);
            String format = String.format(SQL, tableName.toLowerCase());
            int execute = db.execute(format);
            log.info("create table success {}: {}", tableName, execute);
            DynamicTableNameUtils.getIS_EXISTS_TABLE().put(tableName, true);
        } catch (SQLException e) {
            log.info("create table error {}: {}", tableName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查表是否存在
     *
     * @param tableName 表名
     * @return 结果
     */
    public Boolean checkTableIsExists(String tableName) {
        Boolean isExist = DynamicTableNameUtils.getIS_EXISTS_TABLE().get(tableName);
        if (isExist != null && isExist) {
            return true;
        }
        boolean isExistDb = dynamicMapper.checkTableIsExists(tableName) > 0;
        if (isExistDb) {
            DynamicTableNameUtils.getIS_EXISTS_TABLE().put(tableName, true);
        } else {
            DynamicTableNameUtils.getIS_EXISTS_TABLE().put(tableName, false);
        }
        return isExistDb;
    }

    /**
     * 检查表是否存在 如果不存在则创建表
     * todo 这里需要分布式锁，要不然就会创建重复的表
     * @param tableName 表名
     */
    @Lock4j(name = "createTable", keys = "#tableName", expire = 15000, acquireTimeout = 1000)
    public void checkOrCreateTable(String tableName) {
        Boolean isExists = this.checkTableIsExists(tableName);
        if (!isExists) {
            this.createTable(tableName);
        }
    }

}
