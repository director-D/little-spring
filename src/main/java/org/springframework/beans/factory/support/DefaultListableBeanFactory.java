package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:24
 * @Version: 1.0
 * @Description:
 */
public class DefaultListableBeanFactory implements BeanFactory {

    protected final Map<String, BeanDefinition> beanDefinitionMap =
            new ConcurrentHashMap<>(32);

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
