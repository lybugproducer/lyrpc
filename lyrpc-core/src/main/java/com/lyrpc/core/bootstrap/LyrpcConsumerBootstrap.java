package com.lyrpc.core.bootstrap;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.config.*;
import com.lyrpc.core.discovery.LyrpcConsumerRegistry;
import com.lyrpc.core.distribute.LyrpcIdGenerator;
import com.lyrpc.core.handler.LyrpcResultHandler;
import com.lyrpc.core.handler.LyrpcDatagramHandler;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;
import com.lyrpc.core.trans.LyrpcRequestEncoder;
import com.lyrpc.core.trans.LyrpcResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * LyrpcConsumerBootstrap 服务调用方启动类
 *
 * @author lybugproducer
 * @since 2025/2/16 10:53
 */
@Slf4j
public class LyrpcConsumerBootstrap {

    private static final LyrpcConsumerBootstrap INSTANCE = new LyrpcConsumerBootstrap();

    private final Bootstrap bootstrap;

    private LyrpcConsumerRegistry registry;

    private LyrpcLoadBalancer loadBalancer;

    private final LyrpcConsumerCache cache;

    private LyrpcDatagramHandler datagramHandler;

    private final LyrpcIdGenerator idGenerator;

    private LyrpcConsumerBootstrap() {
        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class);
        cache = new LyrpcConsumerCache(bootstrap);
        idGenerator = new LyrpcIdGenerator();
        log.info("consumer bootstrap instance initialized");
    }

    public static LyrpcConsumerBootstrap getInstance() {
        return INSTANCE;
    }

    /**
     * 配置调用方注册中心信息
     *
     * @param registryConfig 注册中心配置
     * @return LyrpcConsumerBootstrap 实例
     * @author lybugproducer
     * @since 2025/2/16 10:54
     */
    public LyrpcConsumerBootstrap registry(ConsumerRegistryConfig registryConfig) {
        // 注册中心配置
        registry = registryConfig.getRegistry(cache);

        log.info("consumer registry config: {}", registryConfig);
        return this;
    }

    /**
     * 配置负载均衡器
     *
     * @param loadBalancerConfig 负载均衡器配置
     * @return LyrpcConsumerBootstrap 实例
     */
    public LyrpcConsumerBootstrap loadBalancer(LoadBalancerConfig loadBalancerConfig) {
        loadBalancer = loadBalancerConfig.getLoadBalancer(cache);

        log.info("consumer load balancer config: {}", loadBalancerConfig);
        return this;
    }

    /**
     * 配置报文相关配置
     *
     * @param datagramConfig 报文相关配置 如压缩方式 序列化方式等
     * @return LyrpcConsumerBootstrap 当前对象
     */
    public LyrpcConsumerBootstrap datagram(DatagramConfig datagramConfig) {
        datagramHandler = datagramConfig.getDatagramHandler();

        // 初始化报文处理器
        initChannel();

        log.info("consumer datagram config: {}", datagramConfig);
        return this;
    }

    /**
     * 配置服务消费方服务器信息
     *
     * @param clientConfig 客户端配置
     * @return LyrpcConsumerBootstrap 实例
     * @author lybugproducer
     * @since 2025/3/3 10:54
     */
    public LyrpcConsumerBootstrap client(ClientConfig clientConfig) {
        long dataCenterId = clientConfig.getDataCenterId();
        long workerId = clientConfig.getWorkerId();
        idGenerator.setClientId(dataCenterId, workerId);

        log.info("consumer client config: {}", clientConfig);
        return this;
    }

    /**
     * 配置调用服务信息
     *
     * @param referenceConfig 服务配置
     * @return LyrpcConsumerBootstrap 实例
     * @author lybugproducer
     * @since 2025/2/16 10:54
     */
    public LyrpcConsumerBootstrap reference(ReferenceConfig<? extends Lyrpc> referenceConfig) {
        referenceConfig.setLoadBalancer(loadBalancer);
        referenceConfig.setCache(cache);
        referenceConfig.setIdGenerator(idGenerator);

        // 初始化服务提供方缓存列表
        initDiscover(referenceConfig);

        log.info("consumer reference config: {}", referenceConfig);
        return this;
    }

    /**
     * 初始化 Netty 客户端报文处理器
     *
     * @author lybugproducer
     * @since 2025/2/16 10:54
     */
    private void initChannel() {
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) {
                nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                        // 请求编码器
                        .addLast(new LyrpcRequestEncoder(datagramHandler))
                        // 响应解码器
                        .addLast(new LyrpcResponseDecoder(datagramHandler))
                        // 处理结果
                        .addLast(new LyrpcResultHandler(cache, datagramHandler));
            }
        });
    }

    /**
     * 初始化服务提供方缓存列表
     *
     * @param referenceConfig 服务配置
     */
    private void initDiscover(ReferenceConfig<? extends Lyrpc> referenceConfig) {
        Class<? extends Lyrpc> interfaceClass = referenceConfig.getInterfaceClass();

        synchronized (interfaceClass) {
            // 启动时 进行服务发现 初始化服务提供方缓存列表
            List<String> addressList = registry.discover(interfaceClass);

            // 刷新服务地址列表
            cache.refreshProviderAddressList(interfaceClass, addressList);
        }
    }
}
