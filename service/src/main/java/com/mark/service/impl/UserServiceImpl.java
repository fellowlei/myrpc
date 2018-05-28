package com.mark.service.impl;

import com.mark.service.UserService;
import com.mark.service.domain.User;

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
}
