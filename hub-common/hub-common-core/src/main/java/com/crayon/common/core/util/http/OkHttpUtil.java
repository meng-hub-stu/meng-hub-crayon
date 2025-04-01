package com.crayon.common.core.util.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/03/27
 */
@Slf4j
public class OkHttpUtil {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    public static String get(String url, String jsonParam, String jsonHeader) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url + convertGetParams(jsonParam))
                .get();
        Headers headers = convertHeader(jsonHeader);
        if (headers != null) {
            requestBuilder.headers(headers);
        }
        Request request = requestBuilder.build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String post(String url, String jsonBody, String jsonHeader) {
        RequestBody body = RequestBody.create(jsonBody, JSON_MEDIA_TYPE);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        Headers headers = convertHeader(jsonHeader);
        if (headers != null) {
            requestBuilder.headers(headers);
        }
        Request request = requestBuilder.build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 处理get请求参数
     *
     * @param jsonParam 请求jason 参数
     * @return 结果
     */
    public static String convertGetParams(String jsonParam) {
        if (jsonParam == null || jsonParam.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        jsonConvertMap(jsonParam).forEach((k, v) -> stringBuilder.append('&').append(k).append('=').append(v));
        if (!stringBuilder.isEmpty()) {
            stringBuilder.setCharAt(0, '?');
        }
        return stringBuilder.toString();
    }

    /**
     * 处理header
     *
     * @param jsonHeader jsonHeader
     * @return headers
     */
    public static Headers convertHeader(String jsonHeader) {
        if (jsonHeader == null || jsonHeader.isEmpty()) {
            return null;
        }
        Headers.Builder headerBuilder = new Headers.Builder()
                .set("Content-Type", "application/json");
        jsonConvertMap(jsonHeader).forEach((k, v) -> headerBuilder.set(k, v.toString()));
        return headerBuilder.build();
    }

    /**
     * json转map
     *
     * @param jsonStr json字符串
     * @return map
     */
    public static Map<String, Object> jsonConvertMap(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<>() {});
    }

    public static void main(String[] args) {
        String url = "https://new-mts-api.app-alpha.com/account/account/detail";
        String jsonParam = "{\"accountNo\":\"16456032\"}";
        String jsonHeader = "{\"Authorization\":\"Bearer 8884bee2-341e-4ff2-8b40-2f979dbe0d47\", \"tenant-id\":\"1\"}";
        String result = get(url, jsonParam, jsonHeader);
        System.out.println(result);
        AccountResp<Account> accountResp = JSON.parseObject(result,  new TypeReference<AccountResp<Account>>() {});
        if (accountResp != null && accountResp.getData() != null) {
            System.out.println(accountResp.getData().getName());
        }
    }

}
