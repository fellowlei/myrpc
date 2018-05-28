package com.mark.service.impl;

import com.mark.service.UserService;
import com.mark.service.domain.User;

/**
 * Created by lulei on 2018/5/28.
 */
public class MyServiceImpl {
    private UserService userService;

    public User query(String name){
        return userService.query(name);
    }
}
