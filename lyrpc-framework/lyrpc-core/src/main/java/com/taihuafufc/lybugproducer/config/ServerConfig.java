package com.taihuafufc.lybugproducer.config;

import lombok.Data;

/**
 * 服务提供方服务器程序配置类
 *
 * @author lybugproducer
 * @since 2025/3/2 17:39
 */
@Data
public class ServerConfig {
    private String address;

    private int workerThreads;
}
