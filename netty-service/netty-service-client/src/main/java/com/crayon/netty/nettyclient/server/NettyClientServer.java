package com.crayon.netty.nettyclient.server;

import com.crayon.netty.nettyclient.config.NettyClientAction;
import com.crayon.netty.nettyclient.config.NettyClientProperties;
import com.crayon.netty.nettyclient.handler.NettyClientHandler;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NettyClientServer {

    private final DiscoveryClient discoveryClient;
    private final NettyClientProperties nettyClientProperties;
    private Bootstrap bootstrap;
    private final EventLoopGroup group = new NioEventLoopGroup();

    /**
     * 连接服务器
     *
     * @param nettyClientAction 回调函数
     */
    public void connectServer(NettyClientAction nettyClientAction) {
//        connectToUrlPort(nettyClientAction);
        connectToServerName(nettyClientAction);
    }

    private void connectToUrlPort(NettyClientAction nettyClientAction) {
        String uri = "http://" + nettyClientProperties.getHostAddress() + ":" + nettyClientProperties.getHostPort();
        try {
            connectToUri(uri, nettyClientAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToServerName(NettyClientAction nettyClientAction) {
        List<String> uriList = new ArrayList<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(nettyClientProperties.getServiceName());
        if (isNotEmpty(instances)) {
            //TODO 可能会有多节点，如果是core服务，需要做判断，或者用别的方式
            instances.forEach(instance -> {
                String sb = instance.getUri().toString();
                uriList.add(sb);
            });
        }
        if (isNotEmpty(uriList)) {
            AtomicInteger index = new AtomicInteger(-1);
            uriList.forEach(uri -> Thread.ofVirtual().start(() -> {
                try {
                    index.getAndIncrement();
                    connectToUri(uri, nettyClientAction);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }));
        }
    }

    private void connectToUri(String uri, NettyClientAction nettyClientAction) throws URISyntaxException, InterruptedException {
        init(nettyClientAction);
        connect(uri);
    }

    public void init(NettyClientAction nettyClientAction) {
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                socketChannel.pipeline().addLast(new NettyClientHandler(nettyClientAction));
            }
        });
    }

    public void connect(String uriStr) throws InterruptedException, URISyntaxException {
        URI uri = new URI(uriStr);
        SocketAddress socketAddress = new InetSocketAddress(uri.getHost(), uri.getPort());
        log.info("Netty Client connecting to Netty server: {},host:{},port:{}", socketAddress, uri.getHost(), uri.getPort());
        ChannelFuture channelFuture = bootstrap.connect(socketAddress).sync();
        if (channelFuture.isSuccess()) {
            log.info("Netty Client connected to Netty server");
            String message = "Netty Client connected to Netty server message";
            ByteBuf requestParamBuf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
            channelFuture.channel().writeAndFlush(requestParamBuf);
        }
        channelFuture.channel().closeFuture().sync();
    }

}
