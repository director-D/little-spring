package org.springframework.aop;

import org.springframework.aop.intercept.MethodInvocation;
import org.springframework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:33
 * @Version: 1.0
 * @Description:
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private Class targetClass;

    private Object target;

    private AdvisedSupport config;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        this.targetClass = this.config.getTargetClass();
        this.target = this.config.getTarget();
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        List<Object> interceptorsAndDynamicMethodMatchers =
                this.config.getInterceptorAndDynamicInterceptionAdvice(method, this.targetClass);
        MethodInvocation invocation =
                new MethodInvocation(proxy, method, this.target,
                        this.targetClass, args, interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }

}
