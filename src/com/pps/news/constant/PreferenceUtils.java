package com.pps.news.constant;

import tv.pps.vipmodule.vip.AccountVerify;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * @file PreferenceUtils.java
 * @create 2013-6-25 下午06:17:32
 * @author lilong
 * @description TODO 保存常用存储数据
 */
public final class PreferenceUtils {

	private PreferenceUtils() {
	}

	private static final String PREFERENCE_FILE_NAME = "ppsnews";
	
	private static final String PREFERENCE_USER_ID = "userid";
	private static final String PREFERENCE_USER_NAME = "username";
	private static final String PREFERENCE_USER_PASSWORD = "userpass";
	private static final String PREFERENCE_USER_AVATAR = "avatar";
	private static final String PREFERENCE_USER_NICKNAME = "nickname";
	private static final String PREFERENCE_USER_TYPE =  "viptype";
	private static final String PREFERENCE_USER_LEVEL =  "level";
	
	private static final String PREFERENCE_IS_AUTO_LOGIN = "isAutoLogin";
	private static final String PREFERENCE_IS_AUTO_CLEAR_CACHE = "isAutoClearCache";
	private static final String PREFERENCE_LAST_UPDATE_TIMESTAMP = "lastUpdateTimeStamp";

	private static SharedPreferences mPrefs;
	
	public static void setDefaultPreferences(Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	// 存储是否自动登陆的状态
	public static void storeIsAutoLogin(boolean isAutoLogin) {
		Editor editor = mPrefs.edit();
		editor.putBoolean(PREFERENCE_IS_AUTO_LOGIN, isAutoLogin);
		editor.commit();
	}
	
	// 获取是否自动登陆的状态
	public static boolean getIsAutoLogin() {
		return mPrefs.getBoolean(PREFERENCE_IS_AUTO_LOGIN, false);
	}
	
	/** 保存用户密码  */
	public static void storeUserPass(String password) {
		Editor editor = mPrefs.edit();
		editor.putString(PREFERENCE_USER_PASSWORD, password);
		editor.commit();
	}
	
	// 获取用户密码
	public static String getUserPassword() {
		return mPrefs.getString(PREFERENCE_USER_PASSWORD, null);
	}
	
	/** 清除用户密码  */
	public static void removeUserPass() {
		Editor editor = mPrefs.edit();
		editor.remove(PREFERENCE_USER_PASSWORD);
		editor.commit();
	}
	
	/** 存储用户登陆信息 */
	public static void storeUser(AccountVerify account) {
		Editor editor = mPrefs.edit();
		editor.putString(PREFERENCE_USER_ID, account.getmUID());
		editor.putString(PREFERENCE_USER_NAME, account.getmUserName());
		editor.putString(PREFERENCE_USER_AVATAR, account.getmIconUrl());
		editor.putString(PREFERENCE_USER_NICKNAME, account.getmDisplayName());
		editor.putString(PREFERENCE_USER_TYPE, account.getVipType());
		editor.putString(PREFERENCE_USER_LEVEL, account.getmLevel());
		editor.commit();
	}
	
	/** 初始化用户登陆信息   */
	public static AccountVerify obtainUser() {
		AccountVerify account = AccountVerify.getInstance();
		account.setmUID(mPrefs.getString(PREFERENCE_USER_ID, null));
		account.setmUserName(mPrefs.getString(PREFERENCE_USER_NAME, null));
		account.setmIconUrl(mPrefs.getString(PREFERENCE_USER_AVATAR, null));
		account.setmDisplayName(mPrefs.getString(PREFERENCE_USER_NICKNAME, null));
		account.setVipType(mPrefs.getString(PREFERENCE_USER_TYPE, null));
		account.setmLevel(mPrefs.getString(PREFERENCE_USER_LEVEL, null));
		// 用户id不为空值且大于0，即为登陆状态
		if (!TextUtils.isEmpty(account.getmUID()) && 
				Integer.parseInt(account.getmUID()) > 0) {
			account.setmLogin(true);
		}
		return account;
	}
	
	/** 清空用户信息  */
	public static void clearUser() {
		Editor editor = mPrefs.edit();
		editor.clear();
		editor.commit();
	}
	
	
	/*************************************************************************************/
	
	
	/** 存储是否自动清除缓存状态  */
	public static void storeIsAutoClearCache(Context context, boolean isAutoClear) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(PREFERENCE_IS_AUTO_CLEAR_CACHE, isAutoClear);
		editor.commit();
	}
	
	/** 获取是否自动清除缓存的状态  */
	public static boolean getIsAutoClearCache(Context context) {
		return getSharedPreferences(context).getBoolean(PREFERENCE_IS_AUTO_CLEAR_CACHE, true);
	}
	
	/** 保存上次更新新闻列表时间戳   */
	public static void saveLastUpdateTimeStamp(Context context, long timestamp) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putLong(PREFERENCE_LAST_UPDATE_TIMESTAMP, timestamp);
		editor.commit();
	}

	/** 获取上次更新新闻列表的时间戳  */
	public static long getLastUpdateTimeStamp(Context context) {
		return getSharedPreferences(context).getLong(PREFERENCE_LAST_UPDATE_TIMESTAMP, 0L);
	}
	
	/** 清空存储的所有values */
	public static void clearAll(Context context) {
		Editor editor = getSharedPreferences(context).edit();
		editor.clear();
		editor.commit();
	}
	
	private static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
	}
}
