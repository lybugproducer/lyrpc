package com.taihuafufc.lybugproducer;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author lybugproducer
 * @since 2025/1/26 11:00
 */
@Data
public class User implements Serializable {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
