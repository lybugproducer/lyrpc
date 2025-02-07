package com.taihuafufc.lybugproducer.discovery.impl;

import com.taihuafufc.lybugproducer.RegistryConfig;
import com.taihuafufc.lybugproducer.ServiceConfig;
import com.taihuafufc.lybugproducer.discovery.AbstractRegistry;
import com.taihuafufc.lybugproducer.discovery.Registry;
import com.taihuafufc.lybugproducer.zk.ZookeeperNode;
import com.taihuafufc.lybugproducer.zk.ZookeeperUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/7 09:44
 */
public class ZookeeperRegistry implements Registry {

    private final ZooKeeper zookeeper;

    private static final String ROOT_NODE_PATH = "/lyrpc";

    public ZookeeperRegistry(String address) {
        this.zookeeper = ZookeeperUtil.createZookeeper(address, 10000);
    }

    @Override
    public void register(ServiceConfig<?> serviceConfig) {
        // 创建根节点
        ZookeeperNode rootNode = new ZookeeperNode(ROOT_NODE_PATH, null);
        ZookeeperUtil.createNode(zookeeper, rootNode, null, CreateMode.PERSISTENT);

        // 创建服务节点 服务节点是一个永久节点 下面连着一系列的主机节点
        String simpleName = serviceConfig.getInterfaceClass().getSimpleName();
        String parentNodePath = ROOT_NODE_PATH + "/" + simpleName;
        ZookeeperNode parentNode = new ZookeeperNode(parentNodePath, null);
        ZookeeperUtil.createNode(zookeeper, parentNode, null, CreateMode.PERSISTENT);

        // 创建主机节点 主机节点是一个临时节点
        String serviceNodePath = parentNodePath + "/" + serviceConfig.getServiceName();
        ZookeeperNode serviceNode = new ZookeeperNode(serviceNodePath, serviceConfig.getAddress().getBytes());
        ZookeeperUtil.createNode(zookeeper, serviceNode, null, CreateMode.EPHEMERAL);

    }

    @Override
    public String discover(Class<?> clazz) {
        // 获取接口的全限定名
        String simpleName = clazz.getSimpleName();
        // 获取父节点
        String parentNodePath = ROOT_NODE_PATH + "/" + simpleName;
        // 获取服务节点列表
        List<String> children = ZookeeperUtil.getChildren(zookeeper, parentNodePath, null);
        // 随机选择一个服务节点
        String serviceNodePath = parentNodePath + "/" + children.get(0);
        // 获取服务节点数据
        return ZookeeperUtil.getData(zookeeper, serviceNodePath, null, null);
    }
}
