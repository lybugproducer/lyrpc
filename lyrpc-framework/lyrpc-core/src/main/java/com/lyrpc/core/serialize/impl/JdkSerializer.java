package com.lyrpc.core.serialize.impl;

import com.lyrpc.core.serialize.LyrpcSerializer;

import java.io.*;

/**
 * JDK 序列化器
 *
 * @author lybugproducer
 * @since 2025/2/10 16:07
 */
public class JdkSerializer implements LyrpcSerializer {

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
