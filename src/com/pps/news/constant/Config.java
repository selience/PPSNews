package com.pps.news.constant;

/**
 * @file Config.java
 * @create 2013-7-8 下午03:17:24
 * @author lilong
 * @description TODO 存储常用的全局变量
 */
public final class Config {

	private Config() {
	}

	/** 存储用户的UserId  */ 
	public static String userId;
	
	/** 默认自动登陆 */
	public static boolean needAutoLogin = true;
	
	/** 默认自动清除过期新闻   */
	public static boolean needAutoClearCache = true;
}
