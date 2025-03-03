package com.crayon.core.service.impl;

import com.alibaba.fastjson2.JSON;
import com.crayon.core.message.ThirdMessage;
import com.crayon.core.netty.NettyChannelManager;
import com.crayon.core.service.CoreService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.alibaba.nacos.common.utils.CollectionUtils.isNotEmpty;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Service
@RequiredArgsConstructor
public class CoreServiceImpl implements CoreService {

    @Override
    public void sendMessage() {
        for (int i = 0; i < 20; i++) {
            System.out.println("发送消息" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ThirdMessage message = ThirdMessage.builder()
                    .messageId(i)
                    .messageContent("第几条消息了： " + i)
                    .type(i <= 5 ? "test" : "dev")
                    .build();
            ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(message), CharsetUtil.UTF_8);
            for (Map.Entry<ChannelId, List<Channel>> channelIdListEntry : NettyChannelManager.getCHANNEL_GROUP_MAP().entrySet()) {
                List<Channel> channels = channelIdListEntry.getValue();
                if (isNotEmpty(channels)) {
                    channels.forEach(channel -> {
                        channel.writeAndFlush(byteBuf);
                    });
                }
            }
        }
    }

}
