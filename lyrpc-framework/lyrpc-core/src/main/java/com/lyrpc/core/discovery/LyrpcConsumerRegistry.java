package com.lyrpc.core.discovery;

import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.config.ServiceConfig;

import java.util.List;
import java.util.Set;

/**
 * 服务消费方注册中心抽象类
 *
 * @author lybugproducer
 * @since 2025/3/3 14:28
 */
public abstract class LyrpcConsumerRegistry implements LyrpcRegistry {

    private final LyrpcConsumerCache cache;

    public LyrpcConsumerRegistry(LyrpcConsumerCache cache, Set<String> addressSet) {
        this.cache = cache;
        initRegistry(addressSet);
    }

    /**
     * 发现服务
     *
     * @param clazz 服务接口
     * @return 服务地址
     */
    public abstract List<String> discover(Class<?> clazz);

    @Override
    public void register(ServiceConfig<?> config, String address) {}

    protected void refresh(Class<?> clazz, List<String> serviceAddressList) {
        cache.refreshServiceAddressList(clazz, serviceAddressList);
    }
}
