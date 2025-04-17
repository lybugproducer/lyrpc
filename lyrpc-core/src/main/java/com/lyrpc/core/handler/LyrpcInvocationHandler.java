package com.lyrpc.core.handler;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.distribute.LyrpcIdGenerator;
import com.lyrpc.core.enumeration.LyrpcMessageType;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;
import com.lyrpc.core.trans.LyrpcRequest;
import com.lyrpc.core.trans.LyrpcRequestPayload;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * 服务消费方反向代理类
 *
 * @author lybugproducer
 * @since 2025/2/9 13:43
 */
@Slf4j
public class LyrpcInvocationHandler<T> implements InvocationHandler {

    private final Class<? extends Lyrpc> interfaceClass;

    private final LyrpcConsumerCache cache;

    private final LyrpcIdGenerator idGenerator;

    private final LyrpcLoadBalancer loadBalancer;

    public LyrpcInvocationHandler(LyrpcConsumerCache cache, Class<? extends Lyrpc> interfaceClass, LyrpcIdGenerator idGenerator, LyrpcLoadBalancer loadBalancer) {
        this.interfaceClass = interfaceClass;
        this.cache = cache;
        this.idGenerator = idGenerator;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 使用负载均衡算法 选择一个服务节点
        String discover = loadBalancer.getProviderAddress(interfaceClass);

        // 选择一个 channel 进行远程调用
        Channel channel = cache.getOrCreateChannel(discover);

        // 封装请求数据
        LyrpcRequest request = buildRequest(method, args);

        // 将返回值类型写入缓存中
        cache.cacheResultType(request.getRequestId(), method.getReturnType());

        // 创建 completable future 用于异步接收服务器响应
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();

        // 将请求写入缓存中
        cache.addPendingRequest(request.getRequestId(), completableFuture);

        // 写出请求数据到 channel 进入 pipeline 执行出站的一系列操作
        channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
            log.info("request sent to {}", discover);
        });

        // 数据将会经过
        // 1. 编码器 服务消费方 序列化 压缩
        // 2. 解码器 服务提供方 解压 反序列化
        // 3. 调用器 服务提供方 调用本地方法
        // 4. 编码器 服务提供方 序列化 压缩
        // 5. 解码器 服务消费方 解压 反序列化
        // 6. 响应器 服务消费方 响应结果

        // 异步等待获取服务器响应
        return completableFuture.get();
    }

    private LyrpcRequest buildRequest(Method method, Object[] args) {
        // 封装请求负载
        LyrpcRequestPayload lyrpcRequestPayload = LyrpcRequestPayload.builder()
                .interfaceName(interfaceClass.getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        // 封装请求
        LyrpcRequest request = LyrpcRequest.builder()
                .payload(lyrpcRequestPayload)
                .requestId(idGenerator.nextId())
                .messageType(LyrpcMessageType.REQUEST.getId())
                .build();

        log.info("build request: {}", request);

        return request;
    }
}
