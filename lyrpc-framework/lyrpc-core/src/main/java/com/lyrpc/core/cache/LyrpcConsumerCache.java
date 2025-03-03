package com.lyrpc.core.cache;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务消费方缓存管理器
 *
 * @author lybugproducer
 * @since 2025/2/16 13:02
 */
@Slf4j
public class LyrpcConsumerCache {

    // 传入 netty 客户端
    private final Bootstrap bootstrap;

    // 通过 ip:port 作为 key 缓存已经建立的 channel
    private final Map<String, Channel> channelCache;

    // 通过 requestId 作为 key 缓存已经发送的请求
    private final Map<Long, CompletableFuture<Object>> requestCache;

    // 通过 requestId 作为 key 缓存请求的返回值
    private final Map<Long, Class<?>> resultTypeCache;

    // 通过 clazz 作为 key 缓存服务的地址的列表
    private final Map<Class<?>, List<String>> serviceAddressCache;

    public LyrpcConsumerCache(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.serviceAddressCache = new ConcurrentHashMap<>();
        this.channelCache = new ConcurrentHashMap<>();
        this.requestCache = new ConcurrentHashMap<>();
        this.resultTypeCache = new ConcurrentHashMap<>();
    }

    /**
     * 获取 channel 如果 channel 不存在则创建 channel 并缓存
     *
     * @param address ip:port
     * @return channel
     */
    public Channel getChannel(String address) {

        // 先从缓存中 尝试获取 channel 如果存在则直接返回
        Channel channel = channelCache.get(address);

        // 使用双重检查锁定 创建 channel 并缓存
        if (channel == null) {
            synchronized (channelCache) {
                channel = channelCache.get(address);
                if (channel == null) {

                    log.info("channel not exist, create channel, address: {}", address);

                    // 获取 ip 和 port
                    String[] ipAndPort = address.split(":");
                    String ip = ipAndPort[0];
                    int port = Integer.parseInt(ipAndPort[1]);

                    try {
                        // 连接服务生产者
                        ChannelFuture connect = bootstrap.connect(ip, port);

                        // 这里会阻塞直到连接成功
                        channel = connect.await().channel();

                        // 缓存 channel
                        channelCache.put(address, channel);

                        log.info("create channel success, address: {}, channel: {}",
                                address, channel);
                    } catch (InterruptedException e) {
                        log.error("create channel failed, address: {}, cause: {}",
                                address, e.getMessage());
                    }
                }
            }
        }

        log.info("get channel success, address: {}, channel: {}", address, channel);

        return channel;
    }

    /**
     * 添加待处理请求
     *
     * @param requestId         请求 id
     * @param completableFuture 请求结果 future
     */
    public void addPendingRequest(long requestId, CompletableFuture<Object> completableFuture) {
        requestCache.put(requestId, completableFuture);

        log.info("add pending request success, requestId: {}, completableFuture: {}",
                requestId, completableFuture);
    }

    /**
     * 完成请求
     *
     * @param requestId 请求 id
     * @param result    请求结果
     */
    public void completeRequest(long requestId, Object result) {
        CompletableFuture<Object> completableFuture = requestCache.remove(requestId);
        if (completableFuture != null) {
            // 唤醒主线程 完成请求
            completableFuture.complete(result);

            log.info("complete request success, requestId: {}, result: {}", requestId, result);
        }
    }

    /**
     * 从缓存中获取请求结果类型
     *
     * @param requestId 请求 id
     * @return 请求结果类型
     */
    public Class<?> getResultType(long requestId) {
        return resultTypeCache.get(requestId);
    }

    /**
     * 缓存请求结果类型
     *
     * @param requestId  请求 id
     * @param resultType 请求结果类型
     */
    public void cacheResultType(long requestId, Class<?> resultType) {
        resultTypeCache.put(requestId, resultType);
    }

    /**
     * 通过服务名获取服务地址列表
     *
     * @param clazz 服务接口类
     * @return 服务地址列表
     */
    public List<String> getServiceAddressList(Class<?> clazz) {
        return serviceAddressCache.get(clazz);
    }

    /**
     * 更新服务地址列表
     *
     * @param clazz              服务接口类
     * @param serviceAddressList 服务地址列表
     */
    public void refreshServiceAddressList(Class<?> clazz, List<String> serviceAddressList) {
        serviceAddressCache.put(clazz, serviceAddressList);
    }
}
