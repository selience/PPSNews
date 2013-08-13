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
	public static final boolean DEBUG = false;
	
	/** 新闻详情传递参数  */
	public static final String NEWS_ID_EXTRAS = "_newsId";
	public static final String NEWS_DETAIL_EXTRAS = "_news";
	public static final int NEWS_DETAIL_SELF_COMMENT = 0x1;
	public static final int NEWS_DETAIL_FRIEND_COMMENT = 0x2;

	/** 新闻详情显示数目  */
	public static final String NEWS_DETAIL_PAGESIZE = "5";
	/** 评论列表显示数目  */
	public static final String COMMENTS_LIST_PAGESIZE = "8";
	
	/** 闹钟设置传递参数  */
	public static final String ALARM_EXTRAS = "intent.extra";
	public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";

	/** 闹钟通知Intent Action */
	public static final String ALARM_ALERT_ACTION = "com.android.alarmclock.ALARM_ALERT";

	/** 自动清空缓存的时间   */
	public static final long CLEAR_NEWS_CACHE_TIMESTAMP = 3 * 24 * 60 * 60 * 1000;
	
	
	/** 获取新闻列表 GET请求  */
	public static String getNewsList() {
		return "http://dy.ugc.pps.tv/api/mobile/news_api.php";
	}

	/** 获取所有评论  */
	public static String getCommentByVideoId(String pageID) {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentGetByVideo&returntype=json&encode=gbk&pageID="+pageID;
	}
	
	/** 新增评论  */
	public static String getCommentCreate() {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentCreate";
	}
	
	/** 删除评论  */
	public static String getCommentDelete() {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentDelete";
	}
	
	/** 获取用户发布过的所有评论  */
	public static String getCommentByUser(String pageID) {
		return "http://vcomment.pt.pps.tv/api.php?act=CommentGetByUser&pageID="+pageID;
	}
}
