package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.discovery.Registry;
import com.taihuafufc.lybugproducer.protocol.Protocol;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:18
 */
public class LyrpcBootstrap {

    private static final LyrpcBootstrap INSTANCE = new LyrpcBootstrap();

    private Protocol protocol;

    private Registry registry;


    private LyrpcBootstrap() {
    }

    public static LyrpcBootstrap getInstance() {
        return INSTANCE;
    }


    /**
     * 定义应用注册中心
     *
     * @param registryConfig 注册中心地址
     * @return this
     */
    public LyrpcBootstrap registry(RegistryConfig registryConfig) {
        this.registry = registryConfig.getRegistry();
        return this;
    }

    /**
     * 定义应用协议
     *
     * @param protocolConfig 协议配置
     * @return this
     */
    public LyrpcBootstrap protocol(ProtocolConfig protocolConfig) {
        this.protocol = protocolConfig.getProtocol();
        return this;
    }

    /**
     * 发布服务
     *
     * @param serviceConfig 服务配置
     * @return this
     */
    public LyrpcBootstrap publish(ServiceConfig<?> serviceConfig) {
        // 发布服务
        this.registry.register(serviceConfig);
        return this;
    }

    /**
     * reference
     *
     * @param referenceConfig 引用配置
     * @return this
     */
    public LyrpcBootstrap reference(ReferenceConfig<?> referenceConfig) {
        referenceConfig.setRegistry(registry);
        return this;
    }

    public void start() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
