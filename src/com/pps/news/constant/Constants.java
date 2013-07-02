package com.pps.news.constant;

import java.io.File;

import android.os.Environment;

/**
 * @file Constants.java
 * @create 2013-6-4 下午03:50:38
 * @author lilong dreamxsky@gmail.com
 * @description TODO 应用存储常量
 */
public class Constants {

	public static final String APP_NAME = "PPSNews";
	
	// 开发者模式，Release版本更改为false；
	public static final boolean DEVELOP_MODE = true;
	
	public static final String RELEASE_SERVER = "";
	
	public static final File ROOT_DIR = Environment.getExternalStorageDirectory();
	
	public static final File DATA_DIR = new File(ROOT_DIR, APP_NAME);
	
	public static final File CACHE_DIR = new File(DATA_DIR, "data");
	

	/** 服务器请求URL */
	public static final String SERVER_URL = "http://dy.ugc.pps.tv/api";
	

	/**
	 * 获取新闻列表  GET请求
	 * @return 新闻列表数据URL
	 */
	public static String getNewsList() {
		return "http://dy.ugc.pps.tv/api/mobile/news_api.php";
	}
	
	/** */
	public static String getCommentByVideoId() {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentGetByVideo";
	}
}
