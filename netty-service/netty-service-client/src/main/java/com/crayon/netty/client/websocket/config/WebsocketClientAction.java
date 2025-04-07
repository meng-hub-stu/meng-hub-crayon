package com.crayon.netty.client.websocket.config;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@Schema(description = "函数编程接收消息")
@FunctionalInterface
public interface WebsocketClientAction {

    void act(String data);

}
