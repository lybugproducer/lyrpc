package com.taihuafufc.lybugproducer.trans;

import com.taihuafufc.lybugproducer.handler.LyrpcDatagramHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * LYRPC 请求报文解码器
 *
 * @author lybugproducer
 * @since 2025/2/9 19:12
 */
public class LyrpcRequestDecoder extends LengthFieldBasedFrameDecoder {

    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcRequestDecoder(LyrpcDatagramHandler datagramHandler) {
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

        int payloadLength = LyrpcProtocol.readHeader(frame);

        LyrpcRequest request = LyrpcProtocol.readRequestHeader(frame);

        byte[] payload = new byte[payloadLength];
        frame.readBytes(payload);

        if (payload.length > 0) {
            // 解压缩
            byte[] decompress = datagramHandler.decompress(payload);

            // 反序列化
            LyrpcRequestPayload lyrpcRequestPayload
                    = datagramHandler.deserialize(decompress, LyrpcRequestPayload.class);

            request.setPayload(lyrpcRequestPayload);
        }
        return request;
    }


}
