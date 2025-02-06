package com.taihuafufc.lybugproducer;

import lombok.Data;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/1/26 11:00
 */
@Data
public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
