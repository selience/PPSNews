package com.pps.news.app;

import com.pps.news.constant.Constants;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.util.ImageCache;
import com.pps.news.util.Log;
import android.app.Application;

/**
 * @file PPSNewsApplication.java
 * @create 2013-6-4 下午03:48:14
 * @author lilong dreamxsky@gmail.com
 * @description TODO 自定义Application基类
 */
public class NewsApplication extends Application {
	private static NewsApplication _instance;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		
		initialize();
	}

	private void initialize() {
		Log.setLevel(Constants.DEVELOP_MODE);
		ImageCache.initInstance("PPSNews");
		PreferenceUtils.setDefaultPreferences(this);
		// 初始化用户信息
		PreferenceUtils.obtainUser();
	}

	@Override
	public void onLowMemory() {
		ImageCache.getInstance().clearCache();
		super.onLowMemory();
	}
	
	public static NewsApplication getInstance() {
		return _instance;
	}
}
