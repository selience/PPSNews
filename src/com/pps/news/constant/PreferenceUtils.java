package com.pps.news.constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @file PreferenceUtils.java
 * @create 2013-6-25 下午06:17:32
 * @author lilong
 * @description TODO 保存常用设置
 */
public class PreferenceUtils {

	private PreferenceUtils() {
	}

	private static final String PREFERENCE_LAST_UPDATE_TIMESTAMP = "lastUpdateTimeStamp";
	
	/** 保存上次更新新闻列表时间戳   */
	public static void saveLastUpdateTimeStamp(Context context, long timestamp) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putLong(PREFERENCE_LAST_UPDATE_TIMESTAMP, timestamp);
		editor.commit();
	}

	/** 获取上次更新新闻列表的时间戳  */
	public static long getLastUpdateTimeStamp(Context context) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefs.getLong(PREFERENCE_LAST_UPDATE_TIMESTAMP, 0L);
	}
	
}
