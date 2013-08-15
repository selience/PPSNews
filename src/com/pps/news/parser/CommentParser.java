package com.pps.news.parser;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;

public class CommentParser extends Parser<Comment> {

	@Override
	public Comment parse(JSONObject json) throws JSONException {
		Comment comment = new Comment();
		comment.setRn(json.optInt("rn"));
		comment.setAddtime(json.optString("addtime"));
		comment.setCmt_id(json.optLong("cmt_id"));
		comment.setUpload_id(json.optLong("upload_id"));
		comment.setP_id(json.optInt("p_id"));
		comment.setUser_id(json.optLong("user_id"));
		comment.setNick_name(json.optString("nick_name"));
		comment.setCmt_text(json.optString("cmt_text"));
		comment.setUser_face(json.optString("user_face"));
		comment.setUser_type(json.optInt("user_type"));
		comment.setCmt_flag(json.optInt("cmt_flag"));
		comment.setCmd_type(json.optInt("cmd_type"));
		comment.setChannel_id(json.optInt("channel_id"));
		comment.setUrllink(json.optString("urllink"));
		comment.setPic(json.optString("pic"));
		comment.setReply_count(json.optInt("reply_count"));
		comment.setVot_up(json.optInt("vot_up"));
		comment.setVot_down(json.optInt("vot_down"));
		comment.setIs_top_quality(json.optInt("is_top_quality"));
		comment.setPinglun(json.optString("pinglun"));
		comment.setTimeline(json.optString("timeline"));
		if (json.has("reply_info")) {
			JSONObject obj = json.getJSONObject("reply_info");
			comment.setReply_count(obj.optInt("total"));
			
		}
		return comment;
	}

	public String parseCommentId(String content) {
		try {
			JSONObject json = new JSONObject(content);
			return json.optString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Comment parseComment(String content) {
		try {
			JSONObject json = new JSONObject(content);
			Object itemObject = json.opt("data");
			if (itemObject instanceof JSONObject) {
				return parse((JSONObject) itemObject);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public Group<Comment> parse(String content) {
		Group<Comment> comments = new Group<Comment>();
		try {
			JSONObject json = new JSONObject(content);
			Object itemObject = json.opt("data");
			// 获取用户所有评论：有评论消息返回对象，反之返回空字符串；
			if (itemObject instanceof JSONObject) {
				json = (JSONObject) itemObject;
				Object object = json.get("datas");
				// 获取视频评论：有评论信息时返回对象，反之返回数组；
				if (object != null && object instanceof JSONObject) {
					JSONObject datas = (JSONObject) object;
					Iterator<?> iterator = datas.keys();
					while (iterator.hasNext()) {
						String key = iterator.next().toString();
						JSONObject itemJson = datas.optJSONObject(key);
						if (itemJson != null) {
							comments.add(parse(itemJson));
						}
					}
				}
				comments.setTotal(json.optInt("total"));
				comments.setTotal_page(json.optInt("total_page"));
				comments.setCur_page(json.optInt("cur_page"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return comments;
	}
}
