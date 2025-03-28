package com.crayon.teacher.runner;

import com.crayon.netty.client.tcp.server.NettyClientConnect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/3/5
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRunner implements ApplicationRunner {

    private final NettyClientConnect nettyClientConnect;

    @Override
    public void run(ApplicationArguments args) {
        nettyClientConnect.connectServer(data -> {
            log.info("teacher receive message:{}", data);
        });
    }

}
