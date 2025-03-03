package com.crayon.teacher;

import com.crayon.teacher.entity.Teacher;
import com.crayon.teacher.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

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

    @Test
    public void queryData() {
        Teacher info = teacherService.getById(1L);
        System.out.println(info);
    }

}
