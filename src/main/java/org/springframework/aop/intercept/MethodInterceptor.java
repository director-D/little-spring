package org.springframework.aop.intercept;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午9:58
 * @Version: 1.0
 * @Description:
 */
public interface MethodInterceptor {

    Object invoke(MethodInvocation mi) throws Exception;

}
