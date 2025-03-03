package com.lyrpc.core.enumeration;

import lombok.Getter;

/**
 * 消息类型枚举类
 *
 * @author lybugproducer
 * @since 2025/2/10 09:17
 */
@Getter
public enum LyrpcMessageType {

    REQUEST((byte) 1, "request"), HEARTBEAT((byte) 2, "heartbeat");

    private final byte id;
    private final String type;

    LyrpcMessageType(byte id, String type) {
        this.id = id;
        this.type = type;
    }

}
