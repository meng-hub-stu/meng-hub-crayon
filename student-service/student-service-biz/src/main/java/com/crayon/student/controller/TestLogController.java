package com.crayon.student.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
