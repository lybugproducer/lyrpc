package com.lyrpc.core.trans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LYRPC 请求对象
 *
 * @author lybugproducer
 * @since 2025/2/9 18:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyrpcRequest {

    // 消息类型 1 byte - 普通请求 0 心跳检测 1
    private byte messageType;

    // 请求 ID 8 bytes
    private long requestId;

    // 负载 不定长
    private LyrpcRequestPayload payload;
}
