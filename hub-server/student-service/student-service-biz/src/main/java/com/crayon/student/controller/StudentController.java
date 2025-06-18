package com.crayon.student.controller;

import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crayon.common.core.util.R;
import com.crayon.student.entity.model.Student;
import com.crayon.student.entity.req.StudentReq;
import com.crayon.student.entity.resp.StudentResp;
import com.crayon.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 13:55
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/student")
@Slf4j
@Tag(description = "Student", name = "学生管理")
@Validated
public class StudentController {

    private final StudentService studentService;

    @GetMapping(value = "/page")
    @Operation(summary = "分页", description = "id获取详情")
    public R<Page<Student>> page(Page<Student> page) {
        return R.ok(studentService.page(page));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "详情", description = "id获取详情")
    public R<StudentResp> detail(@PathVariable(value = "id") Long id) {
        return R.ok(studentService.detail(id));
    }

    @PostMapping
    @Operation(summary = "保存", description = "保存学生数据")
    public R<String> save(@Validated @RequestBody Student student) {
        studentService.save(student);
        return R.ok(student.getId().toString());
    }

    @PutMapping
    @Operation(summary = "更新", description = "更新学生数据")
    public R<String> update(@Validated @RequestBody Student student) {
        studentService.updateById(student);
        return R.ok(student.getId().toString());
    }

    @DeleteMapping
    @Operation(summary = "删除", description = "删除学生数据")
    public R<Boolean> del(@RequestParam Long id) {
        return R.ok(studentService.removeById(id));
    }


    @PostMapping(value = "/check")
    @Operation(summary = "check", description = "check学生数据")
    public R<StudentResp> check(@RequestBody StudentReq req) {
//        log.info("reqString:{}", JSON.toJSONString(req));
        StudentResp resp = new StudentResp();
        resp.setCreateTime(LocalDateTime.now());
        resp.setId(12345555L);
//        log.info("respString:{}", JSON.toJSONString(resp));
        return R.ok(resp);
    }

    @GetMapping(value = "/checkParam")
    @Operation(summary = "校验参数", description = "校验参数")
    public R<String> checkParam(@RequestParam String accountName) {
        return R.ok(accountName);
    }


    public static void main(String[] args) {
        String credentials = "bybit_demo" + ":" + "ertuetyjdfgyjfghjwerqwevf";
        String encodedCredentials = Base64.encode(credentials.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodedCredentials);
        byte[] decode = Base64.decode(encodedCredentials.getBytes(StandardCharsets.UTF_8));
        String token = new String(decode, StandardCharsets.UTF_8);
        System.out.println(token);
    }
}
