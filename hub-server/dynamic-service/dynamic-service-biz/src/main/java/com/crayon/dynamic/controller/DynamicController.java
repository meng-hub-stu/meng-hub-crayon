package com.crayon.dynamic.controller;

import com.crayon.common.core.util.R;
import com.crayon.dynamic.entity.ManDynamic;
import com.crayon.dynamic.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 动态生成表和查询sql
 *
 * @author Mengdl
 * @date 2025/04/19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/dynamic")
@Slf4j
@Tag(description = "Dynamic", name = "动态数据库管理")
@Validated
public class DynamicController {

    private final DynamicService dynamicService;

    @PostMapping(value = "/save")
    @Operation(summary = "新增数据", description = "新增数据")
    public R<Boolean> save(@RequestBody ManDynamic manDynamic) {
        return R.ok(dynamicService.save(manDynamic));
    }

    @PostMapping(value = "/save-batch")
    @Operation(summary = "新增数据", description = "新增数据")
    public R<Boolean> saveBatch(@RequestBody List<ManDynamic> manDynamics) {
        return R.ok(dynamicService.saveBatch(manDynamics));
    }

}
