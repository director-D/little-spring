package org.springframework.test.service;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Service;
import org.springframework.test.dao.TestDAO;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:59
 * @Version: 1.0
 * @Description:
 */
@Service
public class TestService {

    @Autowired
    TestDAO testDAO;

    public void echo() {
        System.out.println(testDAO.echo());
    }


}
