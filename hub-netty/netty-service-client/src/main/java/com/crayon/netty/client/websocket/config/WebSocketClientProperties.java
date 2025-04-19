package com.crayon.netty.client.websocket.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Mengdl
 * @date 2025/04/02
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "netty.websocket.client")
public class WebSocketClientProperties {

    @Schema(description = "websocket地址列表")
    private List<WebSocketUrl> webSocketUrlList;

    @Schema(description = "重连时间")
    private int waitTimeReconnect = 5000;

    @Schema(description = "重试次数")
    private int maxRetryConnectCount = 3;

    @Data
    public static class WebSocketUrl {

        @Schema(description = "地址")
        private String url;

        @Schema(description = "端口号")
        private String port;

        @Schema(description = "路径")
        private String path;
    }

}
