package org.springframework.beans.factory.config;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:23
 * @Version: 1.0
 * @Description:
 */
public class BeanDefinition {

    /**
     * <p>bean对应的全类名</p>
     */
    private String beanClassName;

    /**
     * <p>是否懒加载</p>
     */
    private boolean lazyInit = false;

    /**
     * <p>保存在IOC容器时的key值</p>
     */
    private String factoryBeanName;


    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }


}
