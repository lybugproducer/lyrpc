package com.taihuafufc.lybugproducer;

import lombok.Data;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:28
 */
@Data
public class RegistryConfig {

    private final String url;

    public RegistryConfig(String url) {
        this.url = url;
    }
}
