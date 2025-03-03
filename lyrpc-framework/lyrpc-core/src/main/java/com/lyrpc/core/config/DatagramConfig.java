package com.lyrpc.core.config;

import com.lyrpc.core.compress.LyrpcCompressor;
import com.lyrpc.core.compress.impl.DoNothingCompressor;
import com.lyrpc.core.serialize.LyrpcSerializer;
import com.lyrpc.core.serialize.impl.JdkSerializer;
import com.lyrpc.core.handler.LyrpcDatagramHandler;
import lombok.Data;

/**
 * 数据报配置类
 *
 * @author lybugproducer
 * @since 2025/2/16 16:56
 */
@Data
public class DatagramConfig {

    private Class<? extends LyrpcCompressor> compressorClass = DoNothingCompressor.class;

    private Class<? extends LyrpcSerializer> serializerClass = JdkSerializer.class;

    /**
     * 通过反射创建 DatagramHandler 实例
     *
     * @return DatagramHandler 实例
     */
    public LyrpcDatagramHandler getDatagramHandler() {
        LyrpcCompressor compressor;
        LyrpcSerializer serializer;
        try {
            compressor = compressorClass.getDeclaredConstructor().newInstance();
            serializer = serializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new LyrpcDatagramHandler(compressor, serializer);
    }
}
