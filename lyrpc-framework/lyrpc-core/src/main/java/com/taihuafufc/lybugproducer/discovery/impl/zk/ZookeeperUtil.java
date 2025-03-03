package com.taihuafufc.lybugproducer.discovery.impl.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * zookeeper 工具类
 *
 * @author lybugproducer
 * @since 2025/2/6 16:48
 */
public class ZookeeperUtil {

    public static final String ROOT_NODE_PATH = "/LYRPC";

    public static ZooKeeper createZookeeper(String server, int timeout) {
        try {
            return new ZooKeeper(server, timeout, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createNode(ZooKeeper zooKeeper, ZookeeperNode node, Watcher watcher, CreateMode createMode) {
        try {
            if (zooKeeper.exists(node.getPath(), watcher) == null) {
                zooKeeper.create(node.getPath(), node.getData(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getChildren(ZooKeeper zookeeper, String parentNodePath, Watcher watcher) {
        try {
            return zookeeper.getChildren(parentNodePath, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
