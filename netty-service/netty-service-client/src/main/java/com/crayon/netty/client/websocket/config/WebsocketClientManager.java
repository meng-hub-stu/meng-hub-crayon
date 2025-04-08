package com.crayon.netty.client.websocket.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mengdl
 * @date 2025/04/02
 */
@Schema(description = "websocket客户端管理器")
public class WebsocketClientManager {

    @Getter
    @Setter
    private static WebsocketClientAction websocketClientAction;

    @Getter
    @Setter
    private static Set<String> serviceUriList = new HashSet<>();

    public static void addUri(String serviceUri) {
        WebsocketClientManager.getServiceUriList().add(serviceUri);
    }

    public static void addUriList(Set<String> serviceUriList) {
        WebsocketClientManager.getServiceUriList().addAll(serviceUriList);
    }

    public static void removeUri(String serviceUri) {
        WebsocketClientManager.getServiceUriList().remove(serviceUri);
    }

}
