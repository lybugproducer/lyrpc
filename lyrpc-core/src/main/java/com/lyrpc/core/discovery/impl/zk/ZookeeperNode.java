package com.lyrpc.core.discovery.impl.zk;

import lombok.Data;

/**
 * zookeeper 数据结构中的节点
 *
 * @author lybugproducer
 * @since 2025/2/6 17:11
 */
@Data
public class ZookeeperNode {

    private String path;
    private byte[] data;

    public ZookeeperNode(String path, byte[] data) {
        this.path = path;
        this.data = data;
    }
}
