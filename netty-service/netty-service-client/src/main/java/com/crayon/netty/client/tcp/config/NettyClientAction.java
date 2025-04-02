package com.crayon.netty.client.tcp.config;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@FunctionalInterface
public interface NettyClientAction {
    /**
     * 回调函数
     *
     * @param data 数据
     */
    void act(String data);
}
