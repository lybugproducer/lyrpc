package com.taihuafufc.lybugproducer;

import lombok.Data;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:32
 */
@Data
public class ProtocolConfig {

    private final String protocolName;

    public ProtocolConfig(String protocolName) {
        this.protocolName = protocolName;
    }
}
