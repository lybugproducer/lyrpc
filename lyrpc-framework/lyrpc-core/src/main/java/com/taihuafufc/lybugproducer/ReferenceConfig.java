package com.taihuafufc.lybugproducer;

import lombok.Data;
import lombok.Setter;

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

    @SuppressWarnings("unchecked")
    public T getProxy() {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(null, args);
                    }
                });
    }
}
