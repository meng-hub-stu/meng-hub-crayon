package com.crayon.dynamic.controller;

import com.crayon.common.core.util.R;
import com.crayon.dynamic.entity.model.ManDynamic;
import com.crayon.dynamic.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/detail")
    @Operation(summary = "查询数据", description = "查询数据")
    public R<List<ManDynamic>> detail() {
//        ManDynamic detail = dynamicService.detail(1L, "Mengdl");
//        ManDynamic manDynamic = dynamicService.detailMt4(1L, "mt5", "Xujing");
        ManDynamic manDynamic1 = dynamicService.detailMt5(1L, "mt5", "Xujing");
        return R.ok(List.of(manDynamic1));
    }

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        String expression = "T(com.crayon.dynamic.config.DataSourceConfig).getDataSourceName()";
        Object value = parser.parseExpression(expression).getValue();
        System.out.println("DataSource Name: " + value);
    }

}
