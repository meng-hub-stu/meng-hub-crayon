package com.crayon.student.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * @author Mengdl
 * @date 2025/04/16
 */
@Slf4j
@Order
@WebFilter(urlPatterns = "/**/**")
@Component
public class RepeatableReadFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest)) {
            chain.doFilter(request, response);
            return;
        }
        String contentType = httpRequest.getContentType();

        // 判断请求是否是文件类型
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            // 是文件类型，直接调用下一个过滤器或处理程序
            chain.doFilter(request, response);
            return;
        }

        RepeatableRequestWrapper requestWrapper = new RepeatableRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // 将输出流回给当前HttpServletResponse
            responseWrapper.copyBodyToResponse();
        }
    }

}
