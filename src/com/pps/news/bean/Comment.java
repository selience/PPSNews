package com.pps.news.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

import com.pps.news.util.CacheUtil;
import com.pps.news.util.ParcelUtil;

/**
 * @file Comment.java
 * @create 2013-6-21 下午02:54:17
 * @author lilong (dreamxsky@gmail.com)
 * @description TODO 新闻评论实体对象
 */
public class Comment implements Parcelable {

	private int rn;
	private String addtime;
	private long cmt_id;
	private long upload_id;
	private int p_id;
	private long user_id;
	private String nick_name;
	private String cmt_text;
	private String user_face;
	private int user_type;
	private int cmt_flag;
	private int cmd_type;
	private int channel_id;
	private String urllink;
	private String pic;
	private int reply_count;
	private int vot_up;
	private int vot_down;
	private int is_top_quality;
	private String pinglun;
	private String timeline;
	private int reply_info_total;

	public Comment() {
	}

	private Comment(Parcel in) {
		rn = ParcelUtil.readIntFromParcel(in);
		addtime = ParcelUtil.readStringFromParcel(in);
		cmt_id = ParcelUtil.readLongFromParcel(in);
		upload_id = ParcelUtil.readLongFromParcel(in);
		p_id = ParcelUtil.readIntFromParcel(in);
		user_id = ParcelUtil.readLongFromParcel(in);
		nick_name = ParcelUtil.readStringFromParcel(in);
		cmt_text = ParcelUtil.readStringFromParcel(in);
		user_face = ParcelUtil.readStringFromParcel(in);
		user_type = ParcelUtil.readIntFromParcel(in);
		cmt_flag = ParcelUtil.readIntFromParcel(in);
		cmd_type = ParcelUtil.readIntFromParcel(in);
		channel_id = ParcelUtil.readIntFromParcel(in);
		urllink = ParcelUtil.readStringFromParcel(in);
		pic = ParcelUtil.readStringFromParcel(in);
		reply_count = ParcelUtil.readIntFromParcel(in);
		vot_up = ParcelUtil.readIntFromParcel(in);
		vot_down = ParcelUtil.readIntFromParcel(in);
		is_top_quality = ParcelUtil.readIntFromParcel(in);
		pinglun = ParcelUtil.readStringFromParcel(in);
		timeline = ParcelUtil.readStringFromParcel(in);
		reply_info_total = ParcelUtil.readIntFromParcel(in);
	}

	public int getRn() {
		return rn;
	}

