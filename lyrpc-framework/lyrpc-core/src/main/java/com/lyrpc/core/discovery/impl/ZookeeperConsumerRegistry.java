package com.lyrpc.core.discovery.impl;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.discovery.LyrpcConsumerRegistry;
import com.lyrpc.core.discovery.impl.zk.ZookeeperUtil;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.Set;

/**
 * 服务消费方注册中心 基于 Zookeeper 实现
 *
 * @author lybugproducer
 * @since 2025/3/3 14:32
 */
public class ZookeeperConsumerRegistry extends LyrpcConsumerRegistry {

    private ZooKeeper zookeeper;

    public ZookeeperConsumerRegistry(LyrpcConsumerCache cache, Set<String> addressSet) {
        super(cache, addressSet);
    }

    @Override
    public List<String> discover(Class<? extends Lyrpc> clazz) {
        // 获取接口的全限定名
        String name = clazz.getName();

        // 获取父节点
        String parentNodePath = ZookeeperUtil.ROOT_NODE_PATH + "/" + name;

        // 创建一个 watcher 监听父节点的子节点变化
        Watcher watcher = event -> {
            if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                // 从注册中心获取服务实例列表
                List<String> serviceList = discover(clazz);

                // 通知负载均衡器 刷新对应的缓存
                refresh(clazz, serviceList);
            }
        };

        // 获取服务节点列表
        return ZookeeperUtil.getChildren(zookeeper, parentNodePath, watcher);
    }

    @Override
    public void initRegistry(Set<String> addressSet) {
        String addresses = String.join(",", addressSet);
        this.zookeeper = ZookeeperUtil.createZookeeper(addresses);
    }
}
