package com.crayon.netty.client.websocket.config;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.function.Consumer;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@Schema(description = "函数编程接收消息")
@FunctionalInterface
public interface WebsocketConsumerHandler {

    void handler(Consumer<Object> data);

}
