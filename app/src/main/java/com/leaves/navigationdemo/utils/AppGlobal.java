package com.leaves.navigationdemo.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppGlobal {

    private static Application application;

    public static Application getGlobalApplication(){
        if (application == null){
            try {
                Method currentApplication = Class.forName("android.app.ActivityThread").getMethod("currentApplication");
                application = (Application) currentApplication.invoke(null, new Object[]{});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return  application;
    }
}
