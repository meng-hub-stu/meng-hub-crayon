package com.crayon.netty.client.tcp.handler;

import com.crayon.netty.client.tcp.config.NettyClientAction;
import com.crayon.netty.client.tcp.config.NettyClientManager;
import com.crayon.netty.client.tcp.server.NettyClientServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final NettyClientAction nettyClientAction;

    private final NettyClientServer nettyClientServer;

    private final URI uri;

    public NettyClientHandler(NettyClientAction nettyClientAction, URI uri, NettyClientServer nettyClientServer) {
        log.info("NettyClientHandler init");
        this.nettyClientAction = nettyClientAction;
        this.uri = uri;
        this.nettyClientServer = nettyClientServer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive channelId: {}", ctx.channel().id());
        NettyClientManager.getServiceUriList().remove(uri.toString());
        super.channelInactive(ctx);
        ctx.close();
        nettyClientServer.reconnect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        log.info("channelRead0 channelId: {}", channelHandlerContext.channel().id());
        //服务端发过来的消息
        var clientMessage = byteBuf.toString(CharsetUtil.UTF_8);
        nettyClientAction.act(clientMessage);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered channelId: {}", ctx.channel().id());
        super.channelRegistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught channelId: {}", ctx.channel().id());
        NettyClientManager.getServiceUriList().remove(uri.toString());
        super.exceptionCaught(ctx, cause);
    }

}
