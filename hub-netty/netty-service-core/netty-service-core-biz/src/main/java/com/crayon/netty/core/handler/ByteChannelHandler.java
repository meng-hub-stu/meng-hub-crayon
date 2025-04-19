package com.crayon.netty.core.handler;

import com.crayon.netty.core.netty.NettyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Slf4j
public class ByteChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("netty-core Inactive");
        NettyChannelManager.clearChannel(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("netty-core error,ex:{}", cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //客户端发过来的消息
        String clientMessage = byteBuf.toString(CharsetUtil.UTF_8);
        log.info("客户端发来的消息：{}", clientMessage);
        NettyChannelManager.addChannel(channelHandlerContext.channel());
    }

}
