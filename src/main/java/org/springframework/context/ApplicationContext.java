package org.springframework.context;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 上午2:25
 * @Version: 1.0
 * @Description:
 */
public interface ApplicationContext {

    void refresh() throws Exception;

    Object getBean(String beanName);


}
