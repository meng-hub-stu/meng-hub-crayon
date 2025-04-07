package com.crayon.student.runner;

import com.crayon.netty.client.tcp.config.NettyClientAction;
import com.crayon.netty.client.tcp.server.NettyClientConnect;
import com.crayon.netty.client.websocket.config.WebsocketClientAction;
import com.crayon.netty.client.websocket.server.WebSocketConnect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Slf4j
@RequiredArgsConstructor
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MessageRunner implements ApplicationRunner {

    private final NettyClientConnect nettyClientConnect;
    private final WebSocketConnect webSocketConnect;

    @Override
    public void run(ApplicationArguments args) {
        //基本的tcp连接
        NettyClientAction nettyClientAction = (String data) -> {
            log.info("netty receive message:{}", data);
        };
        nettyClientConnect.connectServer(nettyClientAction);

        //websocket的tcp连接
        WebsocketClientAction websocketClientAction = (String data) -> {
            log.info("websocket receive message:{}", data);
        };
        webSocketConnect.connectServer(websocketClientAction);
    }

}
