package com.taihuafufc.lybugproducer.discovery.impl;

import com.taihuafufc.lybugproducer.config.ServiceConfig;
import com.taihuafufc.lybugproducer.discovery.LyrpcRegistry;
import com.taihuafufc.lybugproducer.loadbalancer.LyrpcLoadBalancer;
import com.taihuafufc.lybugproducer.loadbalancer.impl.RoundRobinLoadBalancer;
import com.taihuafufc.lybugproducer.discovery.impl.zk.ZookeeperNode;
import com.taihuafufc.lybugproducer.discovery.impl.zk.ZookeeperUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * 注册中心实现类 使用 zookeeper 作为注册中心
 *
 * @author lybugproducer
 * @since 2025/2/7 09:44
 */
public class ZookeeperRegistry implements LyrpcRegistry {

    private final ZooKeeper zookeeper;

    private final LyrpcLoadBalancer loadBalancer;

    private static final String ROOT_NODE_PATH = "/lyrpc";

    public ZookeeperRegistry(String address) {
        this.zookeeper = ZookeeperUtil.createZookeeper(address, 60000);
        this.loadBalancer = new RoundRobinLoadBalancer();
    }

    @Override
    public void register(ServiceConfig<?> serviceConfig, String address) {

        // 创建根节点 /lyrpc
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

    /**
     * 注册中心应该返回服务地址的列表 而不是单个地址
     * @param clazz 服务接口
     * @return 服务地址列表
     */
    @Override
    public List<String> discover(Class<?> clazz) {
        // 获取接口的全限定名
        String name = clazz.getName();
        // 获取父节点
        String parentNodePath = ROOT_NODE_PATH + "/" + name;
        // 获取服务节点列表
        List<String> children = ZookeeperUtil.getChildren(zookeeper, parentNodePath, null);
//        // 随机选择一个服务节点
//        String serviceNodePath = parentNodePath + "/" + children.get(0);
//        // 获取服务节点数据
//        return ZookeeperUtil.getData(zookeeper, serviceNodePath, null, null);
        return children;
    }

    @Override
    public String select(List<String> discovered) {
        loadBalancer.select(discovered);
        return discovered.get(0);
    }

}
