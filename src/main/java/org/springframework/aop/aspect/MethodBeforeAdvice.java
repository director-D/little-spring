package org.springframework.aop.aspect;

import org.springframework.aop.intercept.MethodInterceptor;
import org.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:25
 * @Version: 1.0
 * @Description:
 */
public class MethodBeforeAdvice extends AbstractAspectJAdvice implements Advice, MethodInterceptor {

    public MethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Exception {
        super.invokeAdviceMethod(mi, null, null);
        return mi.proceed();
    }
}
