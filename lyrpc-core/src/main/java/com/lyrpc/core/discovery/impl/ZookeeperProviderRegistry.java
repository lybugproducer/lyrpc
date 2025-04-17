package com.lyrpc.core.discovery.impl;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.config.ServiceConfig;
import com.lyrpc.core.discovery.LyrpcProviderRegistry;
import com.lyrpc.core.discovery.impl.zk.ZookeeperNode;
import com.lyrpc.core.discovery.impl.zk.ZookeeperUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.Set;

import static com.lyrpc.core.discovery.impl.zk.ZookeeperUtil.ROOT_NODE_PATH;

/**
 * 服务提供方注册中心 zookeeper 实现
 *
 * @author lybugproducer
 * @since 2025/3/3 17:13
 */
public class ZookeeperProviderRegistry extends LyrpcProviderRegistry {

    private ZooKeeper zookeeper;

    public ZookeeperProviderRegistry(Set<String> addressSet) {
        super(addressSet);
    }

    @Override
    public void register(ServiceConfig<? extends Lyrpc> serviceConfig, String address) {

        // 创建根节点
        ZookeeperNode rootNode = new ZookeeperNode(ROOT_NODE_PATH, null);
        ZookeeperUtil.createNode(zookeeper, rootNode, null, CreateMode.PERSISTENT);

        // 创建服务节点 服务节点是一个永久节点 下面连着一系列的主机节点 /lyrpc/接口名
        String interfaceName = serviceConfig.getInterfaceClass().getName();
        String parentNodePath = ROOT_NODE_PATH + "/" + interfaceName;
        ZookeeperNode parentNode = new ZookeeperNode(parentNodePath, null);
        ZookeeperUtil.createNode(zookeeper, parentNode, null, CreateMode.PERSISTENT);

        // 创建主机节点 主机节点是一个临时节点 /lyrpc/接口名/主机地址
        String serviceNodePath = parentNodePath + "/" + address;
        ZookeeperNode serviceNode = new ZookeeperNode(serviceNodePath, null);
        ZookeeperUtil.createNode(zookeeper, serviceNode, null, CreateMode.EPHEMERAL);

    }

    @Override
    public void initRegistry(Set<String> addressSet) {
        String addresses = String.join(",", addressSet);
        this.zookeeper = ZookeeperUtil.createZookeeper(addresses);
    }

}
