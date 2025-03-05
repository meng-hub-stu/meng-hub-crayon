package com.crayon.netty.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "netty.server")
public class NettyServerProperties {
    private int port;
    private int bossThreadCount = 2;
    private int workerThreadCount = 16;
    private int lowWaterMark;
    private int highWaterMark;
    private int maxFrameLength;
    private int waitConnectQueueSize;
    private boolean isKeepAlive;
    private String applicationName;
    private String metaDataKey;
    private String metaDataValue;
    private Long tenantId = 0L;
}
