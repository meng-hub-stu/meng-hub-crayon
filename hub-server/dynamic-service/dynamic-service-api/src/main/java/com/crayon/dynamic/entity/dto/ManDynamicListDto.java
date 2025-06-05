package com.crayon.dynamic.entity.dto;

import com.crayon.dynamic.entity.model.ManDynamic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Mengdl
 * @date 2025/06/05
 */
@Data
public class ManDynamicListDto {

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "数据列表")
    private List<ManDynamic> manDynamics;

}
