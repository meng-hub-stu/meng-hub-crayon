package com.crayon.netty.out.runner;

import com.alibaba.fastjson2.JSON;
import com.crayon.netty.client.tcp.server.NettyClientConnect;
import com.crayon.netty.out.config.NettyServerProperties;
import com.crayon.netty.out.entity.MessageVo;
import com.crayon.netty.out.netty.NettyChannelManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mengdl
 * @date 2025/3/5
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRunner implements ApplicationRunner {

    private final NettyClientConnect nettyClientConnect;
    private final NettyServerProperties nettyServerProperties;
    private static final Map<String, Long> DATE_DELAY = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        nettyClientConnect.connectServer(data -> {
            log.info("netty out receive message:{}", data);
            MessageVo messageVo = JSON.parseObject(data, MessageVo.class);
            //加上消息进行延迟
            Long startTimeStamp = DATE_DELAY.get(messageVo.getMessageId().toString());
            long nowTimeStamp = System.currentTimeMillis();
            if (startTimeStamp != null) {
                if (nowTimeStamp - startTimeStamp < nettyServerProperties.getDelayTime()) {
                    return ;
                }
            }
            //这里可以设置延时推送
            for (Map.Entry<String, List<Channel>> listEntry : NettyChannelManager.getCHANNEL_GROUP_MAP().entrySet()) {
                List<Channel> channels = listEntry.getValue();
                channels.forEach(channel -> {
                    channel.writeAndFlush(new TextWebSocketFrame("out 转发" + data));
                });
            }
            //添加到本地缓存中
            DATE_DELAY.put(messageVo.getMessageId().toString(), nowTimeStamp);
        });

    }

}
