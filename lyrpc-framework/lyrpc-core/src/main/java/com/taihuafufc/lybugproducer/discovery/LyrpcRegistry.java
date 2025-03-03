package com.taihuafufc.lybugproducer.discovery;

import com.taihuafufc.lybugproducer.config.ServiceConfig;

import java.util.List;

/**
 * 注册中心接口
 * 注册中心具有的能力 注册服务 发现服务
 *
 * @author lybugproducer
 * @since 2025/2/7 09:41
 */
public interface LyrpcRegistry {

    /**
     * 注册服务
     *
     * @param config 服务配置
     */
    void register(ServiceConfig<?> config, String address);

    /**
     * 发现服务
     *
     * @param clazz 服务接口
     * @return 服务地址
     */
    List<String> discover(Class<?> clazz);

}
