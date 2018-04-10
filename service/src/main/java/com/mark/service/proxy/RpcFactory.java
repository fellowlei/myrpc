package com.mark.service.proxy;

import com.mark.service.util.PropertiesUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcFactory {

    public static <T> T factory(final T rpcService, boolean isCglib, final String beanName) {
        String isProxy = PropertiesUtil.getPropertyValue("mockRpc.isProxy", "false");
        if ("false".equals(isProxy)) {
            return rpcService;
        }
        if (isCglib) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(rpcService.getClass());
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                    return HttpProxy.invokeHttpProxy(beanName,method,args);
                }
            });
            return (T) enhancer.create();
        } else {
            Object o = Proxy.newProxyInstance(RpcFactory.class.getClassLoader(), rpcService.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return HttpProxy.invokeHttpProxy(beanName, method, args);
                }
            });
            return (T) o;
        }
    }
}
