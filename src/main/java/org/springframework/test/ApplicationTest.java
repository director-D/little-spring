package org.springframework.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.config.ApplicationConfig;
import org.springframework.test.service.TestMybatisService;
import org.springframework.test.service.TestService;

public class ApplicationTest {


    public static void main(String[] args) throws Exception {

        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);

//        TestService service = (TestService) applicationContext.getBean("testService");
//        service.echo();

        //测试mybatis的功能 (与IOC整合)
        TestMybatisService myBatisService =
                (TestMybatisService) applicationContext.getBean("testMybatisService");
        System.out.println(myBatisService.select(1));


    }


}
