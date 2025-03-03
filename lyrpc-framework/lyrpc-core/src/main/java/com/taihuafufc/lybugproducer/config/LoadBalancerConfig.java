package com.taihuafufc.lybugproducer.config;

import com.taihuafufc.lybugproducer.loadbalancer.LyrpcLoadBalancer;
import lombok.Setter;

/**
 * 负载均衡配置类
 *
 * @author lybugproducer
 * @since 2025/3/3 16:32
 */
@Setter
public class LoadBalancerConfig {

    private Class<? extends LyrpcLoadBalancer> loadBalancerClass;

    public LyrpcLoadBalancer getLoadBalancer() {
        try {
            return loadBalancerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
