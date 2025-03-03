package com.taihuafufc.lybugproducer.loadbalancer.impl;

import com.taihuafufc.lybugproducer.cache.LyrpcConsumerCache;
import com.taihuafufc.lybugproducer.discovery.LyrpcRegistry;
import com.taihuafufc.lybugproducer.loadbalancer.LyrpcLoadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
