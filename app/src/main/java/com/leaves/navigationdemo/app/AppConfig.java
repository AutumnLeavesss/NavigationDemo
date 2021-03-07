package com.leaves.navigationdemo.app;

import android.content.res.AssetManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.leaves.navigationdemo.model.BottomBar;
import com.leaves.navigationdemo.model.BottomBarTab;
import com.leaves.navigationdemo.model.Destination;
import com.leaves.navigationdemo.utils.AppGlobal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class AppConfig {

    private static AppConfig appConfig;
    private static Map<String, Destination> destinationConfig;
    private static BottomBar sBottomBar;
    private BottomBarTab bottomBarTab;

    public static AppConfig getInstance(){
        if (appConfig == null){
            appConfig = new AppConfig();
        }
        return appConfig;
    }

    public static Map<String, Destination> getDestinationConfig() {
        if (destinationConfig == null) {
            String destinationContent = parseFile("destination.json");
            destinationConfig = JSON.parseObject(destinationContent, new TypeReference<Map<String, Destination>>() {
            }.getType());
        }

        return destinationConfig;
    }

    public BottomBarTab getBottomBarTab() {
        Log.e("Header-Bottom-Bar", "getBottomBarTab: 我进来了" );
        if (bottomBarTab == null) {
            String tabsConfig = parseFile("main_tabs_config.json");
            bottomBarTab = JSON.parseObject(tabsConfig, BottomBarTab.class);

        }
        Log.e("Header-Bottom-Bar", "getBottomBarTab 我获取到数据了 : " + bottomBarTab );
        return bottomBarTab;
    }

    public static BottomBar getBottomBarConfig() {
        if (sBottomBar == null) {
            String content = parseFile("main_tabs_config_demo.json");
            sBottomBar = JSON.parseObject(content, BottomBar.class);
        }
        Log.e("Header-Bottom-Bar", "getBottomBarConfig 我获取到数据了 : " + sBottomBar );
        return sBottomBar;
    }


    private static String parseFile(String fileName) {
        AssetManager assets = AppGlobal.getGlobalApplication().getResources().getAssets();

        InputStream openFile = null;
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            openFile = assets.open(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(openFile));
            String line = "";
            int length = 1024;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (openFile != null) {
                try {
                    openFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
