package com.lyrpc.core.loadbalancer.impl;

import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器实现类
 *
 * @author lybugproducer
 * @since 2025/2/10 17:02
 */
public class RoundRobinLoadBalancer extends LyrpcLoadBalancer {

    private final AtomicInteger pointer = new AtomicInteger(0);

    public RoundRobinLoadBalancer(LyrpcConsumerCache cache) {
        super(cache);
    }

    @Override
    public String select(List<String> serviceAddressList) {
        int size = serviceAddressList.size();
        if (size == 0) {
            return null;
        }
        int index = pointer.getAndIncrement() % size;
        return serviceAddressList.get(index);
    }
}
