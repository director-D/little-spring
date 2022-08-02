package org.springframework.web.servlet;

import org.springframework.annotation.Controller;
import org.springframework.annotation.RequestMapping;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 下午5:20
 * @Version: 1.0
 * @Description:
 */
public class DispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    private AbstractApplicationContext context;


    @Override
    public void init(ServletConfig config) {
        try {
            // 初始化IOC容器
            context = new AnnotationConfigApplicationContext(
                    Class.forName(config.getInitParameter(LOCATION)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化Spring MVC九大组件
        initStrategies(context);
    }

    /**
     * <p>初始化Spring MVC九大组件</p>
     *
     * @param context IOC容器
     */
    protected void initStrategies(AbstractApplicationContext context) {
        // 初始化多文件上传组件
        initMultipartResolver(context);
        // 初始化本地语言环境
        initLocaleResolver(context);
        // 初始化模板处理器
        initThemeResolver(context);
        // 初始化映射器
        initHandlerMappings(context);
        // 初始化适配器
        initHandlerAdapters(context);
        // 初始化异常拦截器
        initHandlerExceptionResolvers(context);
        // 初始化视图预处理器
        initRequestToViewNameTranslator(context);
        // 初始化视图转换器
        initViewResolvers(context);
        // 初始化FlashMap管理器
        initFlashMapManager(context);
    }

    private void initViewResolvers(AbstractApplicationContext context) {
        // 默认扫描"/template"目录
        String templateRoot = "/templates";
        String templateRootPath = this.getClass().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        Arrays.asList(templateRootDir.listFiles()).forEach(
                iter -> this.viewResolvers.add(new ViewResolver(templateRoot))
        );
    }

    private void initHandlerAdapters(AbstractApplicationContext context) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
        }

    }

    private void initHandlerMappings(AbstractApplicationContext context) {
        // 从IOC容器中获取所有的实例
        List<String> beanNames = new ArrayList<>(context.beanDefinitionMap.keySet());


        for (String beanName : beanNames) {
            Object controller = context.getBean(beanName);
            Class<?> clazz = controller.getClass();

            //
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }

            // 扫描所有public类型的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String regex = "/" + baseUrl + "/" + requestMapping.value()
                        .replaceAll("\\*", "*")
                        .replaceAll("/+", "+");
                Pattern pattern = Pattern.compile(regex);
                this.handlerMappings.add(new HandlerMapping(controller, method, pattern));
            }
        }
    }

    /**
     * <p>初始化九大组件的方法，由于我们实现的是迷你版的spring，只对映射器、视图解析器和适配器做初始化</p>
     */
    private void initMultipartResolver(AbstractApplicationContext context) {
    }

    private void initLocaleResolver(AbstractApplicationContext context) {
    }

    private void initThemeResolver(AbstractApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(AbstractApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(AbstractApplicationContext context) {
    }

    private void initFlashMapManager(AbstractApplicationContext context) {
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            response.getWriter().write("" +
                    "<h1>500</h1><hr/>" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        // 根据用户请求获取映射器
        HandlerMapping handler = getHandler(request);

        if (handler == null) {
            processDispatchResult(request, response, new ModelAndView("404"));
            return;
        }

        //获取handler适配器
        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        //渲染并响应给用户
        processDispatchResult(request, response, modelAndView);
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }

        //获取映射器的适配器
        HandlerAdapter handlerAdapter = this.handlerAdapters.get(handler);
        if (handlerAdapter.supports(handler)) {
            return handlerAdapter;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws IOException {
        if (modelAndView == null || this.viewResolvers.isEmpty()) {
            return;
        }

        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(modelAndView.getViewName());
            if (view != null) {
                view.render(modelAndView.getModel(), request, response);
                return;
            }
        }
    }

    private HandlerMapping getHandler(HttpServletRequest request) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (matcher.matches()) {
                return handlerMapping;
            }
        }

        return null;

    }


}
