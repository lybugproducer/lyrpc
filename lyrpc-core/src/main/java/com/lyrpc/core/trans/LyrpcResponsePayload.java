package com.lyrpc.core.trans;

import com.lyrpc.core.Lyrpc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LYRPC 请求负载
 *
 * @author lybugproducer
 * @since 2025/2/9 18:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyrpcResponsePayload {

    // 返回值类型
    private Class<? extends Lyrpc> returnType;

    // 返回值
    private Object result;

}
