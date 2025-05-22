package com.crayon.netty.client.websocket.server;

import com.crayon.netty.client.websocket.config.WebsocketClientAction;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;

import java.net.URI;
import java.util.List;

/**
 * @author Mengdl
 * @date 2025/04/07
 */
public class TestWebSocketConnect {
    public static void main(String[] args) throws Exception {
        //可能会有多个地址需要连接，这里可以用线程池来连接
        List<String> uris = List.of("ws://127.0.0.1:12003/ws/message");
        WebsocketClientAction websocketClientAction = data -> {
            System.out.println("Received message: " + data);
        };
        uris.forEach(uri -> Thread.ofVirtual().start(() -> {
            try {
                WebSocketServer client = new WebSocketServer(websocketClientAction, uri, null, null);
                client.init();
                client.connect();
                client.shutdown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
        //主线程等待
        Thread.currentThread().join();

        Class<?> clazz = Class.forName("com.crayon.netty.client.websocket.handler.WebSocketClientHandler");
        // 获取构造函数
        Object handler = clazz.getDeclaredConstructor(URI.class, WebSocketClientHandshaker.class,
                WebsocketClientAction.class, WebSocketServer.class).newInstance(null, null, null, null);
    }

}
