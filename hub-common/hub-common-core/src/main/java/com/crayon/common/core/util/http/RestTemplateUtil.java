package com.crayon.common.core.util.http;

import com.alibaba.fastjson2.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank;
import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;
import static com.crayon.common.core.util.http.OkHttpUtil.convertGetParams;
import static com.crayon.common.core.util.http.OkHttpUtil.jsonConvertMap;

/**
 * @author Mengdl
 * @date 2025/03/26
 */
public class RestTemplateUtil {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    /**
     * 发送GET请求
     *
     * @param url          请求的URL
     * @param responseType 返回类型
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public static <T> T get(String url, Class<T> responseType) {
        return REST_TEMPLATE.getForObject(url, responseType);
    }

    /**
     * 发送GET请求
     *
     * @param url          请求的URL
     * @param responseType 返回类型
     * @param headers      请求头信息
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public static <T> T get(String url, Class<T> responseType, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return REST_TEMPLATE.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType).getBody();
    }

    /**
     * 发送GET请求
     *
     * @param url          请求的URL
     * @param responseType 返回类型
     * @param jsonHeader   请求头信息
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public static <T> T get(String url, Class<T> responseType, String jsonHeader) {
        HttpHeaders httpHeaders = new HttpHeaders();
        jsonConvertMap(jsonHeader).forEach((k, v) -> httpHeaders.add(k, v.toString()));
        return REST_TEMPLATE.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType).getBody();
    }

    public static <T> T get(String url, Class<T> responseType, String jsonHeader, String uriVariables) {
        String queryParams = convertGetParams(uriVariables);
        url = url + queryParams;
        HttpHeaders httpHeaders = new HttpHeaders();
        jsonConvertMap(jsonHeader).forEach((k, v) -> httpHeaders.add(k, v.toString()));
        return REST_TEMPLATE.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType).getBody();
    }

    public static <T> T get(String url, Class<T> responseType, String jsonHeader, Map<String, ?> uriVariables) {
        String queryParams = convertGetParams(JSON.toJSONString(uriVariables));
        url = url + queryParams;
        HttpHeaders httpHeaders = new HttpHeaders();
        jsonConvertMap(jsonHeader).forEach((k, v) -> httpHeaders.add(k, v.toString()));
        return REST_TEMPLATE.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType).getBody();

    }

    public static <T> T post(String url, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, entity, responseType);
        return response.getBody();
    }

    public static String requestRemote(HttpMethod httpMethod, String url, String paramJson, String headerJson) {
        String response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (isNotBlank(headerJson)) {
                jsonConvertMap(headerJson).forEach((k, v) -> headers.add(k, v.toString()));
            }
            if (HttpMethod.GET.equals(httpMethod)) {
                response = REST_TEMPLATE.exchange(url + convertGetParams(paramJson), HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
            } else {
                HttpEntity<Object> entity = new HttpEntity<>(paramJson, headers);
                response = REST_TEMPLATE.exchange(url, HttpMethod.POST, entity, String.class).getBody();
            }
            if (isBlank(response)) {
                throw new RuntimeException("request third calling interface error");
            }
        } catch (Exception e) {
            throw new RuntimeException("request third calling interface error :" + e.getMessage());
        }
        return response;
    }

    public static void main(String[] args) {
        String url = "https://new-mts-api.app-alpha.com/account/account/detail";
        String jsonParam = "{\"accountNo\":\"16456032\"}";
        String jsonHeader = "{\"Authorization\":\"Bearer a8f135a5-fc86-4fd0-bcb0-c5a5adbd0791\", \"tenant-id\":\"1\"}";
        String result = get(url, String.class, jsonHeader, jsonParam);
        System.out.println(result);
    }

}
