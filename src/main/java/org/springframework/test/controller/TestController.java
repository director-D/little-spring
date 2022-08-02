package org.springframework.test.controller;

import org.springframework.annotation.Controller;
import org.springframework.annotation.RequestMapping;
import org.springframework.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/2 下午3:30
 * @Version: 1.0
 * @Description:
 */
@Controller
@RequestMapping("little_spring_Web_exploded")
public class TestController {

    @RequestMapping("/test")
    public ModelAndView test(@RequestParam("author") String author) {
        Map<String, Object> model = new HashMap<>();
        model.put("author", author);
        return new ModelAndView("test", model);
    }


    @RequestMapping("/exception")
    public void exception() {
        int i = 1 / 0;
    }


}
