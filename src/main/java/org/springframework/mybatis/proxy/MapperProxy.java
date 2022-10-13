package org.springframework.mybatis.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.annotation.mybatis.*;
import org.springframework.aop.intercept.MethodInvocation;
import org.springframework.mybatis.utils.JDBCUtils;
import org.springframework.mybatis.utils.SqlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 15:26
 * @Version: 1.0
 * @Description:
 */
public class MapperProxy implements MethodInterceptor {

    private Class<?> mapperInterface ;

    public MapperProxy(Class<?> mapperInterface){
        this.mapperInterface = mapperInterface;
    }


    public Object getProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(mapperInterface);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    private Object query(String sql, Method method,Object[] args){
        try {
            List<String> sqlParams = SqlUtils.getParams(sql);
            Parameter[] params = method.getParameters();
            List<Object> sortedParams = getSortedParams(params, sqlParams, args);
            //使用占位符"?" 替代 sql 语句中的变量
            sql = SqlUtils.occupied(sql);
            //执行sql语句
            ResultSet rs = JDBCUtils.query(sql, sortedParams);
            if(!rs.next()){
                return null;
            }
            rs.previous();

            //
            Class<?> returnType = method.getReturnType();
            Object result = returnType.newInstance();
            while (rs.next()){
                Field[] fields = returnType.getDeclaredFields();
                for (Field field : fields) {
                    Object value = rs.getObject(field.getName());
                    field.setAccessible(true);
                    field.set(result,value);
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private int execute(String sql, Method method,Object[] args){
        //获取sql上的参数名称
        List<String> sqlParams = SqlUtils.getParams(sql);
        //获取方法上的参数信息
        Parameter[] parameters = method.getParameters();
        List<Object> sortedParams = getSortedParams(parameters, sqlParams, args);
        sql = SqlUtils.occupied(sql);
        return JDBCUtils.execute(sql,sortedParams.toArray());
    }

    private List<Object> getSortedParams(Parameter[] params, List<String> sqlParams, Object[] args) {
        ConcurrentHashMap<String,Object> paramMap = new ConcurrentHashMap<>();
        for (int i = 0; i < params.length; i++) {
            Parameter param = params[i];
            //获取参数名称
            Param annotation = param.getAnnotation(Param.class);
            if(annotation == null){
                throw new RuntimeException("请为mapper的返参数上添加@param注解");
            }
            String paramName = annotation.value().trim();
            if("".equals(paramName)){
                throw new RuntimeException("请为mapper的返参数上添加@param注解");
            }
            Object ob = args[i];
            paramMap.put(paramName,ob);
        }

        //排好序的参数
        List<Object> sortedParams = new ArrayList<>();
        for (String sqlParam : sqlParams) {
            Object o = paramMap.get(sqlParam);
            sortedParams.add(o);
        }

        return sortedParams;


    }


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        Object object = null;
        //增
        Insert insert = method.getAnnotation(Insert.class);
        if(insert!=null){
            object = execute(insert.value().trim(), method, args);
        }
        //删除
        Delete delete = method.getAnnotation(Delete.class);
        if(delete!=null){
            object = execute(delete.value().trim(), method, args);
        }

        //改
        Update update = method.getAnnotation(Update.class);
        if(update!=null){
            object = execute(update.value().trim(),method,args);
        }

        //查询
        Select select = method.getAnnotation(Select.class);
        if(select!=null){
            object = query(select.value().trim(),method,args);
        }
        return object;
    }
}
