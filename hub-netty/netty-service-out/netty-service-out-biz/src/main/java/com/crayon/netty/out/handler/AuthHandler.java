package com.crayon.netty.out.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static org.apache.http.HttpHeaders.CONTENT_LENGTH;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

/**
 * @author Mengdl
 * @date 2025/04/11
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            HttpHeaders headers = request.headers();
            String token = headers.get("Authorization");
            if (!isValidAuthToken(token)) {
                sendForbiddenResponse(ctx);
            } else {
                ctx.fireChannelRead(request.retain());
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private boolean isValidAuthToken(String authToken) {
        // 这里添加你的认证逻辑，例如检查数据库或缓存
        // 假设有效的Token是 "Bearer your_valid_token_here"
        return "Bearer your_valid_token_here".equals(authToken);
    }

    private void sendForbiddenResponse(ChannelHandlerContext ctx) {
        String result = "{\"error\": \"Invalid Authorization token\"}";
        ByteBuf buffer = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, buffer);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
