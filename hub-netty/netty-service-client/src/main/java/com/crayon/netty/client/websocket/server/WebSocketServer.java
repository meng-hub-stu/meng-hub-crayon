package com.crayon.netty.client.websocket.server;

import com.crayon.netty.client.websocket.config.WebSocketClientProperties;
import com.crayon.netty.client.websocket.config.WebsocketClientAction;
import com.crayon.netty.client.websocket.config.WebsocketClientManager;
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
import java.net.URISyntaxException;

/**
 * @author Mengdl
 * @date 2025/03/24
 */
@Slf4j
public class WebSocketServer {

    private final WebsocketClientAction websocketClientAction;
    private final URI uri;
    private Bootstrap bootstrap;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final DefaultHttpHeaders defaultHttpHeaders;
    private final WebSocketClientProperties webSocketClientProperties;
    private Integer retryCount = 0;

    public WebSocketServer(WebsocketClientAction websocketClientAction, String uri, DefaultHttpHeaders defaultHttpHeaders, WebSocketClientProperties webSocketClientProperties) throws URISyntaxException {
        this.websocketClientAction = websocketClientAction;
        this.uri = new URI(uri);
        this.defaultHttpHeaders = defaultHttpHeaders;
        this.webSocketClientProperties = webSocketClientProperties;
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
                                uri,
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        uri,
                                        WebSocketVersion.V13,
                                        null,
                                        false,
                                        defaultHttpHeaders == null ? new DefaultHttpHeaders() : defaultHttpHeaders),
                                websocketClientAction,
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
                //连接成功之后添加到连接管理器中
//                WebsocketClientManager.getServiceUriList().add(uri.toString());
                WebsocketClientManager.addUri(uri.toString());
            } else {
                System.err.println("Connection failed: " + f.cause());
            }
        });
        // 等待通道关闭（长连接）
        future.channel().closeFuture().sync();
    }

    public void reconnect() {
        if (retryCount > webSocketClientProperties.getMaxRetryConnectCount()) {
            log.error("重连失败，已达最大重试次数");
            return;
        } else {
            shutdown();
        }
        retryCount++;
        try {
            log.info("retry connect {} (尝试 {}/{} 在{} ms后)...", uri, retryCount, webSocketClientProperties.getMaxRetryConnectCount(), webSocketClientProperties.getWaitTimeReconnect());
            Thread.sleep(webSocketClientProperties.getWaitTimeReconnect());
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
