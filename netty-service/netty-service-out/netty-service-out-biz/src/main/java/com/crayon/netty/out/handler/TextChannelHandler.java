package com.crayon.netty.out.handler;

import com.crayon.netty.out.netty.NettyChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Slf4j
public class TextChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final AttributeKey<String> studentKey = AttributeKey.valueOf("studentKey");
    private final AttributeKey<String> teacherKey = AttributeKey.valueOf("teacherKey");

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelManager.clearChannel(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("netty-out error,ex:{}", cause.getMessage());
        ctx.close();
    }

    /**
     * 接收客户端的消息内容
     * @param channelHandlerContext channel
     * @param tx 消息内容
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame tx) throws Exception {
        //客户端发过来的消息
        String clientMessage = tx.text();
        //可以通过消息类型，添加不同的消息分组channel
        log.info("客户端发来的消息：{}", clientMessage);
    }

    /**
     * 连接
     * @param ctx channel
     * @throws Exception 异常
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered channelId: {}", ctx.channel().id());
        Channel channel = ctx.channel();
        channel.attr(studentKey);
        NettyChannelManager.addChannel(ctx.channel());
    }

}
