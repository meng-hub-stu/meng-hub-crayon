package com.crayon.netty.client.util;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@FunctionalInterface
public interface NettyClientMessage {
    void message(String data);
}
