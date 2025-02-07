package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.protocol.Protocol;
import lombok.Data;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:32
 */
@Data
public class ProtocolConfig {

    private Class<? extends Protocol> protocolClass;

    public Protocol getProtocol() {
        try {
            return protocolClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
