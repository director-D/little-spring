package org.springframework.mybatis.utils;

import org.springframework.mybatis.config.Configuration;

import java.sql.*;
import java.util.List;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 14:45
 * @Version: 1.0
 * @Description:
 */
public class JDBCUtils {

    private static String driver , url , userName , password;

    private static Connection connection;

    public static void init(Configuration config){
        driver = config.getDriver();
        url = config.getUrl();
        userName = config.getUserName();
        password = config.getPassWord();
    }

    public static boolean load(){
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static Connection connection(){
        load();
        try {
            if(connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(url,userName,password);
                //
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        }
        return null;
    }

    public static Statement createStatement(){
        connection();
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static PreparedStatement preparedStatement(String sql){
        connection();
        try {
            return connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void commit(Connection conn){
        if(conn != null){
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static void rollBack(Connection conn){
        if(conn!=null){
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static Object typeOf(Object object){
        Object r = object;
        if(object instanceof java.sql.Timestamp){
            return r;
        }

        //将java.util.date 转成 java.sql.Date
        if(object instanceof  java.util.Date){
            r = new Date(((java.util.Date) object).getTime());
            return r;
        }

        //将Character 或 char 变成 String
        if(object instanceof Character || object.getClass() == char.class){
            r = String.valueOf(object);
            return r;
        }
        return r;
    }

    public static ResultSet query(String sql, List<Object> params){
        if(sql == null || sql.trim().isEmpty() || !sql.trim().toLowerCase().startsWith("select")){
            throw new RuntimeException("不支持");
        }
        try {
            if(params.size() > 0){
                PreparedStatement preparedStatement = preparedStatement(sql);
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i+1,params.get(i));
                }
                return preparedStatement.executeQuery();

            }else {
                Statement statement = createStatement();
                return statement.executeQuery(sql);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //除查找插入外的其他操作
    public static int execute(String sql , Object... params){
        if(sql == null || sql.trim().isEmpty() || sql.trim().toLowerCase().startsWith("select")){
            throw new RuntimeException("不支持");
        }
        int line ;
        sql = sql.trim().toLowerCase();

        Connection conn = null;
        try {
            if(params.length >0 ){
                PreparedStatement preparedStatement = preparedStatement(sql);
                conn = preparedStatement.getConnection();
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i+1,typeOf(params[i]));
                }
                line = preparedStatement.executeUpdate();
                commit(conn);
            }else {
                Statement statement = createStatement();
                conn = statement.getConnection();
                line = statement.executeUpdate(sql);
                commit(conn);
            }
        } catch (SQLException e) {
            //回滚事务
            rollBack(conn);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return line;
    }


    public static void release(Object closeable){
        if(closeable != null){
            if(closeable instanceof ResultSet){
                try {
                    ((ResultSet) closeable).close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(closeable instanceof Statement){
                try {
                    ((Statement) closeable).close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(closeable instanceof  Connection){
                try {
                    ((Connection) closeable).close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }






}
