package com.crayon.teacher.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crayon.common.core.util.R;
import com.crayon.teacher.entity.Teacher;
import com.crayon.teacher.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 13:55
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping(value = "/page")
    @Operation(summary = "分页", description = "id获取详情")
    public R<Page<Teacher>> page(Page<Teacher> page) {
        return R.ok(teacherService.page(page));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "详情", description = "id获取详情")
    public R<Teacher> detail(@PathVariable(value = "id") Long id) {
        return R.ok(teacherService.getById(id));
    }

    @PostMapping
    @Operation(summary = "保存", description = "保存老师数据")
    public R<String> save(@Validated @RequestBody Teacher student) {
        teacherService.save(student);
        return R.ok(student.getId().toString());
    }

    @PutMapping
    @Operation(summary = "更新", description = "更新老师数据")
    public R<String> update(@Validated @RequestBody Teacher student) {
        teacherService.updateById(student);
        return R.ok(student.getId().toString());
    }

    @DeleteMapping
    @Operation(summary = "删除", description = "删除老师数据")
    public R<Boolean> del(@RequestParam Long id) {
        return R.ok(teacherService.removeById(id));
    }

}
