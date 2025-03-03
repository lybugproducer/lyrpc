package com.taihuafufc.lybugproducer.loadbalancer;

import java.util.List;

/**
 * 负载均衡器接口
 *
 * @author lybugproducer
 * @since 2025/2/10 17:07
 */
public interface LyrpcLoadBalancer {

    /**
     * 选择一个服务实例
     * @param serviceList 服务实例列表
     * @return 选择的服务实例
     */
    String select(List<String> serviceList);

}
