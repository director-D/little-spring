package org.springframework.context.support;

import org.springframework.annotation.*;
import org.springframework.annotation.mybatis.Mapper;
import org.springframework.aop.AopUtils;
import org.springframework.aop.support.AdvisedSupport;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mybatis.utils.MyBatisUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:37
 * @Version: 1.0
 * @Description:
 */
public abstract class AbstractApplicationContext extends DefaultListableBeanFactory implements ApplicationContext {

    protected BeanDefinitionReader reader;

    /**
     * <p>保存单例对象</p>
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    /**
     * <p>保存包装对象</p>
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();


    @Override
    public void refresh() throws Exception {

        // 扫描需要扫描的包，并把相关的类转化为beanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //初始化JDBC环境信息
        MyBatisUtils.instantiationMybatisConfig("application.properties");
        //扫描mapper
        MyBatisUtils.scanMappperBeanDefinition(beanDefinitions);
        // 注册，将beanDefinition放入IOC容器存储
        doRegisterBeanDefinition(beanDefinitions);
        // 初始化AOP配置类
        AopUtils.instantiationAopConfig(beanDefinitions);
        // 将非懒加载的类初始化
        doAutowired();

    }

    /**
     * <p>将beanDefinition放入IOC容器存储</p>
     */
    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception(beanDefinition.getFactoryBeanName() + "已经存在！");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }


    /**
     * <p>将非懒加载的类初始化</p>
     */
    private void doAutowired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }


    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        try {
            // 通过bd实例化bean
            Object instance = instantiateBean(beanDefinition);
            if (instance == null) {
                return null;
            }
            // 将实例化后的bean使用bw包装
            BeanWrapper beanWrapper = new BeanWrapper(instance);

            this.factoryBeanInstanceCache.put(beanDefinition.getBeanClassName(), beanWrapper);

            // 开始注入操作
            populateBean(instance);

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateBean(Object instance) throws Exception {

        // 判断是否是代理类
        if (AopUtils.isAopProxy(instance)) {
            instance = AopUtils.getTarget(instance);
        }

        if(instance == null){
            return;
        }
        Class clazz = instance.getClass();
        // 判断是否有Controller、Service、Component、Repository等注解标记
        if (!(clazz.isAnnotationPresent(Component.class) ||
                clazz.isAnnotationPresent(Controller.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Repository.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // 如果属性没有被Autowired标记，则跳过
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            String autowiredBeanName = field.getType().getName();

            field.setAccessible(true);

            try {
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            // 先判断单例池中是否存在该类的实例
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                //
                if(clazz.isAnnotationPresent(Mapper.class)){
                    //接入mybatis 功能
                    instance= MyBatisUtils.createProxy(clazz).getProxy();
                }else {
                    instance = clazz.newInstance();
                }




                // 接入AOP功能
                for (AdvisedSupport aspect : AopUtils.CONFIGS) {
                    aspect.setTargetClass(clazz);
                    aspect.setTarget(instance);

                    if (aspect.pointCutMatch()) {
                        instance = AopUtils.createProxy(aspect).getProxy();
                    }
                }

//                this.factoryBeanObjectCache.put(className, instance);
//                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(), instance);


                this.factoryBeanObjectCache.put(className, instance);
                this.factoryBeanObjectCache.put(beanDefinition.getBeanClassName(), instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;

    }


}
