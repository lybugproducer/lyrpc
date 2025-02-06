package com.taihuafufc.lybugproducer;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:18
 */
public class LyrpcBootstrap {

    private LyrpcBootstrap() {
    }

    private static final LyrpcBootstrap INSTANCE = new LyrpcBootstrap();

    public static LyrpcBootstrap getInstance() {
        return INSTANCE;
    }

    /**
     * 定义应用名称
     * @param appName 应用名称
     * @return this
     */
    public LyrpcBootstrap name(String appName) {
        return this;
    }

    /**
     * 定义应用注册中心
     * @param registryConfig 注册中心地址
     * @return this
     */
    public LyrpcBootstrap registry(RegistryConfig registryConfig) {
        return this;
    }

    /**
     * 定义应用协议
     * @param protocolConfig 协议配置
     * @return this
     */
    public LyrpcBootstrap protocol(ProtocolConfig protocolConfig) {
        return this;
    }

    /**
     * 发布服务
     * @param serviceConfig 服务配置
     * @return this
     */
    public LyrpcBootstrap publish(ServiceConfig<?> serviceConfig) {
        return this;
    }

    /**
     * reference
     * @param referenceConfig 引用配置
     * @return this
     */
    public LyrpcBootstrap reference(ReferenceConfig<?> referenceConfig) {
        return this;
    }

    public void start() {

    }
}
