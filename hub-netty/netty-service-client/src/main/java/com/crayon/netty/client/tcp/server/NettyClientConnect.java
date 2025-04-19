package com.crayon.netty.client.tcp.server;

import com.crayon.netty.client.tcp.config.NettyClientAction;
import com.crayon.netty.client.tcp.config.NettyClientManager;
import com.crayon.netty.client.tcp.config.NettyClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNull;
import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NettyClientConnect {

    private final DiscoveryClient discoveryClient;
    private final NettyClientProperties nettyClientProperties;

    /**
     * 连接服务器
     *
     * @param nettyClientAction 回调函数
     */
    public void connectServer(NettyClientAction nettyClientAction) {
        connectToServerName(nettyClientAction);
//        connectToUrlPort(nettyClientAction);
    }

    private void connectToUrlPort(NettyClientAction nettyClientAction) {
        String uri = "http://" + nettyClientProperties.getHostAddress() + ":" + nettyClientProperties.getHostPort();
        try {
            connectToClient(nettyClientAction, uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connectToServerName(NettyClientAction nettyClientAction) {
        if (isNull(nettyClientAction)) {
            return;
        }
        NettyClientManager.setNettyClientAction(nettyClientAction);
        if (isBlank(nettyClientProperties.getServiceName())) {
            return;
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(nettyClientProperties.getServiceName());
        if (isEmpty(instances)) {
            return;
        }
        Set<String> usedUriList = new HashSet<>();
        instances.forEach(instance -> usedUriList.add(instance.getUri().toString()));
        if (isNotEmpty(NettyClientManager.getServiceUriList())) {
            usedUriList.removeAll(NettyClientManager.getServiceUriList());
            if (isEmpty(usedUriList)) {
                return;
            }
        }
        if (isNotEmpty(usedUriList)) {
            usedUriList.forEach(uri -> Thread.ofVirtual().start(() -> {
                try {
                    connectToClient(nettyClientAction, uri);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }

    private void connectToClient(NettyClientAction nettyClientAction, String url) throws URISyntaxException, InterruptedException {
        NettyClientServer client = new NettyClientServer(nettyClientAction, url, nettyClientProperties);
        client.init();
        client.connect();
//        client.shutdown();
    }

    /**
     * 定时执行，防止修改了配置文件
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
    public void healthCheck() {
        connectToServerName(NettyClientManager.getNettyClientAction());
    }

}
