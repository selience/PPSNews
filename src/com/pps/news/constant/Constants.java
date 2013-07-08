package com.pps.news.constant;

/**
 * @file Constants.java
 * @create 2013-6-4 下午03:50:38
 * @author lilong dreamxsky@gmail.com
 * @description TODO 应用存储常量
 */
public class Constants {

	public static final String APP_NAME = "PPSNews";
	
	// 开发者模式，Release版本更改为false；
	public static final boolean DEVELOP_MODE = false;
	
	/** 服务器请求URL */
	public static final String SERVER_URL = "http://dy.ugc.pps.tv/api";

	public static final String NEWS_DETAIL_EXTRAS = "_extras";
	public static final int NEWS_DETAIL_SELF_COMMENT = 0x1;
	public static final int NEWS_DETAIL_FRIEND_COMMENT = 0x2;
	
	
	
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
