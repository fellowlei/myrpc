package com.mark.service.impl;

import com.mark.service.UserService;
import com.mark.service.domain.User;
import com.mark.service.proxy.MyProxy;

/**
 * Created by lulei on 2018/5/28.
 */
public class UserServiceImpl implements UserService {
    @Override
    public boolean insert(User user) {
        System.out.println("UserServiceImpl.insert");
        System.out.println(user);
        return true;
    }

    @Override
    public User query(String name) {
        System.out.println("UserServiceImpl.query");
        return new User("mark",18);
    }

    public static void main(String[] args) throws Exception {
        UserService userService = new UserServiceImpl();

        // rpc call
        UserService proxyInstance = MyProxy.getProxyInstance(userService);
        User user = proxyInstance.query("mark");
        System.out.println(user);
    }
}
