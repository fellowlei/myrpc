package com.mark.service;

import com.alibaba.fastjson.JSONObject;
import com.mark.service.net.SocketClient;
import com.mark.service.util.KryoTool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lulei on 2018/5/17.
 */
public class MyProxy implements InvocationHandler{

    public Object target;

    public MyProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("MyProxy.invoke before");
//        Object result = method.invoke(target, args);
        Object result = SocketClient.remoteExecute(target, method, args);
        System.out.println("MyProxy.invoke after");
        return result;
    }

    public Object invokeLocal(Object obj,Method method, Object[] args){
        JSONObject jsonObject = genRemoteObj(target, method, args);
        System.out.println(jsonObject);
        JSONObject resutObject = invokeRemote(jsonObject);
        Object result = parseRemoteResult(resutObject);
        return result;
    }

    public static Object parseRemoteResult(JSONObject jsonObject) {
        try {
            String clazz = jsonObject.getString("clazz");
            String resultJson = jsonObject.getString("result");
            Object result = KryoTool.decode(resultJson, Class.forName(clazz));
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject genRemoteObj(Object obj,Method method, Object[] args){
        JSONObject param = new JSONObject();
        param.put("clazz",obj.getClass().getName());
        param.put("method", method.getName());
        param.put("objs", KryoTool.encode(args));
        return param;
    }

    public static JSONObject invokeRemote(JSONObject jsonObject) {
        try {
            String clazzName = jsonObject.getString("clazz");
            String method = jsonObject.getString("method");
            String params = jsonObject.getString("objs");
            Object[] args = KryoTool.decode(params, Object[].class);
            Class<?> clazz = Class.forName(clazzName);
            Object obj = clazz.newInstance();
            Object result = null;
            for (Method method1 : clazz.getMethods()) {
                if (method1.getName().equals(method)) {
                    result = method1.invoke(obj, args);
                }
            }
            JSONObject resultObject = new JSONObject();
            resultObject.put("code", "0");
            resultObject.put("clazz", result.getClass().getName());
            resultObject.put("result", KryoTool.encode(result));
            return resultObject;
        }catch (Exception e){
            e.printStackTrace();
            JSONObject resultObject = new JSONObject();
            resultObject.put("code", "1");
            return  resultObject;
        }
    }

    public static  <T> T getProxyInstance(T target){
        return (T) Proxy.newProxyInstance(MyProxy.class.getClassLoader(),target.getClass().getInterfaces(),new MyProxy(target));
    }
}
