package com.crayon.dynamic.entity.dto;

import com.crayon.dynamic.entity.model.ManDynamic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/4/21 21:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ManDynamicDto extends ManDynamic {

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "等级")
    private String grade;

}
