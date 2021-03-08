package com.leaves.navigationdemo.app;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.leaves.navigationannotation.DestinationType;
import com.leaves.navigationdemo.app.navigator.FixFragmentNavigator;
import com.leaves.navigationdemo.model.Destination;
import com.leaves.navigationdemo.utils.AppGlobal;

import java.util.Map;

public class NavGraphBuilder {

    public static void build(NavController navController, FragmentActivity activity, FragmentManager childFragmentManager, int containerId) {

        NavigatorProvider navigatorProvider = navController.getNavigatorProvider();
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

//        NavigatorProvider navigatorProvider = navController.getNavigatorProvider();
        //系统的FragmentNavigator,是用Replace去实现页面的替换,这样会使fragment进行一个多次的初始化操作,很不友好
        //FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);
        //自定义的FragmentNavigator,使用hide跟show去替换replace,友好交互体验
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(activity, childFragmentManager, containerId);
        navigatorProvider.addNavigator(fragmentNavigator);

        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);


        Map<String, Destination> destinationConfig = AppConfig.getDestinationConfig();

        for (Destination valueDestination : destinationConfig.values()) {
            if (DestinationType.TYPE_FRAGMENT.equals(valueDestination.getClassType())) {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(valueDestination.getClassName());
                destination.setId(valueDestination.getId());
                destination.addDeepLink(valueDestination.getPageUrl());
                navGraph.addDestination(destination);
            } else if (DestinationType.TYPE_ACTIVITY.equals(valueDestination.getClassType())) {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setComponentName(new ComponentName(AppGlobal.getGlobalApplication().getPackageName(), valueDestination.getClassName()));
                destination.setId(valueDestination.getId());
                destination.addDeepLink(valueDestination.getPageUrl());
                navGraph.addDestination(destination);
            }

            if (valueDestination.getAsStart()) {
                navGraph.setStartDestination(valueDestination.getId());
            }
        }

        navController.setGraph(navGraph);
    }
}
