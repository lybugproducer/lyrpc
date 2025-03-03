package com.lyrpc.core.discovery;

import com.lyrpc.core.config.ServiceConfig;

import java.util.List;
import java.util.Set;

/**
 * 注册中心接口
 * 注册中心具有的能力 注册服务 发现服务
 *
 * @author lybugproducer
 * @since 2025/2/7 09:41
 */
public interface LyrpcRegistry {

    /**
     * 初始化注册中心
     *
     * @param addressSet 注册中心地址集合
     */
    void initRegistry(Set<String> addressSet);

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
