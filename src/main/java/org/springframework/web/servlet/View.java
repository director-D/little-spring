package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/1 下午5:21
 * @Version: 1.0
 * @Description:
 */
public class View {

    /**
     * <p>内容类型</p>
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    /**
     * <p>模板文件</p>
     */
    private File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    /**
     * <p>处理特殊字符串</p>
     */
    public static String makeStringForRegExp(String string) {
        return string
                .replace("\\", "\\\\")
                .replace("*", "\\")
                .replace("+", "\\+")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("?", "\\?")
                .replace(",", "\\,")
                .replace(".", "\\.")
                .replace("&", "\\&");
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");
        StringBuffer sb = new StringBuffer();

        //对模板逐行读取,当有符号${}包裹变量时,将其替换
        try {
            String line = null;
            while ((line = ra.readLine()) != null) {
                // 避免出现乱码
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                // 利用正则查找每一行中由${}包裹的变量
                Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    // 获取变量名
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("\\$\\{|\\}", "");
                    // 查找model中对应的变量
                    Object paramValue = model.get(paramName);
                    if (paramValue == null) {
                        continue;
                    }
                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                sb.append(line);
            }
        } finally {
            ra.close();
        }
        // 通过response响应给用户
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());
    }


}
