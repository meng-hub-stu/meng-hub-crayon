package com.crayon.common.core.util.http;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/03/26
 */
public class RestTemplateUtil {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    /**
     * 发送GET请求
     *
     * @param url 请求的URL
     * @param responseType 返回类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public static <T> T get(String url, Class<T> responseType) {
        return REST_TEMPLATE.getForObject(url, responseType);
    }

    /**
     * 发送GET请求，携带参数
     *
     * @param url 请求的URL
     * @param uriVariables 请求参数
     * @param responseType 返回类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public static <T> T get(String url, Map<String, ?> uriVariables, Class<T> responseType) {
        return REST_TEMPLATE.getForObject(url, responseType, uriVariables);
    }

    /**
     * 发送POST请求
     *
     * @param url 请求的URL
     * @param requestBody 请求体
     * @param responseType 返回类型
     * @param <T> 泛型类型
     * @return 响应结果
     */
    public static <T> T post(String url, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, entity, responseType);
        return response.getBody();
    }

}
