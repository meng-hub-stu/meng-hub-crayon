package com.crayon.netty.out.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import io.netty.util.AttributeKey;

/**
 * @author Mengdl
 * @date 2025/04/11
 */
public class AuthHandler extends SimpleChannelInboundHandler<HandshakeComplete> {
    private static final AttributeKey<String> AUTH_TOKEN = AttributeKey.valueOf("authToken");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeComplete msg) throws Exception {
        HttpHeaders entries = msg.requestHeaders();
        String authToken = entries.get("Authorization");
        if (!isValidAuthToken(authToken)) {
            ctx.close();
            return;
        }

        ctx.channel().attr(AUTH_TOKEN).set(authToken);
        ctx.fireChannelRead(msg);
    }

    private boolean isValidAuthToken(String authToken) {
        // 这里添加你的认证逻辑，例如检查数据库或缓存
        // 假设有效的Token是 "Bearer your_valid_token_here"
        return "Bearer your_valid_token_here".equals(authToken);
    }
}
