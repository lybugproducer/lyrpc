package com.taihuafufc.lybugproducer.discovery;

import com.taihuafufc.lybugproducer.ServiceConfig;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/7 09:43
 */
public abstract class AbstractRegistry implements Registry {

    protected String address;

    public AbstractRegistry(String address) {
        this.address = address;
    }
}
