package com.lyrpc.core.loadbalancer.impl;

import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.loadbalancer.LyrpcLoadBalancer;

import java.util.List;

/**
 * 轮询负载均衡器实现类
 *
 * @author lybugproducer
 * @since 2025/2/10 17:02
 */
public class RoundRobinLoadBalancer extends LyrpcLoadBalancer {

    private int pointer;

    public RoundRobinLoadBalancer(LyrpcConsumerCache cache) {
        super(cache);
    }

    @Override
    public String select(List<String> serviceAddressList) {
        int size = serviceAddressList.size();
        if (size == 0) {
            return null;
        }
        int index = (pointer++) % size;
        return serviceAddressList.get(index);
    }
}
