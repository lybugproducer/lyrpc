package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperRegistry;

public class Main {
    public static void main(String[] args) {
        ReferenceConfig<UserLyrpc> userReferenceConfig = new ReferenceConfig<>();
        userReferenceConfig.setInterfaceClass(UserLyrpc.class);

        ReferenceConfig<ArticleLyrpc> articleReferenceConfig = new ReferenceConfig<>();
        articleReferenceConfig.setInterfaceClass(ArticleLyrpc.class);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegistryClass(ZookeeperRegistry.class);
        registryConfig.setAddress("127.0.0.1:2181");

        LyrpcBootstrap.getInstance()
                .registry(registryConfig)
                .reference(userReferenceConfig)
                .reference(articleReferenceConfig);

        // 获取代理对象
        UserLyrpc proxy = userReferenceConfig.getProxy();
        proxy.getUserById(123456);
    }
}