package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:23
 * @Version: 1.0
 * @Description:
 */
public class BeanDefinitionReader {

    /**
     * <p>存储扫描出来的bean的全类名</p>
     */
    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String scanPackage) throws Exception {
        doScan(scanPackage);
    }

    /**
     * <p>扫描包下的类</p>
     *
     * @param scanPackage 包名
     */
    public void doScan(String scanPackage) throws Exception {
        // 将包名转为文件路径
        // 将包名转为文件路径
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if (url == null) {
            throw new Exception("包" + scanPackage + "不存在！");
        }
        if (url == null) {
            throw new Exception("包" + scanPackage + "不存在！");
        }
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    // 如果不是class文件则跳过
                    continue;
                }
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registryBeanClasses.add(className);
            }
        }
    }

    /**
     * <p>将扫描到的类信息转化为bd对象</p>
     */
    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    // 如果是接口则跳过
                    continue;
                }
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    result.add(doCreateBeanDefinition(anInterface.getName(), beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <p>将类信息转化为beanDefinition</p>
     */
    public BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * <p>将类名首字母小写</p>
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
