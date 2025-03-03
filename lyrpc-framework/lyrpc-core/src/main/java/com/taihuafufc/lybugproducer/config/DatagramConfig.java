package com.taihuafufc.lybugproducer.config;

import com.taihuafufc.lybugproducer.compress.LyrpcCompressor;
import com.taihuafufc.lybugproducer.compress.impl.DoNothingCompressor;
import com.taihuafufc.lybugproducer.serialize.LyrpcSerializer;
import com.taihuafufc.lybugproducer.serialize.impl.JdkSerializer;
import com.taihuafufc.lybugproducer.handler.LyrpcDatagramHandler;
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
