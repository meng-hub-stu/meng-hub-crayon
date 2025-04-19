package com.crayon.student.controller;

import com.alibaba.fastjson2.JSON;
import com.crayon.student.entity.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Mengdl
 * @date 2025/03/12
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestLogController {

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String sign() {
        log.info("这是一行info日志");
        log.error("这是一行error日志");
        return "success";
    }

    @RequestMapping(value = "/body", method = RequestMethod.POST)
    public Student test(HttpServletRequest request) throws Exception {
        String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        String body1 = request.getParameter("data");
        log.info("这是一行info日志,body:{}", body);
        Student student = JSON.parseObject(body, Student.class);
        return student;
    }

    public static void main(String[] args) {

        ZonedDateTime nowInMoscow = ZonedDateTime.now();
        System.out.println(nowInMoscow.getOffset());
        System.out.println(nowInMoscow.getOffset().getTotalSeconds() / 3600);

        ZoneId currentZone = ZoneId.systemDefault();
        System.out.println(currentZone.getId());
        System.out.println(currentZone.getId());

    }

}
