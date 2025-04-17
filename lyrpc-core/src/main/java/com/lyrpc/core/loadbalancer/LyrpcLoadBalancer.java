package com.lyrpc.core.loadbalancer;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;

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

    /**
     * 根据 接口类型 获取服务地址
     *
     * @param interfaceClass 接口类
     * @return 服务地址
     */
    public String getProviderAddress(Class<? extends Lyrpc> interfaceClass) {
        // 从缓存中获取服务地址列表
        List<String> providerAddressList = cache.getProviderAddressList(interfaceClass);

        if (providerAddressList == null || providerAddressList.isEmpty()) {
            return null;
        }
        // 使用负载均衡算法选择一个服务实例
        return select(providerAddressList);
    }

    /**
     * 选择一个服务实例
     *
     * @param serviceList 服务实例名称
     * @return 选择的服务实例
     */
    public abstract String select(List<String> serviceList);
}
