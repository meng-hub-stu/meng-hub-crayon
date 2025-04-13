package com.crayon.common.core.fastjason;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/4/13 9:59
 **/
@Data
@Configuration
@ConfigurationProperties("common.core")
@ConditionalOnMissingBean(CommonCoreProperties.class)
public class CommonCoreProperties {
    private boolean enable = false;

    /**
     * 系统默认时区
     */
    private String defaultZone = ZoneId.systemDefault().toString();

    /**
     * header默认时区参数
     *  ASIA_SHANGHAI = "Asia/Shanghai";
     */
    private String zone = "zone";

}
