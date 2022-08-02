package org.springframework.test.dao;

import org.springframework.annotation.Repository;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:58
 * @Version: 1.0
 * @Description:
 */
@Repository
public class TestDAO {

    public String echo() {
        return "This is TestDAO#echo!!!";
    }

}
