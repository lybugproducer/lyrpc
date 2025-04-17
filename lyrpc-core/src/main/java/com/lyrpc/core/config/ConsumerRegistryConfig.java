package com.lyrpc.core.config;

import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.discovery.LyrpcConsumerRegistry;
import com.lyrpc.core.discovery.impl.ZookeeperConsumerRegistry;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * 注册中心配置类
 *
 * @author lybugproducer
 * @since 2025/2/6 11:28
 */
@Data
public class ConsumerRegistryConfig {

    private Class<?> registryClass = ZookeeperConsumerRegistry.class;

    private Set<String> addressSet;

    public ConsumerRegistryConfig() {
        addressSet = new HashSet<>();
    }

    /**
     * 通过反射创建注册中心实例
     *
     * @return 注册中心实例
     */
    public LyrpcConsumerRegistry getRegistry(LyrpcConsumerCache cache) {
        try {
            Constructor<?> constructor
                    = registryClass.getConstructor(LyrpcConsumerCache.class, Set.class);
            return (LyrpcConsumerRegistry) constructor.newInstance(cache, addressSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAddress(String address) {
        addressSet.add(address);
    }
}
