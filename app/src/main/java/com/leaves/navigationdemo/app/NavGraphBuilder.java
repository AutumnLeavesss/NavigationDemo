package com.leaves.navigationdemo.app;

import android.content.ComponentName;

import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.leaves.navigationannotation.DestinationType;
import com.leaves.navigationdemo.model.Destination;
import com.leaves.navigationdemo.utils.AppGlobal;

import java.util.Map;

public class NavGraphBuilder {

    public static void build(NavController navController) {
        NavigatorProvider navigatorProvider = navController.getNavigatorProvider();

        FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);
        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

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
