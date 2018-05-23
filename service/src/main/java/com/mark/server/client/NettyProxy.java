package com.mark.server.client;

import com.mark.server.SocketClient;
import com.mark.server.domain.RpcRequest;
import com.mark.service.MyProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by lulei on 2018/5/22.
 */
public class NettyProxy implements InvocationHandler {
    public Object target;

    public NettyProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("MyProxy.invoke before");
//        Object result = method.invoke(target, args);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(target.getClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setRequestId(UUID.randomUUID().toString());

        NettyClient nettyClient = new NettyClient();
        nettyClient.start(rpcRequest);
//        Object result = SocketClient.remoteExecute(target, method, args);
        System.out.println("MyProxy.invoke after");
        // TODO add netty client response
        return null;
    }

    public static  <T> T getProxyInstance(T target){
        return (T) Proxy.newProxyInstance(NettyProxy.class.getClassLoader(),target.getClass().getInterfaces(),new NettyProxy(target));
    }
}
