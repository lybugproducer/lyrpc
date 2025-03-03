package com.taihuafufc.lybugproducer.exception;

/**
 * 数据报解析异常
 *
 * @author lybugproducer
 * @since 2025/03/01 17:22
 */
public class DatagramDecodeException extends RuntimeException {
    public DatagramDecodeException(String message) {
        super(message);
    }
}
