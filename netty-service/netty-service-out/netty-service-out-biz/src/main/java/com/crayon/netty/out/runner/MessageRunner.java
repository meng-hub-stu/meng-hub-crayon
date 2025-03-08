package com.crayon.netty.out.runner;

import com.crayon.netty.client.server.NettyClientConnect;
import com.crayon.netty.out.netty.NettyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2025/3/5
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRunner implements ApplicationRunner {

    private final NettyClientConnect nettyClientConnect;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        nettyClientConnect.connectServer(data -> {
            log.info("netty out receive message:{}", data);
            for (Map.Entry<ChannelId, List<Channel>> listEntry : NettyChannelManager.getCHANNEL_GROUP_MAP().entrySet()) {
                List<Channel> channels = listEntry.getValue();
                channels.forEach(channel -> {
                    channel.writeAndFlush(new TextWebSocketFrame("out 转发" + data));
                });
            }

        });

    }

}
