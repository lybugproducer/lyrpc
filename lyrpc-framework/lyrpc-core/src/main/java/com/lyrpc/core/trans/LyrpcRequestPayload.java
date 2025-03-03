package com.lyrpc.core.trans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class LyrpcRequestPayload implements Serializable {

    // 接口的名字
    private String interfaceName;

    // 方法的名字
    private String methodName;

    // 请求参数列表 parameterTypes 和 parameters 配合使用

    // 参数类型确定重载方法
    private Class<?>[] parameterTypes;

    // 具体参数执行方法调用
    private Object[] parameters;

}
