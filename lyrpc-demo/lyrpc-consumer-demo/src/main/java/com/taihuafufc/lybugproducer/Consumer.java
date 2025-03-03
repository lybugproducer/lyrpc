package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.bootstrap.LyrpcConsumerBootstrap;
import com.taihuafufc.lybugproducer.compress.impl.DoNothingCompressor;
import com.taihuafufc.lybugproducer.compress.impl.GZIPCompressor;
import com.taihuafufc.lybugproducer.config.ClientConfig;
import com.taihuafufc.lybugproducer.config.DatagramConfig;
import com.taihuafufc.lybugproducer.config.ReferenceConfig;
import com.taihuafufc.lybugproducer.config.RegistryConfig;
import com.taihuafufc.lybugproducer.discovery.impl.ZookeeperRegistry;
import com.taihuafufc.lybugproducer.serialize.impl.JdkSerializer;
import com.taihuafufc.lybugproducer.serialize.impl.JsonSerializer;

public class Consumer {
    public static void main(String[] args) {
        // 定义服务接口 UserLyrpc
        ReferenceConfig<UserLyrpc> userReferenceConfig = new ReferenceConfig<>();
        userReferenceConfig.setInterfaceClass(UserLyrpc.class);

        // 定义服务接口 ArticleLyrpc
        ReferenceConfig<ArticleLyrpc> articleReferenceConfig = new ReferenceConfig<>();
        articleReferenceConfig.setInterfaceClass(ArticleLyrpc.class);

        // 定义注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegistryClass(ZookeeperRegistry.class);
        registryConfig.setAddress("127.0.0.1:2181");

        // 定义数据报协议配置
        DatagramConfig datagramConfig = new DatagramConfig();
        datagramConfig.setSerializerClass(JdkSerializer.class);
        datagramConfig.setCompressorClass(GZIPCompressor.class);

        // 定义服务消费方服务器配置
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setDataCenterId(1L);
        clientConfig.setWorkerId(1L);

        // 启动消费者
        LyrpcConsumerBootstrap.getInstance()
                .registry(registryConfig)
                .datagram(datagramConfig)
                .client(clientConfig)
                .reference(userReferenceConfig)
                .reference(articleReferenceConfig);

        // 获取代理对象
        UserLyrpc proxy = userReferenceConfig.getProxy();
        User userById = proxy.getUserById(123456);
        System.out.println("userById = " + userById);

        ArticleLyrpc articleProxy = articleReferenceConfig.getProxy();
        Article articleById = articleProxy.getArticleById(123456);
        System.out.println("articleById = " + articleById);
    }
}