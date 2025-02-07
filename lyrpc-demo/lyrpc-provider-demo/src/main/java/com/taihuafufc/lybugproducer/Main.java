package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperRegistry;
import com.taihuafufc.lybugproducer.impl.UserLyrpcImpl;
import com.taihuafufc.lybugproducer.protocol.impl.LyrpcProtocol;

public class Main {
    public static void main(String[] args) {
        // 封装要发布的服务
        ServiceConfig<UserLyrpc> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterfaceClass(UserLyrpc.class);
        serviceConfig.setReference(new UserLyrpcImpl());
        serviceConfig.setServiceName("lyrpc-user-service");
        serviceConfig.setAddress("127.0.0.1:7000");

        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegistryClass(ZookeeperRegistry.class);
        registryConfig.setAddress("127.0.0.1:2181");

        // 协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setProtocolClass(LyrpcProtocol.class);

        // 服务提供方启动 定义注册中心 定义协议 发布服务
        LyrpcBootstrap.getInstance()
                .registry(registryConfig)
                .protocol(protocolConfig)
                .publish(serviceConfig)
                .start();
    }
}