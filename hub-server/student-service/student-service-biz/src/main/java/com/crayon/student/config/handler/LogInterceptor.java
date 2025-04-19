package com.crayon.student.config.handler;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.crayon.student.config.filter.RepeatableRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.jboss.logging.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 日志拦截器
 *
 * @author Mengdl
 * @date 2025/03/12
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tid = UUID.randomUUID().toString().replace("-", "");
        //可以考虑让客户端传入链路ID，但需保证一定的复杂度唯一性；如果没使用默认UUID自动生成
        if (!StringUtils.isEmpty(request.getHeader(TRACE_ID))) {
            tid = request.getHeader(TRACE_ID);
        }
        //打印到日志中
        MDC.put(TRACE_ID, tid);

        // 获取请求的内容类型
        String contentType = request.getContentType();
        // 判断请求是否可以转换为 RepeatableRequestWrapper 类型
        if (request instanceof RepeatableRequestWrapper requestWrapper) {
            // 获取请求体内容
            String requestBody = getRequestBody(requestWrapper);
            if (requestBody != null && handler instanceof HandlerMethod handlerMethod) {
                Class<?> parameterType = handlerMethod.getMethodParameters()[0].getParameterType();
                try {
                    // 反序列化请求体内容为指定的类型
                    JSON.parseObject(requestBody, parameterType);
                } catch (Exception e) {
                    log.error("请求参数解析失败,{}", e.getMessage());
                    throw new RuntimeException();
                }
            }
            putMdc(requestBody);
            // 记录日志，包含请求的 URI、内容类型、请求体和请求参数
            log.info("接口url:{}, contentType:{}, requestBody:{}, formParams:{}", request.getRequestURI(), contentType
                    , requestBody
                    , JSON.toJSONString(request.getParameterMap()));
        } else {
            // 如果请求不是 RepeatableRequestWrapper 类型，则直接记录请求参数
            if (request.getParameterMap() != null) {
                log.info("接口url:{}, contentType:{}, formParams:{}", request.getRequestURI(), contentType
                        , JSON.toJSONString(request.getParameterMap()));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.remove(TRACE_ID);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            String result = getResponseBody(responseWrapper);
            log.info("接口返回参数{}", result);
        }
        MDC.clear();
    }

    protected String getRequestBody(RepeatableRequestWrapper requestWrapper) {
        byte[] bytes = requestWrapper.getContentAsByteArray();
        if (bytes.length == 0) {
            return null;
        }
        String contentTypeHeader = requestWrapper.getContentType();
        if (StringUtils.isBlank(contentTypeHeader)) {
            log.warn("Request Content-Type is empty");
            return new String(bytes, StandardCharsets.UTF_8).replaceAll("\\r\\n|\\r|\\n", "");
        }
        ContentType contentType = ContentType.parse(contentTypeHeader);
        if (contentType.getCharset() == null) {
            return new String(bytes, StandardCharsets.UTF_8).replaceAll("\\r\\n|\\r|\\n", "");
        }
        return new String(bytes, contentType.getCharset()).replaceAll("\\r\\n|\\r|\\n", "");
    }

    protected String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        byte[] bytes = responseWrapper.getContentAsByteArray();
        if (bytes.length == 0) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8).replaceAll("\\r\\n|\\r|\\n", "");
    }

    private static void putMdc(String requestBody) {
        try {
            org.slf4j.MDC.put("requestId", requestBody == null ? null : String.valueOf(JSONPath.eval(requestBody, "$.referenceId")));
        } catch (Exception e) {
            log.error("putMdc失败,{}", e.getMessage());
        }
    }

}
