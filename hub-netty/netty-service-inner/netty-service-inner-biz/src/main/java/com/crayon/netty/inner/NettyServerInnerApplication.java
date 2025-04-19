package com.crayon.netty.inner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@EnableFeignClients("com.crayon")
@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
public class NettyServerInnerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyServerInnerApplication.class,args);
    }

}
