package com.crayon.teacher;

import com.alibaba.druid.pool.DruidDataSource;
import com.crayon.teacher.entity.Teacher;
import com.crayon.teacher.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Mengdl
 * @date 2024/11/29
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TeacherApplication.class)
public class TestTeacher {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DataSource dataSource;

    @Test
    public void queryData() {
        Teacher info = teacherService.getById(1L);
        System.out.println(info);
    }

    @Test
    public void checkPoolConfig() throws SQLException {
        System.out.println("当前使用的数据源: " + dataSource.getClass().getName());
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druid = (DruidDataSource) dataSource;
            System.out.println(String.format(
                    "Druid 连接池配置：maxActive=%d, initialSize=%d, maxWait=%d",
                    druid.getMaxActive(),
                    druid.getInitialSize(),
                    druid.getMaxWait())
            );
        }
        System.out.println( "未使用 Druid 连接池！");
    }

}
