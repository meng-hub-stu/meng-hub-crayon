package com.crayon.netty.client.server;

import com.crayon.netty.client.config.NettyClientAction;
import com.crayon.netty.client.config.NettyClientProperties;
import com.crayon.netty.client.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Mengdl
 * @date 2025/3/5
 */
@Slf4j
public class NettyClientServer {

    private final NettyClientAction nettyClientAction;
    private final URI uri;
    private final NettyClientProperties nettyClientProperties;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private int retryCount = 0;

    public NettyClientServer(NettyClientAction nettyClientAction, String uriStr, NettyClientProperties nettyClientProperties) throws URISyntaxException {
        this.uri = new URI(uriStr);
        this.nettyClientAction = nettyClientAction;
        this.nettyClientProperties = nettyClientProperties;
    }

    public void init() {
        bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                socketChannel.pipeline().addLast(new NettyClientHandler(nettyClientAction, NettyClientServer.this));
            }
        });
    }

    public void connect() throws InterruptedException {
        SocketAddress socketAddress = new InetSocketAddress(uri.getHost(), uri.getPort());
        log.info("Netty Client connecting to Netty server: {},host:{},port:{}", socketAddress, uri.getHost(), uri.getPort());
        ChannelFuture channelFuture = bootstrap.connect(socketAddress).sync();
        if (channelFuture.isSuccess()) {
            log.info("Netty Client connected to Netty server");
            //TODO 发送一条消息给服务端
            retryCount = 0;
            String message = "Netty Client connected to Netty server message";
            ByteBuf requestParamBuf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
            channelFuture.channel().writeAndFlush(requestParamBuf);
        }
        channelFuture.channel().closeFuture().sync();
    }

    public void reconnect() {
        if (retryCount < nettyClientProperties.getMaxRetryConnectCount()) {
            retryCount++;
            try {
                int retryDelay = nettyClientProperties.getWaitTimeReconnect();
                log.info("retry connect {} (尝试 {}/{} 在{} ms后)...", uri, retryCount, nettyClientProperties.getMaxRetryConnectCount(), retryDelay);
                Thread.sleep(retryDelay);
                connect();
            } catch (InterruptedException e) {
                log.error("尝试重新连接中断，异常:{}", e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("重连失败:{}", e.getMessage());
                reconnect();
            }
        }
    }

}
