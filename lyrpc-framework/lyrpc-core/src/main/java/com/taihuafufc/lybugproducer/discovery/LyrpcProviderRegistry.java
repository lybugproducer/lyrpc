package com.taihuafufc.lybugproducer.discovery;

import com.taihuafufc.lybugproducer.config.ServiceConfig;

import java.util.List;
import java.util.Set;

/**
 * 服务提供方注册中心抽象类
 *
 * @author lybugproducer
 * @since 2025/3/3 14:35
 */
public abstract class LyrpcProviderRegistry implements LyrpcRegistry {

    public LyrpcProviderRegistry(Set<String> addressSet) {
        initRegistry(addressSet);
    }

    /**
     * 注册服务
     *
     * @param config 服务配置
     */
    public abstract void register(ServiceConfig<?> config, String address);

    @Override
    public List<String> discover(Class<?> clazz) {
        return null;
    }

    protected abstract void initRegistry(Set<String> addressSet);
}
