package com.leaves.navigationdemo.model;

public class BottomTab {
	private String title;
	private int size;
	private boolean enable;
	private int index;
	private String tintColor;
	private String pageUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTintColor() {
		return tintColor;
	}

	public void setTintColor(String tintColor) {
		this.tintColor = tintColor;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
}
