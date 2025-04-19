package com.crayon.netty.inner.netty;

import com.crayon.netty.inner.config.NettyServerProperties;
import com.crayon.netty.inner.handler.ByteChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Component
@RequiredArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyServerProperties nettyServerProperties;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(nettyServerProperties.getMaxFrameLength(),
                0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ByteChannelHandler());
    }

}
