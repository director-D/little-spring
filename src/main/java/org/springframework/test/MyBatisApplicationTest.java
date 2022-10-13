package org.springframework.test;

import org.springframework.mybatis.config.Configuration;
import org.springframework.mybatis.proxy.MapperProxy;
import org.springframework.mybatis.utils.JDBCUtils;
import org.springframework.test.mapper.UserMapper;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 16:14
 * @Version: 1.0
 * @Description:
 */
public class MyBatisApplicationTest {
    public static void main(String[] args) {
        // 测试MyBatis功能（单独使用）
        JDBCUtils.init(new Configuration("application.properties"));
        UserMapper userMapper = (UserMapper) new MapperProxy(UserMapper.class).getProxy();
        System.out.println(userMapper.select(1));

    }

}
