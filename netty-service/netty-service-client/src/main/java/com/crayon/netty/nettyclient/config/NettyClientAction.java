package com.crayon.netty.nettyclient.config;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@FunctionalInterface
public interface NettyClientAction {
    void act(String data);
}
