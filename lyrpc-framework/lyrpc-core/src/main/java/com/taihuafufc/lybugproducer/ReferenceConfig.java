package com.taihuafufc.lybugproducer;

import com.taihuafufc.lybugproducer.discovery.Registry;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:37
 */
@Data
public class ReferenceConfig<T> {

    private Class<T> interfaceClass;

    private Registry registry;

    @SuppressWarnings("unchecked")
    public T getProxy() {

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 服务发现
                String discover = registry.discover(interfaceClass);
                // 远程调用
                System.out.println(method.getName());
                System.out.println(discover);
                return null;
            }
        };

        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{interfaceClass},
                handler);
    }
}
