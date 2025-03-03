package com.taihuafufc.lybugproducer.trans;

import com.taihuafufc.lybugproducer.exception.DatagramDecodeException;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * LYRPC 协议
 *
 * @author lybugproducer
 * @since 2025/2/16 15:57
 */
@Slf4j
public class LyrpcProtocol {

    private static final String MAGIC_NUMBER = "LYRPC";

    /**
     * 编码头部信息
     *
     * @param byteBuf 待编码的缓冲区
     * @param length  待编码的长度
     */
    public static void writeHeader(ByteBuf byteBuf, int length) {
        // 5 bytes for magic number
        byteBuf.writeBytes("LYRPC".getBytes(StandardCharsets.UTF_8));

        // 1 byte for version
        byteBuf.writeByte(1);

        // 1 byte for header length
        byteBuf.writeByte(20);

        // 4 bytes for length
        byteBuf.writeInt(20 + length);
    }

    /**
     * 编码请求头部信息
     *
     * @param byteBuf 待编码的缓冲区
     * @param request 请求对象
     */
    public static void writeRequestHeader(ByteBuf byteBuf, LyrpcRequest request) {

        // 1 byte for message type
        byteBuf.writeByte(request.getMessageType());

        // 8 bytes for request id
        byteBuf.writeLong(request.getRequestId());
    }

    /**
     * 编码响应头部信息
     *
     * @param byteBuf  待编码的缓冲区
     * @param response 请求对象
     */
    public static void writeResponseHeader(ByteBuf byteBuf, LyrpcResponse response) {
        // 1 byte for message type
        byteBuf.writeByte(response.getStatusCode());

        // 8 bytes for corresponding request id
        byteBuf.writeLong(response.getCorrespondingRequestId());
    }

    /**
     * 解码头部信息
     *
     * @param frame 待解码的缓冲区
     * @return 解码后的长度
     */
    public static int readHeader(ByteBuf frame) {
        // 5 bytes for magic number
        byte[] magic = new byte[5];
        frame.readBytes(magic);
        if (!MAGIC_NUMBER.equals(new String(magic, StandardCharsets.UTF_8))) {
            log.error("Invalid magic number: {}", new String(magic, StandardCharsets.UTF_8));
            throw new DatagramDecodeException("Invalid magic number");
        }

        // 1 byte for version
        byte version = frame.readByte();
        if (version > 1) {
            log.error("Invalid version: {}", version);
            throw new DatagramDecodeException("Unsupported version: " + version);
        }

        // 1 byte for header length
        short headLength = frame.readByte();

        // 4 bytes for length
        int fullLength = frame.readInt();

        return fullLength - headLength;
    }

    /**
     * 解码请求头部信息
     * @param frame 待解码的缓冲区
     * @return 请求对象
     */
    public static LyrpcRequest readRequestHeader(ByteBuf frame) {
        // 1 byte for message type
        byte messageType = frame.readByte();

        // 8 bytes for request id
        long requestId = frame.readLong();

        // 封装请求对象
        return LyrpcRequest.builder()
                .messageType(messageType)
                .requestId(requestId)
                .build();
    }

    /**
     * 解码响应头部信息
     * @param frame 待解码的缓冲区
     * @return 响应对象
     */
    public static LyrpcResponse readResponseHeader(ByteBuf frame) {
        // 1 byte for message type
        byte statusCode = frame.readByte();

        // 8 bytes for corresponding request id
        long correspondingRequestId = frame.readLong();

        // 封装响应对象
        return LyrpcResponse.builder()
                .statusCode(statusCode)
                .correspondingRequestId(correspondingRequestId)
                .build();
    }
}
