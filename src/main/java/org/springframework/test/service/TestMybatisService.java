package org.springframework.test.service;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Service;
import org.springframework.test.entity.User;
import org.springframework.test.mapper.UserMapper;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 16:56
 * @Version: 1.0
 * @Description:
 */
@Service
public class TestMybatisService {

    @Autowired
    private UserMapper userMapper;

    public int insert(String username, String password) {
        return userMapper.insert(username, password);
    }

    public int delete(int id) {
        return userMapper.delete(id);
    }

    public int update(int id, String password) {
        return userMapper.update(id, password);
    }

    public User select(int id) {
        return userMapper.select(id);
    }



}
