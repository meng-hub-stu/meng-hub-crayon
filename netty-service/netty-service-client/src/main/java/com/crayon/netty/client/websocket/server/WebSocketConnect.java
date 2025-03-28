package com.crayon.netty.client.websocket.server;

import com.crayon.netty.client.websocket.config.NettyClientMessage;

import java.net.URI;
import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/3/28 21:48
 **/
public class WebSocketConnect {
    public static void main(String[] args) throws Exception {
        //可能会有多个地址需要连接，这里可以用线程池来连接
        List<URI> uris = List.of(new URI("ws://127.0.0.1:12003/ws/message"));
        NettyClientMessage nettyClientMessage = data -> {
            System.out.println("Received message: " + data);
        };
        uris.forEach(uri -> Thread.ofVirtual().start(() -> {
            try {
                WebSocketServer client = new WebSocketServer(uri, nettyClientMessage);
                client.init();
                client.connect();
                client.shutdown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
        //主线程等待
        Thread.currentThread().join();
    }

}
