package com.lyrpc.core.handler;

import com.lyrpc.core.compress.LyrpcCompressor;
import com.lyrpc.core.serialize.LyrpcSerializer;
import lombok.Data;

/**
 * 数据报处理器
 *
 * @author lybugproducer
 * @since 2025/2/16 17:05
 */
@Data
public class LyrpcDatagramHandler {

    private final LyrpcCompressor compressor;

    private final LyrpcSerializer serializer;

    public LyrpcDatagramHandler(LyrpcCompressor compressor, LyrpcSerializer serializer) {
        this.compressor = compressor;
        this.serializer = serializer;
    }

    public byte[] serialize(Object object) {
        return serializer.serialize(object);
    }

    public byte[] compress(byte[] data) {
        return compressor.compress(data);
    }

    public byte[] decompress(byte[] data) {
        return compressor.decompress(data);
    }

    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return serializer.deserialize(data, clazz);
    }
}
