package com.taihuafufc.lybugproducer.discovery;

import com.taihuafufc.lybugproducer.ServiceConfig;

/**
 * TODO
 * 注册中心具有的能力 注册服务 发现服务
 *
 * @author lybugproducer
 * @since 2025/2/7 09:41
 */
public interface Registry {

    /**
     * 注册服务
     *
     * @param config 服务配置
     */
    void register(ServiceConfig<?> config);

    /**
     * 发现服务
     *
     * @param clazz 服务接口
     * @return 服务地址
     */
    String discover(Class<?> clazz);

}
