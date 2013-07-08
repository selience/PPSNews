package com.pps.news.constant;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @file PreferenceUtils.java
 * @create 2013-6-25 下午06:17:32
 * @author lilong
 * @description TODO 保存常用设置
 */
public final class PreferenceUtils {

	private PreferenceUtils() {
	}

	private static final String PREFERENCE_USER_ID = "userid";
	private static final String PREFERENCE_USER_NAME = "username";
	private static final String PREFERENCE_USER_PASSWORD = "userpass";
	private static final String PREFERENCE_IS_AUTO_LOGIN = "isAutoLogin";
	private static final String PREFERENCE_IS_AUTO_CLEAR_CACHE = "isAutoClearCache";
	private static final String PREFERENCE_LAST_UPDATE_TIMESTAMP = "lastUpdateTimeStamp";
	
	/** 保存上次更新新闻列表时间戳   */
	public static void saveLastUpdateTimeStamp(SharedPreferences mPrefs, long timestamp) {
		Editor editor = mPrefs.edit();
		editor.putLong(PREFERENCE_LAST_UPDATE_TIMESTAMP, timestamp);
		editor.commit();
	}

	/** 获取上次更新新闻列表的时间戳  */
	public static long getLastUpdateTimeStamp(SharedPreferences mPrefs) {
		return mPrefs.getLong(PREFERENCE_LAST_UPDATE_TIMESTAMP, 0L);
	}
	
	/** 存储是否自动清除缓存状态  */
	public static void storeIsAutoClearCache(SharedPreferences mPrefs, boolean isAutoClear) {
		Editor editor = mPrefs.edit();
		editor.putBoolean(PREFERENCE_IS_AUTO_CLEAR_CACHE, isAutoClear);
		editor.commit();
	}
	
	/** 获取是否自动清除缓存的状态  */
	public static boolean getIsAutoClearCache(SharedPreferences mPrefs) {
		return mPrefs.getBoolean(PREFERENCE_IS_AUTO_CLEAR_CACHE, true);
	}
	
	// 存储是否自动登陆的状态
	public static void storeIsAutoLogin(SharedPreferences mPrefs, boolean isAutoLogin) {
		Editor editor = mPrefs.edit();
		editor.putBoolean(PREFERENCE_IS_AUTO_LOGIN, isAutoLogin);
		editor.commit();
	}
	
	// 获取是否自动登陆的状态
	public static boolean getIsAutoLogin(SharedPreferences mPrefs) {
		return mPrefs.getBoolean(PREFERENCE_IS_AUTO_LOGIN, true);
	}
	
	/** 存储用户名 */
	public static void storeUserName(SharedPreferences mPrefs, String userName) {
		Editor editor = mPrefs.edit();
		editor.putString(PREFERENCE_USER_NAME, userName);
		editor.commit();
	}
	
	/** 获取存储的用户名  */
	public static String getUserName(SharedPreferences mPrefs) {
		return mPrefs.getString(PREFERENCE_USER_NAME, null);
	}
	
	/** 存储用户密码  */
	public static void storeUserPass(SharedPreferences mPrefs, String userPass) {
		Editor editor = mPrefs.edit();
		editor.putString(PREFERENCE_USER_PASSWORD, userPass);
		editor.commit();
	}
	
	/** 获取存储的用户密码  */
	public static String getUserPass(SharedPreferences mPrefs) {
		return mPrefs.getString(PREFERENCE_USER_PASSWORD, null);
	}
	
	/** 存储用户标识Id  */
	public static void storeUserId(SharedPreferences mPrefs, String userId) {
		Editor editor = mPrefs.edit();
		editor.putString(PREFERENCE_USER_ID, userId);
		editor.commit();
	}
	
	/** 获取存储的用户Id  */
	public static String getUserId(SharedPreferences mPrefs) {
		return mPrefs.getString(PREFERENCE_USER_ID, null);
	}
}
