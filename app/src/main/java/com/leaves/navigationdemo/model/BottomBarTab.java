package com.leaves.navigationdemo.model;

import java.util.List;

public class BottomBarTab{
	private String activityColor;
	private String inActivityColor;
	private int selectTab;
	private List<BottomTab> tabs;

	public String getActivityColor() {
		return activityColor;
	}

	public void setActivityColor(String activityColor) {
		this.activityColor = activityColor;
	}

	public String getInActivityColor() {
		return inActivityColor;
	}

	public void setInActivityColor(String inActivityColor) {
		this.inActivityColor = inActivityColor;
	}

	public int getSelectTab() {
		return selectTab;
	}

	public void setSelectTab(int selectTab) {
		this.selectTab = selectTab;
	}

	public List<BottomTab> getTabs() {
		return tabs;
	}

	public void setTabs(List<BottomTab> tabs) {
		this.tabs = tabs;
	}
}