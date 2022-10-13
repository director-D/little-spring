package org.springframework.test.aspect;

import org.springframework.annotation.Component;
import org.springframework.annotation.annotation.*;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午10:46
 * @Version: 1.0
 * @Description:
 */
@Aspect
@Component
public class LogAspect {

    /**
     * <p>配置切面</p>
     */
    @Pointcut("public * org.springframework.test.service.*.*(..)")
    public void logPointcut(){}

    /**
     * <p>前置通知</p>
     */
    @Before
    public void logBefore() {
        System.out.println("This is LogAspect before");
    }

    /**
     * <p>后置返回通知</p>
     */
    @AfterReturning
    public void logAfter() {
        System.out.println("This is LogAspect After");
    }

    /**
     * <p>异常通知</p>
     */
    @AfterThrowing
    public void logAfterThrowing() {
        System.out.println("This is LogAspect AfterThrowing");
    }

}
