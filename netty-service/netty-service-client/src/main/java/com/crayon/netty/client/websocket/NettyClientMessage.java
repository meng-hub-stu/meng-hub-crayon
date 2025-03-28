package com.crayon.netty.client.websocket;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@FunctionalInterface
public interface NettyClientMessage {
    void message(String data);
}
