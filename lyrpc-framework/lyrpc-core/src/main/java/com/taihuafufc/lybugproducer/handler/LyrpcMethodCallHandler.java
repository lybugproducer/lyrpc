package com.taihuafufc.lybugproducer.handler;

import com.taihuafufc.lybugproducer.config.ServiceConfig;
import com.taihuafufc.lybugproducer.enumeration.LyrpcResponseStatus;
import com.taihuafufc.lybugproducer.trans.LyrpcRequest;
import com.taihuafufc.lybugproducer.trans.LyrpcResponse;
import com.taihuafufc.lybugproducer.trans.LyrpcRequestPayload;
import com.taihuafufc.lybugproducer.trans.LyrpcResponsePayload;
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

    private final Map<String, ServiceConfig<?>> serviceMap;

    private final LyrpcDatagramHandler datagramHandler;

    public LyrpcMethodCallHandler(Map<String, ServiceConfig<?>> serviceMap, LyrpcDatagramHandler datagramHandler) {
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
        Class<?>[] parameterTypes = payload.getParameterTypes();
        Object[] parameters = payload.getParameters();
        String interfaceName = payload.getInterfaceName();

        // 从服务列表中获取服务实现类对象
        ServiceConfig<?> serviceConfig = serviceMap.get(interfaceName);
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
