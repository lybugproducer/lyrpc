package com.taihuafufc.lybugproducer.compress;

/**
 * 压缩器接口
 *
 * @author lybugproducer
 * @since 2025/2/16 17:00
 */
public interface LyrpcCompressor {

    /**
     * 压缩数据
     *
     * @param data 原始数据
     * @return 压缩后的数据
     */
    byte[] compress(byte[] data);

    /**
     * 解压数据
     *
     * @param data 压缩后的数据
     * @return 解压后的数据
     */
    byte[] decompress(byte[] data);
}
