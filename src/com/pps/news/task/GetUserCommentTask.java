package com.pps.news.task;

import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import tv.pps.vipmodule.vip.AccountVerify;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.CommentParser;
import com.pps.news.util.UIUtil;

/**
 * @file GetUserCommentTask.java
 * @create 2013-7-15 下午04:57:50
 * @author lilong
 * @description TODO 获取用户发表的所有评论
 */
public class GetUserCommentTask extends GenericTask {

	public GetUserCommentTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Result doInBackground(String... params) {
		AccountVerify account = AccountVerify.getInstance();
		BetterHttp httpClient = BetterHttp.getInstance();
		String domain = "ppsios";
		Result result = httpClient.doHttpPost(Constants.getCommentByUser(params[0]), 
				new BasicNameValuePair("comment_user_id", account.getmUID()),
				new BasicNameValuePair("per_page", params[1]),			
				new BasicNameValuePair("domain", domain),
				new BasicNameValuePair("sign", UIUtil.md5ForString(domain+"vcommet")),
				new BasicNameValuePair("source", "news"));
		
		if (result.getCode()==HttpStatus.SC_OK) {
			Group<Comment> comments = new CommentParser().parse(result.getMessage());
			result.addResult(comments);
		}
		return result;
	}

}
