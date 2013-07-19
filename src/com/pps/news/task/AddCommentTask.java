package com.pps.news.task;

import org.apache.http.message.BasicNameValuePair;

import tv.pps.vipmodule.vip.AccountVerify;

import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.util.UIUtil;

/**
 * @file AddCommentTask.java
 * @create 2013-7-12 下午06:21:18
 * @author lilong
 * @description TODO 添加评论任务
 */
public class AddCommentTask extends GenericTask {

	public AddCommentTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Result doInBackground(String... params) {
		String domain = "ppsios";
		String sign = UIUtil.md5ForString(domain+"vcommet");
		
		AccountVerify account = AccountVerify.getInstance();
		BetterHttp httpClient =  BetterHttp.getInstance();
		if (account.getmUID()==null) { // 匿名发表评论
			return httpClient.doHttpPost(Constants.getCommentCreate(),
					new BasicNameValuePair("upload_id", params[0]),
					new BasicNameValuePair("cmt_text", params[1]), 
					new BasicNameValuePair("user_ip", params[2]), 
					new BasicNameValuePair("cmd_type", "1"), 
					new BasicNameValuePair("source", "news"), 
					new BasicNameValuePair("domain", domain),
					new BasicNameValuePair("sign", sign)); 
		} else {  // 登陆发表评论
			return httpClient.doHttpPost(Constants.getCommentCreate(),
					new BasicNameValuePair("upload_id", params[0]), // 视频id
					new BasicNameValuePair("cmt_text", params[1]), // 评论内容
					new BasicNameValuePair("user_ip", params[2]), // 用户发布评论IP
					new BasicNameValuePair("cmd_type", "1"), // 发布评论类型 1：视频评论 2:频道评论 3：剧集 4:专题 5:特殊页面6:直播评论
					new BasicNameValuePair("source", "news"), // 接口调用来源 （web,pc,v，mobile）
					new BasicNameValuePair("domain", domain),
					new BasicNameValuePair("sign", sign), // 验证字符串算法(md5($domain.'vcommet'))
					new BasicNameValuePair("user_id", account.getmUID()), // 发布评论用户id
					new BasicNameValuePair("nick_name", account.getmDisplayName()), //用户名
					new BasicNameValuePair("user_face", account.getmIconUrl())); // 用户头像
		}
	}

}
