package com.lyrpc.core.config;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.distribute.LyrpcIdGenerator;
import com.lyrpc.core.handler.LyrpcInvocationHandler;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;
import lombok.Data;

import java.lang.reflect.Proxy;

/**
 * 调用接口的配置类
 *
 * @author lybugproducer
 * @since 2025/2/6 11:37
 */
@Data
public class ReferenceConfig<T> {

    private Class<? extends Lyrpc> interfaceClass;

    private LyrpcLoadBalancer loadBalancer;

    private LyrpcConsumerCache cache;

    private LyrpcIdGenerator idGenerator;

    /**
     * 通过 JDK 动态代理获取代理对象 以供服务消费方调用
     *
     * @return T 代理对象
     */
    @SuppressWarnings("unchecked")
    public T getProxy() {

        if (interfaceClass == null) {
            throw new IllegalArgumentException("interfaceClass cannot be null");
        }

        if (loadBalancer == null) {
            throw new IllegalArgumentException("loadBalancer cannot be null");
        }

        if (cache == null) {
            throw new IllegalArgumentException("cache cannot be null");
        }

        if (idGenerator == null) {
            throw new IllegalArgumentException("idGenerator cannot be null");
        }

        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{interfaceClass},
                new LyrpcInvocationHandler<>(cache, interfaceClass, idGenerator, loadBalancer));
    }
}
