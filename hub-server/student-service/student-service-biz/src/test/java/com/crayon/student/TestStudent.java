package com.crayon.student;

import com.alibaba.fastjson2.JSON;
import com.crayon.student.entity.model.Student;
import com.crayon.student.entity.req.StudentReq;
import com.crayon.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mengdl
 * @date 2025/2/27
 */
@SpringBootTest
public class TestStudent {

    @Autowired
    private StudentService studentService;


    @Test
    public void save() {
        Student student = Student.builder()
                .stuNo("123")
                .name("Mengdl")
                .sex("男")
                .height(180)
                .weight(80)
                .tenantId(1L).build();
        studentService.saveOrUpdate(student);
    }

    @Test
    public void testLongDeserializer() {
        String jsonString = "{\"id\": \"1234567890123456789\", \"name\": \"John\", \"sex\": \"Male\"}";
        StudentReq studentReq = JSON.parseObject(jsonString, StudentReq.class);
        assertEquals(Long.valueOf(1234567890123456789L), studentReq.getId());
        assertEquals("John", studentReq.getName());
        assertEquals("Male", studentReq.getSex());
    }

}
