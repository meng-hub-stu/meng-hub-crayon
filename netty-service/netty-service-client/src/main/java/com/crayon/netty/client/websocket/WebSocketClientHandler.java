package com.crayon.netty.client.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mengdl
 * @date 2025/03/24
 */
@Slf4j
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;
    private final NettyClientMessage nettyClientMessage;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker, NettyClientMessage nettyClientMessage) {
        this.handshaker = handshaker;
        this.nettyClientMessage = nettyClientMessage;
    }

//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        super.handlerAdded(ctx);
//        handshaker.handshake(ctx.channel());
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发起握手请求
        log.info("Handshake started");
        handshaker.handshake(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        // 处理握手响应
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
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
            nettyClientMessage.message(textFrame.text());
            System.out.println("Received message: " + textFrame.text());
        } else if (msg instanceof CloseWebSocketFrame) {
            System.out.println("Connection closed by server");
            ctx.close();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

}
