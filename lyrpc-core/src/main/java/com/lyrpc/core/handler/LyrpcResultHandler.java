package com.lyrpc.core.handler;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.cache.LyrpcConsumerCache;
import com.lyrpc.core.trans.LyrpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务调用方结果处理器
 *
 * @author lybugproducer
 * @since 2025/2/9 17:03
 */
@Slf4j
public class LyrpcResultHandler extends ChannelInboundHandlerAdapter {

    private final LyrpcConsumerCache cache;
    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcResultHandler(LyrpcConsumerCache cache, LyrpcDatagramHandler datagramHandler) {
        this.cache = cache;
        this.datagramHandler = datagramHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LyrpcResponse response = (LyrpcResponse) msg;
        long requestId = response.getCorrespondingRequestId();

        // 解析返回值结果
        byte[] resultBytes = response.getResultBytes();
        Class<?> resultType = cache.getResultType(requestId);

        // 反序列化
        Object result = datagramHandler.deserialize(resultBytes, resultType);

        log.info("Received response for request id: {}, result: {}", requestId, result);

        // 通知主线程 响应已收到
        cache.completeRequest(requestId, result);
    }
}
