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
	public static final boolean DEVELOP_MODE = true;

	/** 新闻详情传递参数  */
	public static final String NEWS_ID_EXTRAS = "_newsId";
	public static final String NEWS_DETAIL_EXTRAS = "_news";
	public static final int NEWS_DETAIL_SELF_COMMENT = 0x1;
	public static final int NEWS_DETAIL_FRIEND_COMMENT = 0x2;

	/** 闹钟设置传递参数  */
	public static final String ALARM_EXTRAS = "alarms";

	/** 闹钟通知Intent Action */
	public static final String ALARM_ALERT_ACTION = "com.android.alarmclock.ALARM_ALERT";

	
	
	/** 获取新闻列表 GET请求  */
	public static String getNewsList() {
		return "http://dy.ugc.pps.tv/api/mobile/news_api.php";
	}

	/** 获取所有评论  */
	public static String getCommentByVideoId() {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentGetByVideo";
	}

	/** 新增评论  */
	public static String getCommentCreate() {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentCreate";
	}
}
