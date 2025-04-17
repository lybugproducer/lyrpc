package com.lyrpc.demo.provider;

import com.lyrpc.core.bootstrap.LyrpcProviderBootstrap;
import com.lyrpc.core.compressor.impl.GzipCompressor;
import com.lyrpc.core.config.DatagramConfig;
import com.lyrpc.core.config.ProviderRegistryConfig;
import com.lyrpc.core.config.ServerConfig;
import com.lyrpc.core.config.ServiceConfig;
import com.lyrpc.core.configreader.ProviderConfigReader;
import com.lyrpc.core.configreader.impl.ProviderXmlConfigReader;
import com.lyrpc.core.discovery.impl.ZookeeperProviderRegistry;
import com.lyrpc.demo.provider.impl.ArticleLyrpcImpl;
import com.lyrpc.demo.provider.impl.UserLyrpcImpl;
import com.lyrpc.demo.api.ArticleLyrpc;
import com.lyrpc.demo.api.UserLyrpc;
import com.lyrpc.core.serializer.impl.JdkSerializer;

public class Provider {
    public static void main(String[] args) {
        // 封装要发布的服务
        ServiceConfig<UserLyrpc> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterfaceClass(UserLyrpc.class);
        serviceConfig.setReference(new UserLyrpcImpl());

        ServiceConfig<ArticleLyrpc> articleServiceConfig = new ServiceConfig<>();
        articleServiceConfig.setInterfaceClass(ArticleLyrpc.class);
        articleServiceConfig.setReference(new ArticleLyrpcImpl());

//        // 服务器配置
//        ServerConfig serverConfig = new ServerConfig();
//        serverConfig.setAddress("127.0.0.1:23456");
//        serverConfig.setWorkerThreads(10);
//
//        // 注册中心配置
//        ProviderRegistryConfig registryConfig = new ProviderRegistryConfig();
//        registryConfig.setRegistryClass(ZookeeperProviderRegistry.class);
//        registryConfig.setAddress("127.0.0.1:2181");
//
//        // 数据报协议配置
//        DatagramConfig datagramConfig = new DatagramConfig();
//        datagramConfig.setCompressorClass(GzipCompressor.class);
//        datagramConfig.setSerializerClass(JdkSerializer.class);

        ProviderConfigReader reader = new ProviderXmlConfigReader();
        try {
            reader.readConfig("/lyrpc-provider.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ProviderRegistryConfig registryConfig = reader.getRegistryConfig();
        DatagramConfig datagramConfig = reader.getDatagramConfig();
        ServerConfig serverConfig = reader.getServerConfig();

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