package com.lyrpc.core.compressor.impl;

import com.lyrpc.core.compressor.LyrpcCompressor;

/**
 * 压缩器实现类 不做任何压缩
 *
 * @author lybugproducer
 * @since 2025/2/16 19:13
 */
public class DoNothingCompressor implements LyrpcCompressor {
    @Override
    public byte[] compress(byte[] data) {
        return data;
    }

    @Override
    public byte[] decompress(byte[] data) {
        return data;
    }
}
