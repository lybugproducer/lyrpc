package com.taihuafufc.lybugproducer.enumeration;

import lombok.Getter;

/**
 * 响应状态枚举类
 *
 * @author lybugproducer
 * @since 2025/2/10 09:17
 */
@Getter
public enum LyrpcResponseStatus {
    SUCCESS((byte) 0, "成功"), FAIL((byte) 1, "失败");

    private final byte code;
    private final String message;

    LyrpcResponseStatus(byte code, String message) {
        this.code = code;
        this.message = message;
    }
}
