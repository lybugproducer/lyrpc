package com.lyrpc.core.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.lyrpc.core.serializer.LyrpcSerializer;

/**
 * Json 序列化器
 *
 * @author lybugproducer
 * @since 2025/3/2 14:25
 */
public class JsonSerializer implements LyrpcSerializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
