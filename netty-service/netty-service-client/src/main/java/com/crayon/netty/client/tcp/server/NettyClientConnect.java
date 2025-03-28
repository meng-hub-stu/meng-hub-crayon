package com.crayon.netty.client.tcp.server;

import com.crayon.netty.client.tcp.config.NettyClientAction;
import com.crayon.netty.client.tcp.config.NettyClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty;

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
//        connectToUrlPort(nettyClientAction);
        connectToServerName(nettyClientAction);
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
        List<String> uriList = new ArrayList<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(nettyClientProperties.getServiceName());
        if (isNotEmpty(instances)) {
            //TODO 可能会有多节点，如果是core服务，需要做判断，或者用别的方式
            instances.forEach(instance -> {
                String sb = instance.getUri().toString();
                uriList.add(sb);
            });
        }
        if (isNotEmpty(uriList)) {
            AtomicInteger index = new AtomicInteger(-1);
            uriList.forEach(uri -> Thread.ofVirtual().start(() -> {
                try {
                    index.getAndIncrement();
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
    }

}
