package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.bootstrap.LyrpcProviderBootstrap;
import com.taihuafufc.lybugproducer.compress.impl.GZIPCompressor;
import com.taihuafufc.lybugproducer.config.DatagramConfig;
import com.taihuafufc.lybugproducer.config.ProviderRegistryConfig;
import com.taihuafufc.lybugproducer.config.ServerConfig;
import com.taihuafufc.lybugproducer.config.ServiceConfig;
import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperProviderRegistry;
import com.taihuafufc.lybugproducer.impl.ArticleLyrpcImpl;
import com.taihuafufc.lybugproducer.impl.UserLyrpcImpl;
import com.taihuafufc.lybugproducer.serialize.impl.JdkSerializer;

public class Provider {
    public static void main(String[] args) {
        // 封装要发布的服务
        ServiceConfig<UserLyrpc> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterfaceClass(UserLyrpc.class);
        serviceConfig.setReference(new UserLyrpcImpl());

        ServiceConfig<ArticleLyrpc> articleServiceConfig = new ServiceConfig<>();
        articleServiceConfig.setInterfaceClass(ArticleLyrpc.class);
        articleServiceConfig.setReference(new ArticleLyrpcImpl());

        // 服务器配置
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setAddress("127.0.0.1:23456");
        serverConfig.setWorkerThreads(10);

        // 注册中心配置
        ProviderRegistryConfig registryConfig = new ProviderRegistryConfig();
        registryConfig.setRegistryClass(ZookeeperProviderRegistry.class);
        registryConfig.setAddress("127.0.0.1:2181");

        // 数据报协议配置
        DatagramConfig datagramConfig = new DatagramConfig();
        datagramConfig.setCompressorClass(GZIPCompressor.class);
        datagramConfig.setSerializerClass(JdkSerializer.class);

        // 服务提供方启动 定义注册中心 定义协议 发布服务
        LyrpcProviderBootstrap.getInstance()
                .registry(registryConfig)
                .datagram(datagramConfig)
                .server(serverConfig)
                .publish(serviceConfig)
                .publish(articleServiceConfig)
                .start();
    }
}