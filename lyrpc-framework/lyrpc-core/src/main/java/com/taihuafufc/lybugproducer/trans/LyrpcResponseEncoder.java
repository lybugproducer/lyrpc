package com.taihuafufc.lybugproducer.trans;

import com.taihuafufc.lybugproducer.handler.LyrpcDatagramHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * LYRPC 响应报文编码器
 *
 * @author lybugproducer
 * @since 2025/2/10 09:22
 */
public class LyrpcResponseEncoder extends MessageToByteEncoder<LyrpcResponse> {

    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcResponseEncoder(LyrpcDatagramHandler datagramHandler) {
        this.datagramHandler = datagramHandler;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LyrpcResponse lyrpcResponse, ByteBuf byteBuf) throws Exception {

        // 处理请求报文
        byte[] resultBytes = getResultBytes(lyrpcResponse);

        // 11 bytes for header
        LyrpcProtocol.writeHeader(byteBuf, resultBytes.length);

        // 9 bytes for response header
        LyrpcProtocol.writeResponseHeader(byteBuf, lyrpcResponse);

        // ? bytes for result
        byteBuf.writeBytes(resultBytes);
    }

    private byte[] getResultBytes(LyrpcResponse lyrpcResponse) {
        // 序列化
        byte[] resultBytes = lyrpcResponse.getResultBytes();

        // 压缩
        resultBytes = datagramHandler.compress(resultBytes);

        return resultBytes;
    }
}
