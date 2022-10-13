package org.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午9:55
 * @Version: 1.0
 * @Description:
 */
public interface JoinPoint {


    /**
     * <p>业务方法本身</p>
     */
    Method getMethod();

    /**
     * <p>该方法的参数列表</p>
     */
    Object[] getArguments();

    /**
     * <p>该方法对应的对象</p>
     */
    Object getThis();

    /**
     * <p>在joinPoint中添加自定义属性</p>
     */
    void setUserAttribute(String key, Object value);

    /**
     * <p>获取自定义属性</p>
     */
    Object getUserAttribute(String key);






}