	public void setRn(int rn) {
		this.rn = rn;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public long getCmt_id() {
		return cmt_id;
	}

	public void setCmt_id(long cmt_id) {
		this.cmt_id = cmt_id;
	}

	public long getUpload_id() {
		return upload_id;
	}

	public void setUpload_id(long upload_id) {
		this.upload_id = upload_id;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getCmt_text() {
		return cmt_text;
	}

	public void setCmt_text(String cmt_text) {
		this.cmt_text = cmt_text;
	}

	public String getUser_face() {
		return user_face;
	}

	public void setUser_face(String user_face) {
		this.user_face = user_face;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public int getCmt_flag() {
		return cmt_flag;
	}

	public void setCmt_flag(int cmt_flag) {
		this.cmt_flag = cmt_flag;
	}

	public int getCmd_type() {
		return cmd_type;
	}

	public void setCmd_type(int cmd_type) {
		this.cmd_type = cmd_type;
	}

	public int getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}

	public String getUrllink() {
		return urllink;
	}

	public void setUrllink(String urllink) {
		this.urllink = urllink;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getReply_count() {
		return reply_count;
	}

	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}

	public int getVot_up() {
		return vot_up;
	}

	public void setVot_up(int vot_up) {
		this.vot_up = vot_up;
	}

	public int getVot_down() {
		return vot_down;
	}

	public void setVot_down(int vot_down) {
		this.vot_down = vot_down;
	}

	public int getIs_top_quality() {
		return is_top_quality;
	}

	public void setIs_top_quality(int is_top_quality) {
		this.is_top_quality = is_top_quality;
	}

	public String getPinglun() {
		return pinglun;
	}

	public void setPinglun(String pinglun) {
		this.pinglun = pinglun;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public int getReply_info_total() {
		return reply_info_total;
	}

	public void setReply_info_total(int reply_info_total) {
		this.reply_info_total = reply_info_total;
	}

	public void writeToOutputStream(DataOutputStream out) throws IOException {
		CacheUtil.writeInt(out, rn);
		CacheUtil.writeString(out, addtime);
		CacheUtil.writeLong(out, cmt_id);
		CacheUtil.writeLong(out, upload_id);
		CacheUtil.writeInt(out, p_id);
		CacheUtil.writeLong(out, user_id);
		CacheUtil.writeString(out, nick_name);
		CacheUtil.writeString(out, cmt_text);
		CacheUtil.writeString(out, user_face);
		CacheUtil.writeInt(out, user_type);
		CacheUtil.writeInt(out, cmt_flag);
		CacheUtil.writeInt(out, cmd_type);
		CacheUtil.writeInt(out, channel_id);
		CacheUtil.writeString(out, urllink);
		CacheUtil.writeString(out, pic);
		CacheUtil.writeInt(out, reply_count);
		CacheUtil.writeInt(out, vot_up);
		CacheUtil.writeInt(out, vot_down);
		CacheUtil.writeInt(out, is_top_quality);
		CacheUtil.writeString(out, pinglun);
		CacheUtil.writeString(out, timeline);
		CacheUtil.writeInt(out, reply_info_total);
	}

	public Comment readToInputStream(DataInputStream in) throws IOException {
		rn = CacheUtil.readInt(in);
		addtime = CacheUtil.readString(in);
		cmt_id = CacheUtil.readLong(in);
		upload_id = CacheUtil.readLong(in);
		p_id = CacheUtil.readInt(in);
		user_id = CacheUtil.readLong(in);
		nick_name = CacheUtil.readString(in);
		cmt_text = CacheUtil.readString(in);
		user_face = CacheUtil.readString(in);
		user_type = CacheUtil.readInt(in);
		cmt_flag = CacheUtil.readInt(in);
		cmd_type = CacheUtil.readInt(in);
		channel_id = CacheUtil.readInt(in);
		urllink = CacheUtil.readString(in);
		pic = CacheUtil.readString(in);
		reply_count = CacheUtil.readInt(in);
		vot_up = CacheUtil.readInt(in);
		vot_down = CacheUtil.readInt(in);
		is_top_quality = CacheUtil.readInt(in);
		pinglun = CacheUtil.readString(in);
		timeline = CacheUtil.readString(in);
		reply_info_total = CacheUtil.readInt(in);
		return this;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		ParcelUtil.writeIntToParcel(out, rn);
		ParcelUtil.writeStringToParcel(out, addtime);
		ParcelUtil.writeLongToParcel(out, cmt_id);
		ParcelUtil.writeLongToParcel(out, upload_id);
		ParcelUtil.writeIntToParcel(out, p_id);
		ParcelUtil.writeLongToParcel(out, user_id);
		ParcelUtil.writeStringToParcel(out, nick_name);
		ParcelUtil.writeStringToParcel(out, cmt_text);
		ParcelUtil.writeStringToParcel(out, user_face);
		ParcelUtil.writeIntToParcel(out, user_type);
		ParcelUtil.writeIntToParcel(out, cmt_flag);
		ParcelUtil.writeIntToParcel(out, cmd_type);
		ParcelUtil.writeIntToParcel(out, channel_id);
		ParcelUtil.writeStringToParcel(out, urllink);
		ParcelUtil.writeStringToParcel(out, pic);
		ParcelUtil.writeIntToParcel(out, reply_count);
		ParcelUtil.writeIntToParcel(out, vot_up);
		ParcelUtil.writeIntToParcel(out, vot_down);
		ParcelUtil.writeIntToParcel(out, is_top_quality);
		ParcelUtil.writeStringToParcel(out, pinglun);
		ParcelUtil.writeStringToParcel(out, timeline);
		ParcelUtil.writeIntToParcel(out, reply_info_total);
	}

	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		public Comment createFromParcel(Parcel in) {
			return new Comment(in);
		}

		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};

}
