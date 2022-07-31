package org.springframework.beans.factory;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:28
 * @Version: 1.0
 * @Description:
 */
public interface BeanFactory {
    Object getBean(String beanName);
}
