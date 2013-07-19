package com.pps.news.task;

import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.CommentParser;
import com.pps.news.util.UIUtil;

/**
 * @file GetNewsCommentsTask.java
 * @create 2013-7-12 下午06:28:42
 * @author lilong
 * @description TODO 获取新闻评论
 */
public class GetVideoCommentTask extends GenericTask {

	public GetVideoCommentTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Result doInBackground(String... params) {
		// TODO Auto-generated method stub
		BetterHttp httpClient = BetterHttp.getInstance();
		String domain = "ppsios";
		Result result = httpClient.doHttpPost(Constants.getCommentByVideoId(params[1]), 
				new BasicNameValuePair("domain", domain),
				new BasicNameValuePair("sign", UIUtil.md5ForString(domain+"vcommet")),
				new BasicNameValuePair("upload_id", params[0]),
				new BasicNameValuePair("source", "news"),
				new BasicNameValuePair("per_page", params[2]),
				new BasicNameValuePair("user_agent", "PPSNews1.0"),
				new BasicNameValuePair("platform", "android"));
		// 测试数据
		if (result.getCode()==HttpStatus.SC_OK) {
			Group<Comment> comments = new CommentParser().parse(result.getMessage());
			result.addResult(comments);
		}
		return result;
	}

}
