package com.taihuafufc.lybugproducer.loadbalancer.impl;

import com.taihuafufc.lybugproducer.loadbalancer.LyrpcLoadBalancer;

import java.util.List;

/**
 * 轮询负载均衡器实现类
 *
 * @author lybugproducer
 * @since 2025/2/10 17:02
 */
public class RoundRobinLoadBalancer implements LyrpcLoadBalancer {

    @Override
    public String select(List<String> serviceList) {
        return serviceList.get(0);
    }
}
