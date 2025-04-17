package com.lyrpc.core.configreader;

import com.lyrpc.core.config.ClientConfig;
import com.lyrpc.core.config.ConsumerRegistryConfig;
import com.lyrpc.core.config.DatagramConfig;
import com.lyrpc.core.config.LoadBalancerConfig;
import lombok.Getter;

/**
 * 解析服务消费方的配置文件
 *
 * @author lybugproducer
 * @since 2025/3/4 10:00
 */
@Getter
public abstract class ConsumerConfigReader implements LyrpcConfigReader {

    // 注册中心配置
    private ConsumerRegistryConfig registryConfig;

    // 负载均衡配置
    private LoadBalancerConfig loadBalancerConfig;

    // 数据报协议配置
    private DatagramConfig datagramConfig;

    // 服务消费方配置
    private ClientConfig clientConfig;

    @Override
    public void readConfig(String configPath) throws Exception {
        read(configPath);
        registryConfig = parseRegistryConfig();
        loadBalancerConfig = parseLoadBalancerConfig();
        datagramConfig = parseDatagramConfig();
        clientConfig = parseClientConfig();
    }

    protected abstract void read(String configPath) throws Exception;

    protected abstract ConsumerRegistryConfig parseRegistryConfig() throws ClassNotFoundException;

    protected abstract LoadBalancerConfig parseLoadBalancerConfig() throws ClassNotFoundException;

    protected abstract DatagramConfig parseDatagramConfig() throws ClassNotFoundException;

    protected abstract ClientConfig parseClientConfig();
}
