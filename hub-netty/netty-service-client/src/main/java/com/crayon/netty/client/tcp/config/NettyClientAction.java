package com.crayon.netty.client.tcp.config;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@Schema(description = "函数编程接收消息")
@FunctionalInterface
public interface NettyClientAction {
    /**
     * 回调函数
     *
     * @param data 数据
     */
    void act(String data);
}
