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
 * @author Mengdl
 * @date 2025/3/3
 */
public class NettyChannelManager {

    @Getter
    private static final Map<ChannelId, List<Channel>> CHANNEL_GROUP_MAP = new ConcurrentHashMap<>();
    @Getter
    private static final AttributeKey<String> STUDENT_KEY = AttributeKey.valueOf("studentKey");
    @Getter
    private static final AttributeKey<String> TEACHER_KEY = AttributeKey.valueOf("teacherKey");

    /**
     * 添加channel
     * @param channel 通道
     */
    public static void addChannel(Channel channel) {
        ChannelId channelId = channel.id();
        List<Channel> channelList = CHANNEL_GROUP_MAP.get(channelId);
        if (channelList == null) {
            channelList = new ArrayList<>();
        }
        channelList.add(channel);
        CHANNEL_GROUP_MAP.put(channelId, channelList);
    }

    public static void clearChannel(Channel channel) {
        ChannelId channelId = channel.id();
        CHANNEL_GROUP_MAP.remove(channelId);
    }

}
