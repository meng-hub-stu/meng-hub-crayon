package com.crayon.netty.client.websocket.config;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@FunctionalInterface
public interface NettyClientMessage {
    void message(String data);
}
