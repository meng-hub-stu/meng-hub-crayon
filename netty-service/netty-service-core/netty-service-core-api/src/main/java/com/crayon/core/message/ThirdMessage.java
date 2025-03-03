package com.crayon.core.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Data
@Builder
public class ThirdMessage {

    @Schema(description = "消息id")
    private Integer messageId;

    @Schema(description = "消息内容")
    private String messageContent;

    @Schema(description = "消息类型")
    private String type;

}
