package com.crayon.netty.out.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/3/4
 */
@Configuration
@RequiredArgsConstructor
public class NacosRegistry {
    private final NettyServerProperties nettyServerProperties;
    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    private final InetUtils inetUtils;

    @PostConstruct
    private void registerNamingService() throws NacosException {
        Instance instance = new Instance();
        InetAddress address = inetUtils.findFirstNonLoopbackAddress();
        instance.setIp(address.getHostAddress());
        instance.setPort(nettyServerProperties.getPort());
        // 设置 metaData
        if (CharSequenceUtil.isNotBlank(nettyServerProperties.getMetaDataKey()) && CharSequenceUtil.isNotBlank(nettyServerProperties.getMetaDataValue())) {
            Map<String, String> metaData = new HashMap<>();
            metaData.put(nettyServerProperties.getMetaDataKey(), nettyServerProperties.getMetaDataValue());
            instance.setMetadata(metaData);
        }
        NamingService namingService = NamingFactory.createNamingService(nacosDiscoveryProperties.getNacosProperties());
        namingService.registerInstance(nettyServerProperties.getApplicationName(), instance);
    }

}
