package com.pps.news.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.pps.news.app.NewsApplication;

public class Utility {

	private static final String ASSETS_NEWS_PREFIX = "_news_list.data";
	private static final String ASSETS_COMMENT_PREFIX = "_comment_list.data";
	
	private Utility() {
	}
	
	
	/**
	 * 读取新闻缓存 
	 * @return
	 */
	public static String readNewsFromAsset() {
		return readAssets(ASSETS_NEWS_PREFIX);
	}
	
	/**
	 * 读取评论缓存
	 * @return
	 */
	public static String readCommentFromAsset() {
		return readAssets(ASSETS_COMMENT_PREFIX);
	}
	
	private static String readAssets(String dirPath) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			NewsApplication mClient = NewsApplication.getInstance();
			InputStream is = mClient.getAssets().open("data/"+dirPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String input = null;
			while ((input = br.readLine())!=null) {
				sb.append(input);
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}
}
