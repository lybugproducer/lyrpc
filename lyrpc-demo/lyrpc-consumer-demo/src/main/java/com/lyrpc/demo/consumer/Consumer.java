package com.lyrpc.demo.consumer;

import com.lyrpc.core.bootstrap.LyrpcConsumerBootstrap;
import com.lyrpc.core.compress.impl.GZIPCompressor;
import com.lyrpc.core.config.*;
import com.lyrpc.core.discovery.impl.ZookeeperConsumerRegistry;
import com.lyrpc.demo.entity.Article;
import com.lyrpc.demo.entity.User;
import com.lyrpc.core.loadbalancer.impl.RoundRobinLoadBalancer;
import com.lyrpc.demo.api.ArticleLyrpc;
import com.lyrpc.demo.api.UserLyrpc;
import com.lyrpc.core.serialize.impl.JdkSerializer;

public class Consumer {
    public static void main(String[] args) {
        // 定义服务接口 UserLyrpc
        ReferenceConfig<UserLyrpc> userReferenceConfig = new ReferenceConfig<>();
        userReferenceConfig.setInterfaceClass(UserLyrpc.class);

        // 定义服务接口 ArticleLyrpc
        ReferenceConfig<ArticleLyrpc> articleReferenceConfig = new ReferenceConfig<>();
        articleReferenceConfig.setInterfaceClass(ArticleLyrpc.class);

        // 定义注册中心配置
        ConsumerRegistryConfig registryConfig = new ConsumerRegistryConfig();
        registryConfig.setRegistryClass(ZookeeperConsumerRegistry.class);
        registryConfig.setAddress("127.0.0.1:2181");

        // 定义负载均衡配置
        LoadBalancerConfig loadBalancerConfig = new LoadBalancerConfig();
        loadBalancerConfig.setLoadBalancerClass(RoundRobinLoadBalancer.class);

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
                .loadBalancer(loadBalancerConfig)
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