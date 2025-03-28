package com.crayon.netty.client.tcp.handler;

import com.crayon.netty.client.tcp.server.NettyClientServer;
import com.crayon.netty.client.tcp.config.NettyClientAction;
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
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final NettyClientAction nettyClientAction;

    private final NettyClientServer nettyClientServer;

    public NettyClientHandler(NettyClientAction nettyClientAction, NettyClientServer nettyClientServer) {
        this.nettyClientAction = nettyClientAction;
        this.nettyClientServer = nettyClientServer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
        nettyClientServer.reconnect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //服务端发过来的消息
        String clientMessage = byteBuf.toString(CharsetUtil.UTF_8);
        nettyClientAction.act(clientMessage);
    }

}
