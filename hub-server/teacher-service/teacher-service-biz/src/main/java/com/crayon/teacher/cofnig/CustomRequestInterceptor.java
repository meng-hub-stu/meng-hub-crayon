package com.crayon.teacher.cofnig;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mengdl
 * @date 2025/03/13
 */
@Slf4j
public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        log.info("request URL: {}, method: {}, headers: {}", template.url(), template.method(), template.headers());
    }

}
