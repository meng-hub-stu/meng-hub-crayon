package com.crayon.netty.out.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;

/**
 * @author Mengdl
 * @date 2025/04/11
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<String> AUTH_TOKEN = AttributeKey.valueOf("authToken");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            HttpHeaders headers = request.headers();
            String token = headers.get("Authorization");
            if (!isValidAuthToken(token)) {
                ctx.close();
                return;
            }
            ctx.fireChannelRead(request.retain());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private boolean isValidAuthToken(String authToken) {
        // 这里添加你的认证逻辑，例如检查数据库或缓存
        // 假设有效的Token是 "Bearer your_valid_token_here"
        return "Bearer your_valid_token_here".equals(authToken);
    }

}
