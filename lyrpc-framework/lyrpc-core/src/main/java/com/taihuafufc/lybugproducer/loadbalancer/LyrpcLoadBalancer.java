package com.taihuafufc.lybugproducer.loadbalancer;

import com.taihuafufc.lybugproducer.cache.LyrpcConsumerCache;

import java.util.List;

/**
 * 负载均衡器抽象类
 *
 * @author lybugproducer
 * @since 2025/2/10 17:07
 */
public abstract class LyrpcLoadBalancer {

    private final LyrpcConsumerCache cache;

    public LyrpcLoadBalancer(LyrpcConsumerCache cache) {
        this.cache = cache;
    }

    public synchronized String choose(Class<?> clazz) {
        // 从缓存中获取服务地址列表
        List<String> serviceAddressList = cache.getServiceAddressList(clazz);

        if (serviceAddressList == null || serviceAddressList.isEmpty()) {
            return null;
        }
        // 使用负载均衡算法选择一个服务实例
        return select(serviceAddressList);
    }

    public synchronized void refresh(Class<?> clazz, List<String> serviceList) {
        // 刷新服务实例列表
        cache.refreshServiceAddressList(clazz, serviceList);
    }

    /**
     * 选择一个服务实例
     *
     * @param serviceList 服务实例名称
     * @return 选择的服务实例
     */
    public abstract String select(List<String> serviceList);
}
