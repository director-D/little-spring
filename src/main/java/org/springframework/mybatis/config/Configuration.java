package org.springframework.mybatis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 14:37
 * @Version: 1.0
 * @Description:
 */
public class Configuration {

    private String driver,url,userName,passWord;

    private String mapperPackage;

    private Properties config = new Properties();


    public Configuration(String location){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location
                .replace("classpath:", ""));

        try {
            this.config.load(is);
            this.driver= this.config.getProperty("mini.jdbc.driver");
            this.url = this.config.getProperty("mini.jdbc.url");
            this.userName = this.config.getProperty("mini.jdbc.username");
            this.passWord = this.config.getProperty("mini.jdbc.password");
            this.mapperPackage = this.config.getProperty("mini.mybatis.mapper.package");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }
}
