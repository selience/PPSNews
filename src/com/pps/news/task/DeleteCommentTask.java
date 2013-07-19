package com.pps.news.task;

import org.apache.http.message.BasicNameValuePair;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.util.UIUtil;

public class DeleteCommentTask extends GenericTask {

	private Comment comment = null;
	
	public DeleteCommentTask(TaskListener taskListener, String taskName, Comment comment) {
		super(taskListener, taskName);
		this.comment = comment;
	}

	@Override
	protected Result doInBackground(String... params) {
		String domain = "ppsios";
		BetterHttp httpClient = BetterHttp.getInstance();
		return httpClient.doHttpPost(Constants.getCommentDelete(), 
				new BasicNameValuePair("cmt_id", String.valueOf(comment.getCmt_id())), //评论id
				new BasicNameValuePair("user_id", String.valueOf(comment.getUser_id())),
				new BasicNameValuePair("upload_id", String.valueOf(comment.getUpload_id())), //新闻id
				new BasicNameValuePair("domain", domain),
				new BasicNameValuePair("user_ip", params[0]), //IP地址
				new BasicNameValuePair("sign", UIUtil.md5ForString(domain+"vcommet")),
				new BasicNameValuePair("source", "news"),
				new BasicNameValuePair("user_name", comment.getNick_name()));
	}

}
