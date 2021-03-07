package com.leaves.navigationdemo.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.leaves.navigationdemo.R;
import com.leaves.navigationdemo.app.AppConfig;
import com.leaves.navigationdemo.model.BottomBarTab;
import com.leaves.navigationdemo.model.BottomTab;
import com.leaves.navigationdemo.model.Destination;

import java.util.List;

public class AppBottomBar extends BottomNavigationView {
    private int icons[] = new int[]{R.mipmap.icon_first, R.mipmap.icon_second, R.mipmap.icon_thrid, R.mipmap.icon_fourth, R.mipmap.icon_fifth};
    private BottomBarTab bottomBarTab;

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bottomBarTab = AppConfig.getInstance().getBottomBarTab();

        List<BottomTab> tabs = bottomBarTab.getTabs();

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_activated};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(bottomBarTab.getActivityColor())
                , Color.parseColor(bottomBarTab.getInActivityColor())};
        ColorStateList colorStateList = new ColorStateList(states, colors);

        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(bottomBarTab.getSelectTab());

        for (BottomTab tab : tabs) {
            if (!tab.isEnable()) {
                continue;
            }
            int itemId = getId(tab.getPageUrl());
            if (itemId < 0) {
                continue;
            }
            MenuItem menuItem = getMenu().add(0, itemId, tab.getIndex(), tab.getTitle());
            menuItem.setIcon(icons[tab.getIndex()]);

        }

        for (BottomTab tab : tabs) {
            if (!tab.isEnable()) {
                continue;
            }
            int itemId = getId(tab.getPageUrl());
            if (itemId < 0) {
                continue;
            }
            int iconSize = dpToPx(tab.getSize());
            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(tab.getIndex());
            bottomNavigationItemView.setIconSize(iconSize);
            if (TextUtils.isEmpty(tab.getTitle())) {
                int tintColor = TextUtils.isEmpty(tab.getTintColor()) ? Color.parseColor("#ff678f") : Color.parseColor(tab.getTintColor());
                bottomNavigationItemView.setIconTintList(ColorStateList.valueOf(tintColor));
                bottomNavigationItemView.setShifting(false);
            }
        }

        //底部导航栏默认选中项
        if (bottomBarTab.getSelectTab() != 0) {
            BottomTab selectTab = bottomBarTab.getTabs().get(bottomBarTab.getSelectTab());
            if (selectTab.isEnable()) {
                int itemId = getId(selectTab.getPageUrl());
                //这里需要延迟一下 再定位到默认选中的tab
                //因为 咱们需要等待内容区域,也就NavGraphBuilder解析数据并初始化完成，
                //否则会出现 底部按钮切换过去了，但内容区域还没切换过去
                post(() -> setSelectedItemId(itemId));
            }
        }

    }

    private int dpToPx(int size) {
        float value = getContext().getResources().getDisplayMetrics().density * size + 0.5f;
        return (int) value;
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getDestinationConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.getId();
    }

}
