package com.crayon.student.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mengdl
 * @date 2025/03/12
 */
@Component
public class StudentConfigure extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomLogInterceptor());
        //可以具体制定哪些需要拦截，哪些不拦截，其实也可以使用自定义注解更灵活完成
        // .addPathPatterns("/**")
        // .excludePathPatterns("/testxx.html");
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
