package com.lyrpc.core.bootstrap;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.config.DatagramConfig;
import com.lyrpc.core.config.ProviderRegistryConfig;
import com.lyrpc.core.config.ServerConfig;
import com.lyrpc.core.config.ServiceConfig;
import com.lyrpc.core.discovery.LyrpcProviderRegistry;
import com.lyrpc.core.discovery.LyrpcRegistry;
import com.lyrpc.core.handler.LyrpcMethodCallHandler;
import com.lyrpc.core.handler.LyrpcDatagramHandler;
import com.lyrpc.core.trans.LyrpcRequestDecoder;
import com.lyrpc.core.trans.LyrpcResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * LyrpcProviderBootstrap 服务提供方启动类
 *
 * @author lybugproducer
 * @since 2025/2/16 11:03
 */
@Slf4j
public class LyrpcProviderBootstrap {

    private static final LyrpcProviderBootstrap INSTANCE = new LyrpcProviderBootstrap();

    private ServerBootstrap serverBootstrap;

    private LyrpcDatagramHandler datagramHandler;

    private LyrpcProviderRegistry registry;

    private final Map<String, ServiceConfig<? extends Lyrpc>> serviceMap;

    private String address;

    private int port;

    private int workerThreads;

    private LyrpcProviderBootstrap() {
        // 私有构造函数
        serviceMap = new HashMap<>();

        log.info("provider bootstrap instance initialized");
    }

    public static LyrpcProviderBootstrap getInstance() {
        return INSTANCE;
    }


    /**
     * 配置调用方注册中心
     *
     * @param registryConfig 调用方注册中心配置
     * @return LyrpcProviderBootstrap 当前对象
     */
    public LyrpcProviderBootstrap registry(ProviderRegistryConfig registryConfig) {
        // 注册中心配置
        registry = registryConfig.getRegistry();

        log.info("provider registry config: {}", registryConfig);
        return this;
    }

    /**
     * 读取报文相关配置
     *
     * @param datagramConfig 报文相关配置 如压缩方式 序列化方式等
     * @return LyrpcProviderBootstrap 当前对象
     */
    public LyrpcProviderBootstrap datagram(DatagramConfig datagramConfig) {
        datagramHandler = datagramConfig.getDatagramHandler();

        log.info("provider datagram config: {}", datagramConfig);
        return this;
    }

    /**
     * 读取服务器配置
     *
     * @param serverConfig 服务器配置 如线程数 地址等
     */
    public LyrpcProviderBootstrap server(ServerConfig serverConfig) {
        // 读取线程数
        workerThreads = serverConfig.getWorkerThreads();

        // 读取服务器地址
        address = serverConfig.getAddress();
        port = Integer.parseInt(address.split(":")[1]);

        log.info("provider server config: {}", serverConfig);
        return this;
    }

    /**
     * 将服务注册到注册中心
     *
     * @param serviceConfig 服务配置
     * @return LyrpcProviderBootstrap 当前对象
     */
    public LyrpcProviderBootstrap publish(ServiceConfig<? extends Lyrpc> serviceConfig) {

        if (address == null) {
            // 服务器地址未配置
            throw new IllegalArgumentException("server address not configured");
        }

        // 这里是 策略设计模式 将服务注册到注册中心
        registry.register(serviceConfig, address);

        // 将服务配置加入到服务列表
        serviceMap.put(serviceConfig.getInterfaceClass().getName(), serviceConfig);

        log.info("provider service config: {}", serviceConfig);
        return this;
    }

    /**
     * 启动服务提供方
     */
    public void start() {

        if (serverBootstrap != null) {
            // 避免重复启动
            return;
        }

        // 设置事件循环组 线程数
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreads);

        // 设置服务端启动器
        serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                                // 请求解码器
                                .addLast(new LyrpcRequestDecoder(datagramHandler))
                                // 方法调用器
                                .addLast(new LyrpcMethodCallHandler(serviceMap, datagramHandler))
                                // 响应编码器
                                .addLast(new LyrpcResponseEncoder(datagramHandler));
                    }
                });

        try {
            // 启动服务提供方 监听端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("provider started on port {}", port);

            // 这里是阻塞线程 等待服务提供方关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("provider start error", e);
        } finally {
            // 优雅关闭线程组
            try {
                bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
                log.info("provider shutdown gracefully");
            } catch (InterruptedException e) {
                log.error("provider shutdown error", e);
            }
        }
    }

}
