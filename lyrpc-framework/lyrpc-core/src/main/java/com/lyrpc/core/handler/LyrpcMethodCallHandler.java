package com.lyrpc.core.handler;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.core.config.ServiceConfig;
import com.lyrpc.core.enumeration.LyrpcResponseStatus;
import com.lyrpc.core.trans.LyrpcRequest;
import com.lyrpc.core.trans.LyrpcResponse;
import com.lyrpc.core.trans.LyrpcRequestPayload;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 服务提供方调用处理器
 *
 * @author lybugproducer
 * @since 2025/2/10 07:54
 */
@Slf4j
public class LyrpcMethodCallHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, ServiceConfig<? extends Lyrpc>> serviceMap;

    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcMethodCallHandler(Map<String, ServiceConfig<? extends Lyrpc>> serviceMap, LyrpcDatagramHandler datagramHandler) {
        this.serviceMap = serviceMap;
        this.datagramHandler = datagramHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 获取负载数据
        LyrpcRequest request = (LyrpcRequest) msg;

        log.info("Received request: {}", request);

        LyrpcRequestPayload payload = request.getPayload();

        // 调用方法
        Object result = callMethod(payload);

        // 构造响应数据
        LyrpcResponse response = new LyrpcResponse();
        response.setStatusCode(LyrpcResponseStatus.SUCCESS.getCode());
        response.setCorrespondingRequestId(request.getRequestId());
        response.setResultBytes(datagramHandler.serialize(result));

        // 写出响应数据
        ctx.channel().writeAndFlush(response);
    }

    private Object callMethod(LyrpcRequestPayload payload) {
        // 从请求负载中获取方法名 参数类型 参数值 接口名
        String methodName = payload.getMethodName();
        Class<? extends Lyrpc>[] parameterTypes = payload.getParameterTypes();
        Object[] parameters = payload.getParameters();
        String interfaceName = payload.getInterfaceName();

        // 从服务列表中获取服务实现类对象
        ServiceConfig<? extends Lyrpc> serviceConfig = serviceMap.get(interfaceName);
        Object reference = serviceConfig.getReference();

        // 通过反射调用方法
        try {
            Method method = reference.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(reference, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
