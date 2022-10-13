package org.springframework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.aop.intercept.MethodInvocation;
import org.springframework.aop.support.AdvisedSupport;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:35
 * @Version: 1.0
 * @Description:
 */
public class CglibAopProxy implements AopProxy, MethodInterceptor {
    private Class targetClass;

    private Object target;

    private AdvisedSupport config;

    public CglibAopProxy(AdvisedSupport config) {
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
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.targetClass);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Exception {
        List<Object> interceptorsAndDynamicMethodMatchers =
                this.config.getInterceptorAndDynamicInterceptionAdvice(method, this.targetClass);
        MethodInvocation invocation =
                new MethodInvocation(proxy, method, this.target,
                        this.targetClass, args, interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }



}
