package com.pps.news.app;

import java.io.File;
import java.util.Observer;
import com.pps.news.constant.Constants;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.location.PPSNewsLocation;
import com.pps.news.task.NotificationTask;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.FileUtils;
import com.pps.news.util.ImageCache;
import com.pps.news.util.Log;
import com.pps.news.util.UIUtil;
import android.app.Application;
import android.os.Environment;

/**
 * @file PPSNewsApplication.java
 * @create 2013-6-4 下午03:48:14
 * @author lilong dreamxsky@gmail.com
 * @description TODO 自定义Application基类
 */
public class NewsApplication extends Application {
	private static NewsApplication _instance;

	private NotificationTask notifyTask = new NotificationTask();
	
	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		
		initialize();
	}

	private void initialize() {
		Log.setLevel(Constants.DEBUG);
		ImageCache.initInstance("PPSNews"); 
		PreferenceUtils.setDefaultPreferences(this);
		PreferenceUtils.obtainUser(); // 获取用户状态
		PPSNewsLocation.getInstance().getLocation(); // 启用定位
		clearNews();
	}

	private void clearNews() {
		long nowTimestamp = System.currentTimeMillis();
		long lastCleanTimestamp = PreferenceUtils.getLastClearTimeStamp(this);
		if (lastCleanTimestamp>nowTimestamp || (nowTimestamp-lastCleanTimestamp)>Constants.CLEAR_NEWS_CACHE_TIMESTAMP) {
			if (PreferenceUtils.getIsAutoClearCache(this)) { // 自动清除过期新闻
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (UIUtil.isSDCardAvailable()) {
							ImageCache.getInstance().clear();
							File dirPath = new File(Environment.getExternalStorageDirectory(), CacheUtil.CACHE_DIRECTORY);
							FileUtils.deleteAllFile(dirPath.getAbsolutePath()); 
							PreferenceUtils.storeLastClearCacheTimeStamp(NewsApplication.this, System.currentTimeMillis());
						}
					}
				});
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.start();
			}
		}
	}
	
	@Override
	public void onLowMemory() {
		ImageCache.getInstance().evictAll();
		super.onLowMemory();
	}
	
	public void notifyObservers(Object data) {
		notifyTask.notifyObservers(data);
	}
	
	public void addObserver(Observer observer) {
		notifyTask.addObserver(observer);
	}
	
	public void removeObserver(Observer observer) {
		notifyTask.deleteObserver(observer);
	}
	
	public static NewsApplication getInstance() {
		return _instance;
	}
	
}
