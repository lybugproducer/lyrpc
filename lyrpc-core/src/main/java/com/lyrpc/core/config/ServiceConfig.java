package com.lyrpc.core.config;

import lombok.Data;

/**
 * 服务配置类
 *
 * @author lybugproducer
 * @since 2025/2/6 11:35
 */
@Data
public class ServiceConfig<T> {

    private Class<T> interfaceClass;

    private T reference;

    private String serviceName;
}
