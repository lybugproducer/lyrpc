package com.lyrpc.core.config;

import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;
import com.lyrpc.core.loadbalancer.impl.RoundRobinLoadBalancer;
import lombok.Setter;

import java.lang.reflect.Constructor;

/**
 * 负载均衡配置类
 *
 * @author lybugproducer
 * @since 2025/3/3 16:32
 */
@Setter
public class LoadBalancerConfig {

    private Class<?> loadBalancerClass = RoundRobinLoadBalancer.class;

    public LyrpcLoadBalancer getLoadBalancer(LyrpcConsumerCache cache) {
        try {
            Constructor<?> constructor = loadBalancerClass
                    .getConstructor(LyrpcConsumerCache.class);
            return (LyrpcLoadBalancer) constructor.newInstance(cache);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
