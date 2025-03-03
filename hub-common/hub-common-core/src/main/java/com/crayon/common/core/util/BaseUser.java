package com.crayon.common.core.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mengdl
 * @date 2023/05/31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "当前登陆人")
public class BaseUser extends BaseEntity {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "姓名")
    private String name;

}
