package com.crayon.netty.client.tcp.config;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mengdl
 * @date 2025/04/01
 */
public class NettyClientManager {

    @Getter
    @Setter
    private static NettyClientAction nettyClientAction;

    @Getter
    @Setter
    private static Set<String> serviceUriList = new HashSet<>();

}
