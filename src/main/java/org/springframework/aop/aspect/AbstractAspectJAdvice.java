package org.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:20
 * @Version: 1.0
 * @Description:
 */
public abstract class AbstractAspectJAdvice {

    private Method aspectMethod;
    private Object aspectTarget;


    public AbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }


    protected Object invokeAdviceMethod(JoinPoint joinPoint,Object returnValue,Throwable ex) throws Exception{

        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if(parameterTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if(parameterTypes[i]==JoinPoint.class){
                args[i] = joinPoint;
            }

            else if (parameterTypes[i] == Throwable.class) {
                args[i] = ex;
            }

            else if (parameterTypes[i] == Object.class) {
                args[i] = returnValue;
            }

        }
        return this.aspectMethod.invoke(aspectTarget, args);
    }


    public Method getAspectMethod() {
        return aspectMethod;
    }

    public void setAspectMethod(Method aspectMethod) {
        this.aspectMethod = aspectMethod;
    }

    public Object getAspectTarget() {
        return aspectTarget;
    }

    public void setAspectTarget(Object aspectTarget) {
        this.aspectTarget = aspectTarget;
    }


}
