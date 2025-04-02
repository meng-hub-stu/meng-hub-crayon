package com.crayon.netty.out;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@EnableFeignClients("com.crayon")
@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class NettyServerOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyServerOutApplication.class,args);
    }

}
