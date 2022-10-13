package org.springframework.annotation.mybatis;

import org.springframework.annotation.Component;

import java.lang.annotation.*;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 14:27
 * @Version: 1.0
 * @Description:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

    String value() default "";

}
