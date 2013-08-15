package com.pps.news.bean;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class AppInfo {

	private int id;
	private String name;
	private String label;
	private String packageName;
	private Drawable drawable;

	public AppInfo() {
	}

	public AppInfo(int id, String label, Drawable drawable, ResolveInfo it) {
		this.id = id;
		this.name = it.activityInfo.name;
		this.label = label;
		this.packageName = it.activityInfo.packageName;
		this.drawable = drawable;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

}
