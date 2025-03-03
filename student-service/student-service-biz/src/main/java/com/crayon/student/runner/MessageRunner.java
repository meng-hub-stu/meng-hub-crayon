package com.crayon.student.runner;

import com.crayon.netty.nettyclient.config.NettyClientAction;
import com.crayon.netty.nettyclient.server.NettyClientServer;
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

    private final NettyClientServer nettyClientServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NettyClientAction nettyClientAction = (String data) -> {
            log.info("print message data:{}", data);
        };
        nettyClientServer.connectServer(nettyClientAction);
    }

}
