package com.pps.news.task;

import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.NewsParser;

/**
 * @file GetNewsListTask.java
 * @create 2013-7-12 下午06:26:26
 * @author lilong
 * @description TODO 获取新闻列表
 */
public class GetNewsListTask extends GenericTask {

	public GetNewsListTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Result doInBackground(String... params) {
		// TODO Auto-generated method stub
		BetterHttp httpClient = BetterHttp.getInstance();
		Result result = httpClient.doHttpGet(Constants.getNewsList());
		Group<News> listNews = new NewsParser().parse(result.getMessage());
		result.addResult(listNews);
		return result;
	}

}
