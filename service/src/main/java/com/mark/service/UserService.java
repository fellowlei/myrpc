package com.mark.service;

import com.mark.service.domain.User;

/**
 * Created by lulei on 2018/5/28.
 */
public interface UserService {
    public boolean insert(User user);
    public User query(String name);
}
