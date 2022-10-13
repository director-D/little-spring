package org.springframework.aop;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/4 下午9:58
 * @Version: 1.0
 * @Description:
 */
public class AopConfig {

    private String aspectClass;

    private String pointCut;

    private String before;

    private String afterReturn;

    private String afterThrow;

    private String afterThrowClass;


    public String getPointCut() {
        return pointCut;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfterReturn() {
        return afterReturn;
    }

    public void setAfterReturn(String afterReturn) {
        this.afterReturn = afterReturn;
    }

    public String getAfterThrow() {
        return afterThrow;
    }

    public void setAfterThrow(String afterThrow) {
        this.afterThrow = afterThrow;
    }

    public String getAfterThrowClass() {
        return afterThrowClass;
    }

    public void setAfterThrowClass(String afterThrowClass) {
        this.afterThrowClass = afterThrowClass;
    }

    public String getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
    }




}
