package com.crayon.netty.client.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

/**
 * @author Mengdl
 * @date 2025/03/24
 */
@Slf4j
public class WebSocketClient {

    private final URI uri;
    private final NettyClientMessage nettyClientMessage;
    private Bootstrap bootstrap;
    private final EventLoopGroup group = new NioEventLoopGroup();

    public WebSocketClient(URI wsUrl, NettyClientMessage nettyClientMessage) {
        this.uri = wsUrl;
        this.nettyClientMessage = nettyClientMessage;
    }

    public void init() throws Exception {
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加HTTP编解码器
                        pipeline.addLast(new HttpClientCodec());
                        // 聚合HTTP报文为FullHttpRequest/Response
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        // WebSocket协议处理器
                        pipeline.addLast(new WebSocketClientHandler(
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        uri,
                                        WebSocketVersion.V13,
                                        null,
                                        false,
                                        new DefaultHttpHeaders()),
                                nettyClientMessage
                        ));
                    }
                });
    }

    public void connect() throws Exception {
        String host = uri.getHost();
        int port = uri.getPort() == -1 ? (host.startsWith("wss") ? 443 : 80) : uri.getPort();

        // 连接服务器
        ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                System.out.println("Connected to WebSocket server");
            } else {
                System.err.println("Connection failed: " + f.cause());
            }
        });
        // 等待通道关闭（长连接）
        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws Exception {
        //可能会有多个地址需要连接，这里可以用线程池来连接
        List<URI> uris = List.of(new URI("ws://127.0.0.1:12003/ws/message"));
        NettyClientMessage nettyClientMessage = data -> {
            System.out.println("Received message: " + data);
        };
        uris.forEach(uri -> Thread.ofVirtual().start(() -> {
            try {
                WebSocketClient client = new WebSocketClient(uri, nettyClientMessage);
                client.init();
                client.connect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

}
