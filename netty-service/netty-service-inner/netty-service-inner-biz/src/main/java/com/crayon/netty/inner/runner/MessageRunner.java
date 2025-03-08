package com.crayon.netty.inner.runner;

import com.alibaba.fastjson2.JSON;
import com.crayon.netty.client.config.NettyClientAction;
import com.crayon.netty.client.server.NettyClientConnect;
import com.crayon.netty.inner.netty.NettyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Slf4j
@RequiredArgsConstructor
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MessageRunner implements ApplicationRunner {

    private final NettyClientConnect nettyClientConnect;

    @Override
    public void run(ApplicationArguments args) {
        NettyClientAction nettyClientAction = (String data) -> {
            log.info("inner 接收消息，print message data:{}", data);
            log.info("inner 发送消息，send message data:{}", data);
            for (Map.Entry<ChannelId, List<Channel>> listEntry : NettyChannelManager.getCHANNEL_GROUP_MAP().entrySet()) {
                List<Channel> channels = listEntry.getValue();
                ByteBuf message = Unpooled.copiedBuffer("inner 转发" + data, CharsetUtil.UTF_8);
                channels.forEach(channel -> {
                    channel.writeAndFlush(message);
                });
            }
        };
        nettyClientConnect.connectServer(nettyClientAction);
    }

}
