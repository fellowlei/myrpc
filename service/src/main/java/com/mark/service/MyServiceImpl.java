package com.mark.service;

import com.mark.service.proxy.MyProxy;

/**
 * Created by lulei on 2018/5/17.
 */
public class MyServiceImpl implements MyService{
    @Override
    public String service(String name) {
        System.out.println("MyServiceImpl.service");
        return "MyServiceImpl.service:" + name;
    }

    public static void main(String[] args) throws Exception {
        MyService myService = new MyServiceImpl();
//
        MyService proxyInstance = MyProxy.getProxyInstance(myService);
        String result = proxyInstance.service("mark");
        System.out.println(result);
//        System.out.println(MyServiceImpl.class.getName());
    }
}
