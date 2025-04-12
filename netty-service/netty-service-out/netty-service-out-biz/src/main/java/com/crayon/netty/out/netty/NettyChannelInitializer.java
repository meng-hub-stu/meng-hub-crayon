package com.crayon.netty.out.netty;

import com.crayon.netty.out.config.NettyServerProperties;
import com.crayon.netty.out.handler.AuthHandler;
import com.crayon.netty.out.handler.HealthCheckHandler;
import com.crayon.netty.out.handler.TextChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Component
@RequiredArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyServerProperties nettyServerProperties;

    private final static String WEBSOCKET_PATH = "webSocket";

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        // 将拆分的http消息（请求内容、请求体）聚合成一个消息
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(nettyServerProperties.getMaxFrameLength(),
                0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        // 块写出
        pipeline.addLast(new ChunkedWriteHandler());
        // 配置读空闲Handler， 60分钟秒该Channel没有产生读将会触发读空闲事件
        pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.MINUTES));
        pipeline.addLast(new AuthHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(
                "/ws/message",
                WEBSOCKET_PATH,
                true,
                65536 * 10,
                false,
                true));
        pipeline.addLast(new HealthCheckHandler());
        pipeline.addLast(new TextChannelHandler());
    }

}
