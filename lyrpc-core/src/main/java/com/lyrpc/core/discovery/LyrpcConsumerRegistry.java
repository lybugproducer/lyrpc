package com.lyrpc.core.discovery;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.config.ServiceConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 服务消费方注册中心抽象类
 *
 * @author lybugproducer
 * @since 2025/3/3 14:28
 */
public abstract class LyrpcConsumerRegistry implements LyrpcRegistry {

    private final LyrpcConsumerCache cache;

    public LyrpcConsumerRegistry(LyrpcConsumerCache cache, Set<String> addressSet) {
        this.cache = cache;
        initRegistry(addressSet);
    }

    /**
     * 发现服务
     *
     * @param interfaceClass 服务接口
     * @return 服务地址
     */
    public abstract List<String> discover(Class<? extends Lyrpc> interfaceClass);

    @Override
    public void register(ServiceConfig<? extends Lyrpc> config, String address) {}

    protected void watch(Class<? extends Lyrpc> interfaceClass) {
        synchronized (interfaceClass) {
            // 从注册中心获取服务实例列表
            List<String> addressList = discover(interfaceClass);

            // 刷新缓存 对比新旧地址列表 处理失效节点和新节点
            refresh(interfaceClass, addressList);
        }
    }

    protected void refresh(Class<? extends Lyrpc> interfaceClass, List<String> serviceAddressList) {
        // 更新服务提供方地址列表缓存
        List<String> previousAddressList = cache
                .refreshProviderAddressList(interfaceClass, serviceAddressList);

        // 对比新旧服务列表 管理连接
        List<String> removed = getRemovedAddressList(previousAddressList, serviceAddressList);
        List<String> added = getAddedAddressList(previousAddressList, serviceAddressList);
        for (String remove : removed) {
            // 移除失效连接
            cache.removeChannel(remove);
        }
        for (String add : added) {
            // 新增连接
            cache.getOrCreateChannel(add);
        }
    }

    private List<String> getRemovedAddressList(List<String> previousAddressList, List<String> serviceAddressList) {
        List<String> removed = new ArrayList<>();
        for (String addr : previousAddressList) {
            if (!serviceAddressList.contains(addr)) {
                removed.add(addr);
            }
        }
        return removed;
    }

    private List<String> getAddedAddressList(List<String> previousAddressList, List<String> serviceAddressList) {
        List<String> added = new ArrayList<>();
        for (String addr : serviceAddressList) {
            if (!previousAddressList.contains(addr)) {
                added.add(addr);
            }
        }
        return added;
    }
}
