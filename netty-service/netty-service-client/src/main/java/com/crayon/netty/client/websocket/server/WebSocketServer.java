package com.crayon.netty.client.websocket.server;

import com.crayon.netty.client.websocket.config.NettyClientMessage;
import com.crayon.netty.client.websocket.handler.WebSocketClientHandler;
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

/**
 * @author Mengdl
 * @date 2025/03/24
 */
@Slf4j
public class WebSocketServer {

    private final URI uri;
    private final NettyClientMessage nettyClientMessage;
    private Bootstrap bootstrap;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Integer retryCount = 0;
    private final Integer retryMax = 3;

    public WebSocketServer(URI uri, NettyClientMessage nettyClientMessage) {
        this.uri = uri;
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
//                        pipeline.addLast(new WebSocketServerProtocolHandler(
//                                "uri",
//                                "webSocket",
//                                true,
//                                65536 * 10,
//                                false,
//                                true));
                        // WebSocket协议处理器
                        pipeline.addLast(new WebSocketClientHandler(
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        uri,
                                        WebSocketVersion.V13,
                                        null,
                                        false,
                                        new DefaultHttpHeaders()),
                                nettyClientMessage,
                                WebSocketServer.this
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

    public void reconnect() {
        if (retryCount > retryMax) {
            log.error("重连失败，已达最大重试次数");
            return;
        }
        retryCount++;
        try {
            log.info("retry connect {} (尝试 {}/{} 在{} ms后)...", uri, retryCount, retryMax, 3000);
            Thread.sleep(3000);
            connect();
        } catch (InterruptedException e) {
            log.error("尝试重新连接中断，异常:{}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("重连失败:{}", e.getMessage());
            reconnect();
        }
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

}
