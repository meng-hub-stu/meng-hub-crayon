package com.crayon.netty.out.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * channel管理类
 *
 * @author Mengdl
 * @date 2025/3/3
 */
public class NettyChannelManager {

    @Getter
    private static final Map<String, List<Channel>> CHANNEL_GROUP_MAP = new ConcurrentHashMap<>();
    @Getter
    private static final AttributeKey<String> STUDENT_KEY = AttributeKey.valueOf("studentKey");
    @Getter
    private static final AttributeKey<String> TEACHER_KEY = AttributeKey.valueOf("teacherKey");

    /**
     * 添加channel
     *
     * @param channel 通道
     */
    public static void addChannel(Channel channel, String serverId) {
        List<Channel> channelList = CHANNEL_GROUP_MAP.computeIfAbsent(serverId, k -> new ArrayList<>());
        Channel existChannel = channelList.stream().filter(source -> source.id().asLongText().equals(channel.id()
                .asLongText())).findFirst().orElse(null);
        if (existChannel == null) {
            channelList.add(channel);
        }
    }

    public static void clearChannel(Channel channel) {
        ChannelId channelId = channel.id();
        CHANNEL_GROUP_MAP.forEach((key, value) -> {
            value.removeIf(source -> source.id().asLongText().equals(channelId.asLongText()));
            if (value.isEmpty()) {
                CHANNEL_GROUP_MAP.remove(key);
            }
        });
    }

}
