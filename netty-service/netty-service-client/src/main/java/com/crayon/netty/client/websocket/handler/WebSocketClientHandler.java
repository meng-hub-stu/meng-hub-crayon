package com.crayon.netty.client.websocket.handler;

import com.crayon.netty.client.tcp.config.NettyClientManager;
import com.crayon.netty.client.websocket.config.WebsocketClientAction;
import com.crayon.netty.client.websocket.server.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

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
        handshake.handshake(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        // 处理握手响应
        if (!handshake.isHandshakeComplete()) {
            try {
                handshake.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
                System.out.println("Handshake complete!");
                // 握手成功后发送测试消息（可选）
                TextWebSocketFrame frame = new TextWebSocketFrame("你好啊");
                ctx.channel().writeAndFlush(frame);
            } catch (WebSocketHandshakeException e) {
                System.err.println("Handshake failed: " + e.getMessage());
            }
            return;
        }

        // 处理WebSocket帧消息
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            websocketClientAction.act(textFrame.text());
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
        NettyClientManager.getServiceUriList().remove(uri.toString());
        webSocketServer.reconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught channelId: {}", ctx.channel().id());
        NettyClientManager.getServiceUriList().remove(uri.toString());
        super.exceptionCaught(ctx, cause);
    }

}
