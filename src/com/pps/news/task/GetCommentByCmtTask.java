package com.pps.news.task;

import org.apache.http.message.BasicNameValuePair;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.CommentParser;
import com.pps.news.util.UIUtil;

/**
 * @file GetCommentByCmt.java
 * @create 2013-8-13 下午03:30:22
 * @author lilong
 * @description TODO 获取单条评论
 */
public class GetCommentByCmtTask extends GenericTask {

	public GetCommentByCmtTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
	}

	@Override
	protected Result doInBackground(String... params) {
		BetterHttp httpClient = BetterHttp.getInstance();
		String domain = "ppsios";
		Result result = httpClient.doHttpPost(Constants.getCommentGetByCmt(), 
				new BasicNameValuePair("domain", domain),
				new BasicNameValuePair("sign", UIUtil.md5ForString(domain+"vcommet")),
				new BasicNameValuePair("source", "news"),
				new BasicNameValuePair("upload_id", params[0]),
				new BasicNameValuePair("cmt_id", params[1]));
		result.addResult(new CommentParser().parseComment(result.getMessage()));
		return result;
	}

}
