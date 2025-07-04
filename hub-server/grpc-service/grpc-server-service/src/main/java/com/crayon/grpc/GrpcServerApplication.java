package com.crayon.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 13:52
 **/
@EnableFeignClients("com.crayon")
@EnableDiscoveryClient
@SpringBootApplication
//@EnableMethodCache(basePackages = "com.crayon.teacher")
//@EnableDeptInterceptor("teacher")
public class GrpcServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcServerApplication.class,args);
    }

}
