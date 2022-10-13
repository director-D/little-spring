package org.springframework.aop.aspect;

import org.springframework.aop.intercept.MethodInterceptor;
import org.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:29
 * @Version: 1.0
 * @Description:
 */
public class AfterThrowingAdvice extends AbstractAspectJAdvice implements Advice, MethodInterceptor {

    private String throwingName;
    private MethodInvocation mi;

    public AfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void setThrowingName(String name) {
        this.throwingName = name;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Exception {
        try {
            return mi.proceed();
        } catch (Throwable ex) {
            super.invokeAdviceMethod(mi, null, ex.getCause());
            throw ex;
        }
    }

}
