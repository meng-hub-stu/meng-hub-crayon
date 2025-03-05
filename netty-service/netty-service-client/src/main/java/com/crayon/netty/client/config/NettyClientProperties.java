package com.crayon.netty.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * @author Mengdl
 * @date 2025/3/1
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "netty.client")
public class NettyClientProperties {
    private String serviceName;
    private String channelType;
    private Boolean directHostAddressFlag = false;
    private String hostAddress;
    private Integer hostPort;
    private int waitTimeReconnect = 5000;
    private int maxRetryConnectCount = 3;
    private int connectTimeout;
    private int maxFrameLength;
    private int maxContentLength;
    private int maxHeaderLength;
    private int maxHeaderSize;
    private int maxChunkSize;
    private int maxChunkSizeInBytes;
    private String metaDataKey;
    private String metaDataValue;

}
