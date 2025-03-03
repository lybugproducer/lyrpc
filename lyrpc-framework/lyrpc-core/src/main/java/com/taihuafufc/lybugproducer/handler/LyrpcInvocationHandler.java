package com.taihuafufc.lybugproducer.handler;

import com.taihuafufc.lybugproducer.cache.LyrpcConsumerCache;
import com.taihuafufc.lybugproducer.discovery.LyrpcRegistry;
import com.taihuafufc.lybugproducer.distribute.LyrpcIdGenerator;
import com.taihuafufc.lybugproducer.enumeration.LyrpcMessageType;
import com.taihuafufc.lybugproducer.trans.LyrpcRequest;
import com.taihuafufc.lybugproducer.trans.LyrpcRequestPayload;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 服务消费方反向代理类
 *
 * @author lybugproducer
 * @since 2025/2/9 13:43
 */
@Slf4j
public class LyrpcInvocationHandler<T> implements InvocationHandler {

    private final Class<T> interfaceClass;

    private final LyrpcRegistry registry;

    private final LyrpcConsumerCache cache;

    private final LyrpcIdGenerator idGenerator;

    public LyrpcInvocationHandler(LyrpcConsumerCache cache, Class<T> interfaceClass, LyrpcRegistry registry, LyrpcIdGenerator idGenerator) {
        this.interfaceClass = interfaceClass;
        this.registry = registry;
        this.cache = cache;
        this.idGenerator = idGenerator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 使用注册中心 进行服务发现
        List<String> list = registry.discover(interfaceClass);

        // 使用负载均衡算法 选择一个服务节点
        String discover = list.get(0);

        // 选择一个 channel 进行远程调用
        Channel channel = cache.getChannel(discover);

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
