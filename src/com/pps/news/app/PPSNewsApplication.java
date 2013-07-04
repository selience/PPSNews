package com.pps.news.app;

import com.pps.news.constant.Constants;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.util.ImageCache;
import android.app.Application;

/**
 * @file PPSNewsApplication.java
 * @create 2013-6-4 下午03:48:14
 * @author lilong dreamxsky@gmail.com
 * @description TODO 自定义Application基类
 */
public class PPSNewsApplication extends Application {
	private static PPSNewsApplication _instance;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;

		initialize();
	}

	public static PPSNewsApplication getInstance() {
		return _instance;
	}

	private void initialize() {
		ImageCache.initInstance("PPSNews");
		// 设置自动清除过期新闻的全局状态
		Constants.needAutoClearCache = PreferenceUtils.getIsAutoClearCache(this);
	}

	@Override
	public void onLowMemory() {
		ImageCache.getInstance().clearCache();
		super.onLowMemory();
	}
}
