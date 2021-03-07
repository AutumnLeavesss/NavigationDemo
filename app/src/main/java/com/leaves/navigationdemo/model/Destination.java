package com.leaves.navigationdemo.model;




public class Destination {

    /**
     * asStart : false
     * needLogin : false
     * className : com.leaves.navigationdemo.ui.dashboard.DashboardFragment
     * pageUrl : main/tabs/dash
     * id : 1713553676
     * classType : Fragment
     */

    private Boolean asStart;
    private Boolean needLogin;
    private String className;
    private String pageUrl;
    private Integer id;
    private String classType;

    public Destination() {
    }

    public Destination(Boolean asStart, Boolean needLogin, String className, String pageUrl, Integer id, String classType) {
        this.asStart = asStart;
        this.needLogin = needLogin;
        this.className = className;
        this.pageUrl = pageUrl;
        this.id = id;
        this.classType = classType;
    }

    public Boolean getAsStart() {
        return asStart;
    }

    public void setAsStart(Boolean asStart) {
        this.asStart = asStart;
    }

    public Boolean getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(Boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}
