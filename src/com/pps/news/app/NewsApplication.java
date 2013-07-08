package com.pps.news.app;

import com.pps.news.constant.Config;
import com.pps.news.constant.Constants;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.util.ImageCache;
import com.pps.news.util.Log;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @file PPSNewsApplication.java
 * @create 2013-6-4 下午03:48:14
 * @author lilong dreamxsky@gmail.com
 * @description TODO 自定义Application基类
 */
public class NewsApplication extends Application {
	public static SharedPreferences mPrefs;
	private static NewsApplication _instance;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		
		initialize();
	}

	public static NewsApplication getInstance() {
		return _instance;
	}

	private void initialize() {
		Log.setLevel(Constants.DEVELOP_MODE);
		ImageCache.initInstance("PPSNews");

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Config.userId = PreferenceUtils.getUserId(mPrefs);
		Config.needAutoLogin = PreferenceUtils.getIsAutoLogin(mPrefs);
		Config.needAutoClearCache = PreferenceUtils.getIsAutoClearCache(mPrefs);
	}

	@Override
	public void onLowMemory() {
		ImageCache.getInstance().clearCache();
		super.onLowMemory();
	}
}
