package com.crayon.netty.out.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Mengdl
 * @date 2025/03/11
 */
@Data
@RequiredArgsConstructor
public class MessageVo {

    @Schema(description = "消息id")
    private Integer messageId;

    @Schema(description = "消息内容")
    private String messageContent;

    @Schema(description = "消息类型")
    private String type;

}
