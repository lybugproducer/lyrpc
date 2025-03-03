package com.lyrpc.core.trans;

import com.lyrpc.core.handler.LyrpcDatagramHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * LYRPC 响应报文解码器
 *
 * @author lybugproducer
 * @since 2025/2/10 09:22
 */
public class LyrpcResponseDecoder extends LengthFieldBasedFrameDecoder {

    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcResponseDecoder(LyrpcDatagramHandler datagramHandler) {
        super(Integer.MAX_VALUE,
                7,
                4,
                -11,
                0);
        this.datagramHandler = datagramHandler;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decode = super.decode(ctx, in);
        if (decode instanceof ByteBuf) {
            return decodeFrame((ByteBuf) decode);
        }
        return null;
    }

    private Object decodeFrame(ByteBuf frame) {

        int resultLength = LyrpcProtocol.readHeader(frame);

        LyrpcResponse response = LyrpcProtocol.readResponseHeader(frame);

        byte[] result = new byte[resultLength];
        frame.readBytes(result);

        if (result.length > 0) {
            // 解压缩
            byte[] decompress = datagramHandler.decompress(result);

            response.setResultBytes(decompress);
        }
        return response;
    }
}
