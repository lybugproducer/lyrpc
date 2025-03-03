package com.taihuafufc.lybugproducer.config;

import com.taihuafufc.lybugproducer.cache.LyrpcConsumerCache;
import com.taihuafufc.lybugproducer.discovery.LyrpcConsumerRegistry;
import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperConsumerRegistry;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 注册中心配置类
 *
 * @author lybugproducer
 * @since 2025/2/6 11:28
 */
@Data
public class ConsumerRegistryConfig {

    private Class<? extends LyrpcConsumerRegistry> registryClass = ZookeeperConsumerRegistry.class;

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
            Constructor<? extends LyrpcConsumerRegistry> constructor
                    = registryClass.getConstructor(Set.class, LyrpcConsumerCache.class);
            return constructor.newInstance(addressSet, cache);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAddress(String address) {
        addressSet.add(address);
    }
}
