package com.pps.news.task;

import org.apache.http.message.BasicNameValuePair;
import com.pps.news.app.NewsApplication;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.CommentParser;
import com.pps.news.util.UIUtil;
import com.pps.news.util.Utility;

/**
 * @file GetNewsCommentsTask.java
 * @create 2013-7-12 下午06:28:42
 * @author lilong
 * @description TODO 获取新闻评论
 */
public class GetCommentsTask extends GenericTask {

	public GetCommentsTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Result doInBackground(String... params) {
		// TODO Auto-generated method stub
		BetterHttp httpClient = BetterHttp.getInstance();
		String domain = "ppsios";
		Result result = httpClient.doHttpPost(Constants.getCommentByVideoId(), 
				new BasicNameValuePair("returntype", "json"),
				new BasicNameValuePair("encode", "utf8"),
				new BasicNameValuePair("pageID", "1"),					
				new BasicNameValuePair("domain", domain),
				new BasicNameValuePair("sign", UIUtil.md5ForString(domain+"vcommet")),
				new BasicNameValuePair("upload_id", params[0]),
				new BasicNameValuePair("source", "mobile"),
				new BasicNameValuePair("user_agent", "PPSNews1.0"),
				new BasicNameValuePair("platform", "android"));
		// 测试数据
		Group<Comment> comments = new CommentParser().parse(result.getMessage());
		//Group<Comment> comments = new CommentParser().parse(Utility.readComment(NewsApplication.getInstance()));
		result.addResult(comments);
		return result;
	}

}
