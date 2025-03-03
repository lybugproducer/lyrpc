package com.lyrpc.core.discovery;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.config.ServiceConfig;

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
    public abstract void register(ServiceConfig<? extends Lyrpc> config, String address);

    @Override
    public List<String> discover(Class<? extends Lyrpc> clazz) {
        return null;
    }
}
