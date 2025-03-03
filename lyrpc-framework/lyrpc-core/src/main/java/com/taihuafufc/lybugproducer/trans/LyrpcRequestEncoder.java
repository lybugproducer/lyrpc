package com.taihuafufc.lybugproducer.trans;

import com.taihuafufc.lybugproducer.enumeration.LyrpcMessageType;
import com.taihuafufc.lybugproducer.handler.LyrpcDatagramHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * LYRPC 请求报文编码器
 *
 * @author lybugproducer
 * @since 2025/2/9 18:22
 */
@Slf4j
public class LyrpcRequestEncoder
        extends MessageToByteEncoder<LyrpcRequest> {

    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcRequestEncoder(LyrpcDatagramHandler datagramHandler) {
        this.datagramHandler = datagramHandler;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LyrpcRequest lyrpcRequest, ByteBuf byteBuf) throws Exception {

        // 处理请求报文
        byte[] payloadBytes = getPayloadBytes(lyrpcRequest);

        // 11 bytes for header
        LyrpcProtocol.writeHeader(byteBuf, payloadBytes.length);

        // 9 bytes for metadata
        LyrpcProtocol.writeRequestHeader(byteBuf, lyrpcRequest);

        // ? bytes for payload
        byteBuf.writeBytes(payloadBytes);

        System.out.println("encode request: " + lyrpcRequest);
    }

    private byte[] getPayloadBytes(LyrpcRequest request) {
        // 获取请求报文的负载
        LyrpcRequestPayload payload = request.getPayload();

        // 对象怎么变成字节数组？序列化！
        byte[] payloadBytes = datagramHandler.serialize(payload);

        // 序列化之后 还应当进行压缩
        payloadBytes = datagramHandler.compress(payloadBytes);

        log.debug("payloadBytes length: {}", payloadBytes.length);

        return payloadBytes;
    }


}
