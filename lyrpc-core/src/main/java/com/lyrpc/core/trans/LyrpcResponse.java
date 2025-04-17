package com.lyrpc.core.trans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LYRPC 响应对象
 *
 * @author lybugproducer
 * @since 2025/2/10 07:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyrpcResponse {

    // 请求 ID Long
    private long correspondingRequestId;

    // 响应状态码 Byte 0 成功 1 失败
    private byte statusCode;

    // 响应结果字节数组
    private byte[] resultBytes;
}
