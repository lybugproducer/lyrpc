package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.discovery.Registry;
import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperRegistry;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:28
 */
@Data
public class RegistryConfig {

    private Class<? extends Registry> registryClass;
    private String address;

    public RegistryConfig() {
    }

    public Registry getRegistry() {
        try {
            Constructor<? extends Registry> constructor
                    = registryClass.getConstructor(String.class);
            return constructor.newInstance(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
