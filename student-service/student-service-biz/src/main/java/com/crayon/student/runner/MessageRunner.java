package com.crayon.student.runner;

import com.crayon.netty.client.tcp.config.NettyClientAction;
import com.crayon.netty.client.tcp.server.NettyClientConnect;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NettyClientAction nettyClientAction = (String data) -> {
            log.info("student receive message:{}", data);
        };
        nettyClientConnect.connectServer(nettyClientAction);
    }

}
