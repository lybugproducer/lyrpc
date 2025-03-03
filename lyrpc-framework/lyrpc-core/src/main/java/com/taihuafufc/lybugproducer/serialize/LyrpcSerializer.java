package com.taihuafufc.lybugproducer.serialize;

/**
 * 序列化器接口
 *
 * @author lybugproducer
 * @since 2025/2/10 16:04
 */
public interface LyrpcSerializer {

    /**
     * 序列化对象
     * @param obj 待序列化对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化对象
     * @param bytes 待反序列化的字节数组
     * @param clazz 待反序列化的类
     * @return 反序列化后的对象
     * @param <T> 待反序列化的类
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
