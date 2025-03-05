package com.crayon.netty.out.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mengdl
 * @date 2025/3/5
 */
@Slf4j
public class HealthCheckHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest httpRequest) throws Exception {
        if (HttpMethod.GET.equals(httpRequest.method()) && "/actuator/health".equals(httpRequest.uri())) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    io.netty.handler.codec.http.HttpVersion.HTTP_1_1,
                    io.netty.handler.codec.http.HttpResponseStatus.OK,
                    Unpooled.copiedBuffer("{\"status\":\"UP\"}", CharsetUtil.UTF_8));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
            HttpUtil.setContentLength(response, response.content().readableBytes());
            channelHandlerContext.writeAndFlush(response);
        }
    }

}
