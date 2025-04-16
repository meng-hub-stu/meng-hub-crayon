package com.crayon.student.config;

import com.crayon.student.config.handler.CustomWebMvcConfigurer;
import com.crayon.student.config.handler.LogInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mengdl
 * @date 2025/03/12
 */
@Component
public class StudentConfigure {

    @Bean
    @ConditionalOnMissingBean(LogInterceptor.class)
    public LogInterceptor customLogInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(CustomWebMvcConfigurer.class)
    @ConditionalOnBean(LogInterceptor.class)
    public CustomWebMvcConfigurer customWebMvcConfigurer(LogInterceptor logInterceptor) {
        return new CustomWebMvcConfigurer(logInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean(RequestInterceptor.class)
    public RequestInterceptor requestInterceptor() {
        return new CustomRequestInterceptor();
    }

    @Bean("virtualThreadPerTaskExecutor")
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService createPoolExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
