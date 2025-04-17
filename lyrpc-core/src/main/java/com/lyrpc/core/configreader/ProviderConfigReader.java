package com.lyrpc.core.configreader;

import com.lyrpc.core.config.DatagramConfig;
import com.lyrpc.core.config.ProviderRegistryConfig;
import com.lyrpc.core.config.ServerConfig;
import lombok.Getter;

/**
 * 解析服务提供方的配置文件
 *
 * @author lybugproducer
 * @since 2025/3/4 10:27
 */
@Getter
public abstract class ProviderConfigReader implements LyrpcConfigReader {

    // 服务器配置
    ServerConfig serverConfig;

    // 注册中心配置
    ProviderRegistryConfig registryConfig;

    // 数据报协议配置
    DatagramConfig datagramConfig;

    @Override
    public void readConfig(String configPath) throws Exception {
        read(configPath);
        serverConfig = parseServerConfig();
        registryConfig = parseRegistryConfig();
        datagramConfig = parseDatagramConfig();
    }

    protected abstract void read(String configPath) throws Exception;

    protected abstract ServerConfig parseServerConfig();

    protected abstract ProviderRegistryConfig parseRegistryConfig() throws ClassNotFoundException;

    protected abstract DatagramConfig parseDatagramConfig() throws ClassNotFoundException;

}
