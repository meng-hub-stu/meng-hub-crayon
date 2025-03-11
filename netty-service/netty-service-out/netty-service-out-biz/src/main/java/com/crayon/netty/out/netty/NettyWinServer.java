package com.crayon.netty.out.netty;

import com.crayon.netty.out.config.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/3/3
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("#{ T(org.springframework.boot.system.SystemProperties).get('os.name').toLowerCase().contains('win') }")
public class NettyWinServer {

    private final NettyServerProperties nettyServerProperties;
    private final NettyChannelInitializer nettyChannelInitializer;

    // 主线程池
    private NioEventLoopGroup mainGrp;
    // 从线程池
    private NioEventLoopGroup subGrp;

    /**
     * Netty服务器启动对象
     */
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    @PostConstruct
    public void webSocketNettyServerInit() throws InterruptedException {
        mainGrp = new NioEventLoopGroup(nettyServerProperties.getBossThreadCount());
        subGrp = new NioEventLoopGroup(nettyServerProperties.getWorkerThreadCount());
        //内存泄露检测
        //DISABLED：关闭内存泄漏检测（默认）。
        //SIMPLE：开启简单的内存泄漏检测（每个字节缓冲区会有 1/64 的概率被检查）。
        //ADVANCED：高级内存泄漏检测（每个字节缓冲区会有 1/4 的概率被检查）。
        //PARANOID：偏执的内存泄漏检测（所有字节缓冲区都会被检查，开销较大）
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);
        serverBootstrap
                // 指定使用上面创建的两个线程池
                .group(mainGrp, subGrp)
                // 指定Netty通道类型
                .channel(NioServerSocketChannel.class)
                .childOption(
                        ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(
                                nettyServerProperties.getLowWaterMark(),
                                nettyServerProperties.getHighWaterMark()))
                .option(
                        ChannelOption.SO_BACKLOG,
                        nettyServerProperties.getWaitConnectQueueSize())
                .childOption(
                        ChannelOption.SO_KEEPALIVE,
                        nettyServerProperties.isKeepAlive())
                // 指定通道初始化器用来加载当Channel收到事件消息后
                .childHandler(nettyChannelInitializer);
        start();
    }

    public void start() throws InterruptedException {
        // 绑定服务器端口，以异步的方式启动服务器
        ChannelFuture future = serverBootstrap.bind(nettyServerProperties.getPort()).sync();
        if (future.isSuccess()) {
            log.info("netty初始化完成，端口:{}", nettyServerProperties.getPort());
        }
    }

    @PreDestroy
    public void stop() {
        mainGrp.shutdownGracefully();
        subGrp.shutdownGracefully();
    }

}
