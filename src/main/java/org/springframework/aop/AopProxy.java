package org.springframework.aop;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午9:56
 * @Version: 1.0
 * @Description:return null;
 */
public interface AopProxy {

    Object getProxy();


    Object getProxy(ClassLoader classLoader);




}
