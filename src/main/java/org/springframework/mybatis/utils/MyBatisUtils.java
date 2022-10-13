package org.springframework.mybatis.utils;

import org.springframework.annotation.mybatis.Mapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.mybatis.config.Configuration;
import org.springframework.mybatis.proxy.MapperProxy;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 16:16
 * @Version: 1.0
 * @Description:
 */
public class MyBatisUtils {


    private static List<String> registerBeanClasses = new ArrayList<>();
    private static Configuration config;

    public static void instantiationMybatisConfig(String location){
        config = new Configuration(location);
        JDBCUtils.init(config);
    }

    public static List<BeanDefinition> scanMappperBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        List<BeanDefinition> mapperBeanDefinitions = new ArrayList<>();
        String mapperPackage = config.getMapperPackage();

        //扫描
        doScan(mapperPackage);

        for (String registerBeanClass : registerBeanClasses) {
            Class<?> mapper = Class.forName(registerBeanClass);
            if(!mapper.isAnnotationPresent(Mapper.class)){
                continue;
            }
            mapperBeanDefinitions.add(doCreateBeanDefinition(toLowerFirstCase(mapper.getSimpleName()),mapper.getName()));
        }

        //合并
        beanDefinitions.addAll(mapperBeanDefinitions);
        return beanDefinitions;
    }

    private static BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private static String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }

    private static void doScan(String scanPackage) throws Exception {
        //将包名转为文件路径
        URL url = MyBatisUtils.class.getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if(url == null){
            throw new Exception("包"+ scanPackage + "不存在");
        }

        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScan(scanPackage+"."+file.getName());

            }else {
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className = scanPackage+"."+ file.getName().replace(".class","");
                registerBeanClasses.add(className);
            }
        }
    }

    public static MapperProxy createProxy(Class<?> mapperInterface){
        return  new MapperProxy(mapperInterface);
    }


}
