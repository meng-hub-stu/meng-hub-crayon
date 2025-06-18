package com.crayon.netty.client.websocket.handler;

import com.crayon.netty.client.websocket.config.WebsocketClientAction;
import com.crayon.netty.client.websocket.config.WebsocketClientManager;
import com.crayon.netty.client.websocket.server.WebSocketServer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author Mengdl
 * @date 2025/03/24
 */
@Slf4j
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final URI uri;
    private final WebSocketClientHandshaker handshake;
    private final WebsocketClientAction websocketClientAction;
    private final WebSocketServer webSocketServer;

    public WebSocketClientHandler(URI uri, WebSocketClientHandshaker handshake,
                                  WebsocketClientAction websocketClientAction,
                                  WebSocketServer webSocketServer) {
        this.uri = uri;
        this.handshake = handshake;
        this.websocketClientAction = websocketClientAction;
        this.webSocketServer = webSocketServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发起握手请求
        log.info("channelActive channelId: {}", ctx.channel().id());
        ChannelFuture future = handshake.handshake(ctx.channel());
        // TODO 下面内容没有经过测试
        boolean success = future.await(5, TimeUnit.SECONDS);
        if (success && future.isSuccess()) {
            // 握手成功
            System.out.println("Handshake succeeded.");
        } else {
            // 握手失败或超时
            System.out.println("Handshake failed or timed out.");
            future.cancel(true);
        }
        future.addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                System.out.println("Asynchronous handshake succeeded.");
            } else {
                System.out.println("Asynchronous handshake failed.");
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        // 处理握手响应
        if (!handshake.isHandshakeComplete()) {
            try {
                handshake.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
                System.out.println("Handshake complete!");
                // 握手成功后发送测试消息（可选）
                TextWebSocketFrame frame = new TextWebSocketFrame("客户端连接服务端");
                ctx.channel().writeAndFlush(frame);
            } catch (WebSocketHandshakeException e) {
                System.err.println("Handshake failed: " + e.getMessage());
            }
            return;
        }

        // 处理WebSocket的消息
        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame textFrame) {
            websocketClientAction.act(textFrame.text());
        } else if (frame instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(Unpooled.copiedBuffer("pong", CharsetUtil.UTF_8)));
        } else if (msg instanceof CloseWebSocketFrame) {
            System.out.println("Connection closed by server");
            ctx.close();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered channelId: {}", ctx.channel().id());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive channelId: {}", ctx.channel().id());
//        WebsocketClientManager.getServiceUriList().remove(uri.toString());
        WebsocketClientManager.removeUri(uri.toString());
        webSocketServer.reconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught channelId: {}", ctx.channel().id());
//        WebsocketClientManager.getServiceUriList().remove(uri.toString());
        WebsocketClientManager.removeUri(uri.toString());
        super.exceptionCaught(ctx, cause);
    }

}
