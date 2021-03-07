package com.leaves.navigationannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface ActivityDestination {
    /**
     * 页面路径
     */
    String pageUrl();

    /**
     * 是否需要登录
     */
    boolean needLogin() default false;

    /**
     * 是否为首页
     */
    boolean asStart() default false;
}
