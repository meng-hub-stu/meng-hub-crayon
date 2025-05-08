package com.crayon.teacher.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mengdl
 * @date 2025/04/24
 */
@Configuration
public class TeacherConfig {

    @Bean
    @ConditionalOnMissingBean(RequestInterceptor.class)
    public RequestInterceptor requestInterceptor() {
        return new CustomRequestInterceptor();
    }

}
