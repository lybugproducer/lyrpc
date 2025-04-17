package com.lyrpc.core.config;

import com.lyrpc.core.discovery.LyrpcProviderRegistry;
import com.lyrpc.core.discovery.impl.ZookeeperProviderRegistry;
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
public class ProviderRegistryConfig {

    private Class<?> registryClass = ZookeeperProviderRegistry.class;

    private Set<String> addressSet;

    public ProviderRegistryConfig() {
        addressSet = new HashSet<>();
    }

    /**
     * 通过反射创建注册中心实例
     *
     * @return 注册中心实例
     */
    public LyrpcProviderRegistry getRegistry() {
        try {
            Constructor<?> constructor
                    = registryClass.getConstructor(Set.class);
            return (LyrpcProviderRegistry) constructor.newInstance(addressSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAddress(String address) {
        addressSet.add(address);
    }
}
