package com.lyrpc.core.config;

import lombok.Data;

/**
 * 服务消费方服务器配置
 *
 * @author lybugproducer
 * @since 2025/3/3 09:15
 */
@Data
public class ClientConfig {
    private long dataCenterId;
    private long workerId;
}
