package org.springframework.mybatis.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 15:20
 * @Version: 1.0
 * @Description:
 */
public class SqlUtils {

    private static Pattern pattern = Pattern.compile("\\#\\{\\w+\\}");

    public static String occupied(String sql){
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()){
            sql = sql.replace(matcher.group(), "?");
        }
        return sql;
    }

    public static List<String> getParams(String sql){
        Matcher matcher = pattern.matcher(sql);
        List<String> list = new ArrayList<>();
        while (matcher.find()){
            list.add(matcher.group().trim()
                    .replace("#{","")
                    .replace("}",""));
        }
        return list;
    }



}
