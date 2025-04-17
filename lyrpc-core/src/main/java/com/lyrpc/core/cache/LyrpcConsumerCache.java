package com.lyrpc.core.cache;

import com.lyrpc.core.Lyrpc;
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
    private final Map<String, Channel> channelPool;

    // 通过 requestId 作为 key 缓存已经发送的请求
    private final Map<Long, CompletableFuture<Object>> requestCache;

    // 通过 requestId 作为 key 缓存请求的返回值
    private final Map<Long, Class<?>> resultTypeCache;

    // 通过 interfaceClass 作为 key 缓存服务提供方的地址的列表
    private final Map<Class<? extends Lyrpc>, List<String>> providerAddressCache;

    public LyrpcConsumerCache(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.providerAddressCache = new ConcurrentHashMap<>();
        this.channelPool = new ConcurrentHashMap<>();
        this.requestCache = new ConcurrentHashMap<>();
        this.resultTypeCache = new ConcurrentHashMap<>();
    }

    /**
     * 获取 channel 如果 channel 不存在则创建 channel 并缓存
     *
     * @param address ip:port
     * @return channel
     */
    public Channel getOrCreateChannel(String address) {

        // 这里使用 computeIfAbsent 方法保证线程安全
        // 如果缓存中存在该地址的 channel 则直接返回
        // 如果缓存中不存在该地址的 channel 则创建 channel 并缓存
        Channel channel = channelPool.computeIfAbsent(address, addr -> {
            log.info("create channel, address: {}", address);

            // 获取 ip 和 port
            String[] ipAndPort = address.split(":");
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);

            try {
                // 连接服务提供方
                ChannelFuture connect = bootstrap.connect(ip, port);
                Channel newChannel = connect.await().channel();
                log.info("create newChannel success, address: {}, newChannel: {}", address, newChannel);
                return newChannel;
            } catch (InterruptedException e) {
                log.error("create channel failed, address: {}, cause: {}", address, e.getMessage());
                throw new RuntimeException(e);
            }
        });

        log.info("get channel success, address: {}, channel: {}", address, channel);

        return channel;
    }

    /**
     * 移除 channel 并关闭连接
     *
     * @param address 传入的失效的地址
     */
    public void removeChannel(String address) {
        // 从缓存中 移除该连接
        Channel remove = channelPool.remove(address);

        // 关闭连接
        if (remove != null && remove.isActive()) {
            try {
                remove.close().await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("remove channel success, address: {}, channel: {}", address, remove);
        }
    }

    /**
     * 添加待处理请求
     *
     * @param requestId         请求 id
     * @param completableFuture 请求结果 future
     */
    public void addPendingRequest(long requestId, CompletableFuture<Object> completableFuture) {
        requestCache.put(requestId, completableFuture);

        log.info("add pending request success, requestId: {}, completableFuture: {}", requestId, completableFuture);
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
     * @param interfaceClass 服务接口类
     * @return 服务地址列表
     */
    public List<String> getProviderAddressList(Class<? extends Lyrpc> interfaceClass) {
        return providerAddressCache.get(interfaceClass);
    }

    /**
     * 更新服务地址列表
     *
     * @param interfaceClass     服务接口类
     * @param serviceAddressList 服务地址列表
     * @return 更新前的服务地址列表
     */
    public List<String> refreshProviderAddressList(Class<? extends Lyrpc> interfaceClass, List<String> serviceAddressList) {
        // 先获取原有的服务地址列表
        List<String> providerAddressList = providerAddressCache.get(interfaceClass);

        // 更新服务地址列表
        providerAddressCache.put(interfaceClass, serviceAddressList);

        // 以上步骤不需要加锁 因为外面加了一把大锁
        return providerAddressList;
    }

}
