package com.taihuafufc.lybugproducer.zk;

import lombok.Data;

/**
 * TODO
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
