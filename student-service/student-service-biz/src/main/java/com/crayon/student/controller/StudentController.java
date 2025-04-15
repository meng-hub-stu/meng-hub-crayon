package com.crayon.student.controller;

import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crayon.common.core.util.R;
import com.crayon.student.entity.model.Student;
import com.crayon.student.entity.req.StudentReq;
import com.crayon.student.entity.resp.StudentResp;
import com.crayon.student.service.StudentService;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Validated
@RequestMapping(value = "/student")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @GetMapping(value = "/page")
    @Schema(name = "分页", description = "id获取详情")
    public R<Page<Student>> page(Page<Student> page) {
        return R.ok(studentService.page(page));
    }

    @GetMapping(value = "/{id}")
    @Schema(name = "详情", description = "id获取详情")
    public R<StudentResp> detail(@PathVariable(value = "id") Long id) {
        return R.ok(studentService.detail(id));
    }

    @PostMapping
    @Schema(name = "保存", description = "保存学生数据")
    public R<String> save(@Validated @RequestBody Student student) {
        studentService.save(student);
        return R.ok(student.getId().toString());
    }

    @PutMapping
    @Schema(name = "更新", description = "更新学生数据")
    public R<String> update(@Validated @RequestBody Student student) {
        studentService.updateById(student);
        return R.ok(student.getId().toString());
    }

    @DeleteMapping
    @Schema(name = "删除", description = "删除学生数据")
    public R<Boolean> del(@RequestParam Long id) {
        return R.ok(studentService.removeById(id));
    }


    @PostMapping(value = "/check")
    @Schema(name = "check", description = "check学生数据")
    public R<StudentResp> check(@RequestBody StudentReq req) {
//        log.info("reqString:{}", JSON.toJSONString(req));
        StudentResp resp = new StudentResp();
        resp.setCreateTime(LocalDateTime.now());
        resp.setId(12345555L);
//        log.info("respString:{}", JSON.toJSONString(resp));
        return R.ok(resp);
    }


    public static void main(String[] args) {
        String credentials = "account_au" + ":" + "dfhdftghcvsdfyhgerfty";
        String encodedCredentials = Base64.encode(credentials.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodedCredentials);
        byte[] decode = Base64.decode(encodedCredentials.getBytes(StandardCharsets.UTF_8));
        String token = new String(decode, StandardCharsets.UTF_8);
        System.out.println(token);
    }
}
