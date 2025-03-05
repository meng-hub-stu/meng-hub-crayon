package com.crayon.netty.inner.handler;

import com.crayon.netty.inner.netty.NettyChannelManager;
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
        //可以通过消息类型，添加不同的消息分组channel
        log.info("客户端发来的消息：{}", clientMessage);
        NettyChannelManager.addChannel(channelHandlerContext.channel());
    }

}
