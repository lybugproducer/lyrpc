package com.lyrpc.core.config;

import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;
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

    private Class<? extends LyrpcLoadBalancer> loadBalancerClass;

    public LyrpcLoadBalancer getLoadBalancer(LyrpcConsumerCache cache) {
        try {
            Constructor<? extends LyrpcLoadBalancer> constructor = loadBalancerClass
                    .getConstructor(LyrpcConsumerCache.class);
            return constructor.newInstance(cache);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
