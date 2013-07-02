package com.pps.news.app;

import com.pps.news.common.CommonOperate;
import com.pps.news.util.ImageCache;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @file PPSNewsApplication.java
 * @create 2013-6-4 下午03:48:14
 * @author lilong dreamxsky@gmail.com
 * @description TODO 自定义Application基类
 */
public class PPSNewsApplication extends Application {
	private static final String TAG = "PPSNews";
	
	public static SharedPreferences mPrefs;
	private static PPSNewsApplication _instance;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;

		initialize();
	}

	@Override
	public void onLowMemory() {
		ImageCache.getInstance().clearCache();
		super.onLowMemory();
	}
	
	public static PPSNewsApplication getInstance() {
		return _instance;
	}

	private void initialize() {
		ImageCache.initInstance("PPSNews");
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		CommonOperate.startAlarmService(this);
	}

}
