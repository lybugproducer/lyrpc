package com.taihuafufc.lybugproducer.config;

import com.taihuafufc.lybugproducer.discovery.LyrpcRegistry;
import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperRegistry;
import lombok.Data;

import java.lang.reflect.Constructor;

/**
 * 注册中心配置类
 *
 * @author lybugproducer
 * @since 2025/2/6 11:28
 */
@Data
public class RegistryConfig {

    private Class<? extends LyrpcRegistry> registryClass = ZookeeperRegistry.class;

    private String address;

    /**
     * 通过反射创建注册中心实例
     *
     * @return 注册中心实例
     */
    public LyrpcRegistry getRegistry() {
        try {
            Constructor<? extends LyrpcRegistry> constructor
                    = registryClass.getConstructor(String.class);
            return constructor.newInstance(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
