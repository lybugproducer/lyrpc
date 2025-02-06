package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.impl.UserLyrpcImpl;

public class Main {
    public static void main(String[] args) {
        // 封装要发布的服务
        ServiceConfig<UserLyrpc> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterfaceClass(UserLyrpc.class);
        serviceConfig.setReference(new UserLyrpcImpl());

        // 服务提供方 启动 ①服务名称 ②定义注册中心 ③发布服务
        LyrpcBootstrap.getInstance()
                .name("lyrpc-provider")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .protocol(new ProtocolConfig("lyrpc"))
                .publish(serviceConfig)
                .start();
    }
}