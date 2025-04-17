package com.lyrpc.core.enumeration;

import lombok.Getter;

/**
 * 响应状态枚举类
 *
 * @author lybugproducer
 * @since 2025/2/10 09:17
 */
@Getter
public enum LyrpcResponseStatus {
    SUCCESS((byte) 0, "SUCCESS"), FAIL((byte) 1, "FAIL");

    private final byte code;
    private final String message;

    LyrpcResponseStatus(byte code, String message) {
        this.code = code;
        this.message = message;
    }
}
