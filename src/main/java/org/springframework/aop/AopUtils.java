package org.springframework.aop;

import org.springframework.annotation.annotation.*;
import org.springframework.aop.support.AdvisedSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.mybatis.proxy.MapperProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:38
 * @Version: 1.0
 * @Description:
 */
public class AopUtils {

    /**
     * <p>存储切面配置信息</p>
     */
    public static final List<AdvisedSupport> CONFIGS = new ArrayList<>();

    /**
     * <p>初始化AOP配置类</p>
     */
    public static void instantiationAopConfig(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
            // 如果该类不是切面Aspect则跳过
            if (!clazz.isAnnotationPresent(Aspect.class)) {
                continue;
            }
            AopConfig config = new AopConfig();

            Method[] methods = clazz.getMethods();

            // 设置切点和回调方法
            for (Method method : methods) {
                if (method.isAnnotationPresent(Pointcut.class)) {
                    // 设置切点
                    config.setPointCut(method.getAnnotation(Pointcut.class).value());
                }
                else if (method.isAnnotationPresent(Before.class)) {
                    // 前后方法
                    config.setBefore(method.getName());
                }
                else if (method.isAnnotationPresent(AfterReturning.class)) {
                    // 后置方法
                    config.setAfterReturn(method.getName());
                }
                else if (method.isAnnotationPresent(AfterThrowing.class)) {
                    // 异常方法
                    config.setAfterThrow(method.getName());
                    config.setAfterThrowClass("java.lang.Exception");
                }
            }
            // 没有设置切点，跳过
            if (config.getPointCut() == null) {
                continue;
            }
            config.setAspectClass(beanDefinition.getBeanClassName());
            CONFIGS.add(new AdvisedSupport(config));
        }
    }

    /**
     * <p>创建代理类</p>
     */
    public static AopProxy createProxy(AdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        // 如果实现了接口则使用jdk动态代理，否则使用Cglib代理
        if (targetClass.getInterfaces().length > 0) {
            return new JdkDynamicAopProxy(config);
        }
        // 使用CGLIB代理
        return new CglibAopProxy(config);
    }

    /**
     * <p>判断是否是代理类</p>
     */
    public static boolean isAopProxy(Object object) {
        return object.getClass().getSimpleName().contains("$");
    }

    /**
     * <p>获取被代理的对象</p>
     */
    public static Object getTarget(Object proxy) throws Exception {

        if(proxy.getClass().getSuperclass() == Proxy.class) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else {
            return getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * <p>获取CGLIB被代理的对象</p>
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        if(dynamicAdvisedInterceptor instanceof MapperProxy){
            return null;
        }
        //TODO 这个地方因为 userMapper 赋值给了 config 导致此处取值错误，需要修改
        Field config = dynamicAdvisedInterceptor.getClass().getDeclaredField("config");
        config.setAccessible(true);

        return ((AdvisedSupport) config.get(dynamicAdvisedInterceptor)).getTarget();
    }

    /**
     * <p>获取jdk代理被代理对象</p>
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        Object aopProxy = h.get(proxy);

        Field config = aopProxy.getClass().getDeclaredField("config");
        config.setAccessible(true);

        return ((AdvisedSupport) config.get(aopProxy)).getTarget();
    }


}
