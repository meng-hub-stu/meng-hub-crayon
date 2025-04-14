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
 * @date 2025/04/14
 */
public class PathHandler extends ChannelInboundHandlerAdapter {

    private final String expectedPath;

    public PathHandler(String expectedPath) {
        this.expectedPath = expectedPath;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            if (request.method() == HttpMethod.GET && request.uri().equals(expectedPath)) {
                ctx.fireChannelRead(request.retain());
            } else {
                sendForbiddenResponse(ctx);
            }
        } else {
            sendForbiddenResponse(ctx);
        }
    }

    private void sendForbiddenResponse(ChannelHandlerContext ctx) {
        String result = "{\"error\": \"path error\"}";
        ByteBuf buffer = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, buffer);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
