//package com.crayon.common.core.util.http;
//
//import org.springframework.http.MediaType;
//
//import java.util.Map;
//
///**
// * @author Mengdl
// * @date 2025/03/26
// */
//public class WebClientUtil {
//
//    private static final WebClient webClient = WebClient.create();
//
//    /**
//     * 发送GET请求
//     *
//     * @param url 请求的URL
//     * @param responseType 返回类型
//     * @param <T> 泛型类型
//     * @return 响应结果
//     */
//    public static <T> T get(String url, Class<T> responseType) {
//        return webClient.get()
//                .uri(url)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(responseType)
//                .block();
//    }
//
//    /**
//     * 发送GET请求，携带参数
//     *
//     * @param url 请求的URL
//     * @param uriVariables 请求参数
//     * @param responseType 返回类型
//     * @param <T> 泛型类型
//     * @return 响应结果
//     */
//    public static <T> T get(String url, Map<String, ?> uriVariables, Class<T> responseType) {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path(url).queryParams(toMultiValueMap(uriVariables)).build())
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(responseType)
//                .block();
//    }
//
//    /**
//     * 发送POST请求
//     *
//     * @param url 请求的URL
//     * @param requestBody 请求体
//     * @param responseType 返回类型
//     * @param <T> 泛型类型
//     * @return 响应结果
//     */
//    public static <T> T post(String url, Object requestBody, Class<T> responseType) {
//        return webClient.post()
//                .uri(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(responseType)
//                .block();
//    }
//
//    private static org.springframework.util.MultiValueMap<String, String> toMultiValueMap(Map<String, ?> uriVariables) {
//        org.springframework.util.MultiValueMap<String, String> multiValueMap = new org.springframework.util.LinkedMultiValueMap<>();
//        uriVariables.forEach((key, value) -> multiValueMap.add(key, value.toString()));
//        return multiValueMap;
//    }
//
//}
