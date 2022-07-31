package org.springframework.context.annotation;

import org.springframework.annotation.ComponentScan;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:43
 * @Version: 1.0
 * @Description:
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    public AnnotationConfigApplicationContext(Class annotatedClass) throws Exception {
        // 初始化父类bdw
        super.reader = new BeanDefinitionReader(getScanPackage(annotatedClass));
        refresh();
    }


    @Override
    public void refresh() throws Exception {
        // 交给父类完成
        super.refresh();
    }

    @Override
    public Object getBean(String beanName) {
        return super.getBean(beanName);
    }


    /**
     * <p>获取@ComponentScan中的value值</p>
     */
    public String getScanPackage(Class annotatedClass) throws Exception {
        // 判断是否有ComponentScan注解
        if (!annotatedClass.isAnnotationPresent(ComponentScan.class)) {
            throw new Exception("请为注解配置类加上@ComponentScan注解！");
        }
        ComponentScan componentScan =
                (ComponentScan) annotatedClass.getAnnotation(ComponentScan.class);
        return componentScan.value().trim();
    }


}
