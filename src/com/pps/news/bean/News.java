package com.pps.news.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.pps.news.parser.ResultType;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file News.java
 * @create 2013-6-4 下午04:20:42
 * @author lilong dreamsky@gmail.com
 * @description TODO 新闻实体对象
 */
public class News implements Parcelable, ResultType {

	public static final int NEWS_POSITION_FLAG_TOP = 1;
	public static final int NEWS_POSITION_FLAG_LEFT_TOP = 2;
	public static final int NEWS_POSITION_FLAG_RIGHT_TOP = 3;
	public static final int NEWS_POSITION_FLAG_LEFT_BOTTOM = 4;
	public static final int NEWS_POSITION_FLAG_RIGHT_BOTTOM = 5;
	
	private long info_id;
	private int show_order;
	private String main_title;
	private String url_key;
	private int start_count;
	private String news_from;
	private String image_url;
	private String thumb_url;
	private String link_url;
	private long start_time;
	private long end_time;
	private String show_date;
	private String sub_title;
	private String pfv_url;
	
	private String desc_title;
	private int position_flag;
	private int page_no;

	public News() {
	}

	private News(Parcel in) {
		info_id = ParcelUtil.readLongFromParcel(in);
		show_order = ParcelUtil.readIntFromParcel(in);
		main_title = ParcelUtil.readStringFromParcel(in);
		url_key = ParcelUtil.readStringFromParcel(in);
		start_count = ParcelUtil.readIntFromParcel(in);
		news_from = ParcelUtil.readStringFromParcel(in);
		image_url = ParcelUtil.readStringFromParcel(in);
		thumb_url = ParcelUtil.readStringFromParcel(in);
		link_url = ParcelUtil.readStringFromParcel(in);
		start_time = ParcelUtil.readLongFromParcel(in);
		end_time = ParcelUtil.readLongFromParcel(in);
		show_date = ParcelUtil.readStringFromParcel(in);
		sub_title = ParcelUtil.readStringFromParcel(in);
		pfv_url = ParcelUtil.readStringFromParcel(in);
	
		desc_title = ParcelUtil.readStringFromParcel(in);
		position_flag = ParcelUtil.readIntFromParcel(in);
		page_no = ParcelUtil.readIntFromParcel(in);
	}

	public long getInfo_id() {
		return info_id;
	}

	public void setInfo_id(long info_id) {
		this.info_id = info_id;
	}

	public int getShow_order() {
		return show_order;
	}

	public void setShow_order(int show_order) {
		this.show_order = show_order;
	}

	public String getMain_title() {
		return main_title;
	}

	public void setMain_title(String main_title) {
		this.main_title = main_title;
	}

	public String getUrl_key() {
		return url_key;
	}

	public void setUrl_key(String url_key) {
		this.url_key = url_key;
	}

	public int getStart_count() {
		return start_count;
	}

	public void setStart_count(int start_count) {
		this.start_count = start_count;
	}

	public String getNews_from() {
		return news_from;
	}

	public void setNews_from(String news_from) {
		this.news_from = news_from;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public String getShow_date() {
		return show_date;
	}

	public void setShow_date(String show_date) {
		this.show_date = show_date;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}

	public String getPfv_url() {
		return pfv_url;
	}

	public void setPfv_url(String pfv_url) {
		this.pfv_url = pfv_url;
	}

	public String getDesc_title() {
		return desc_title;
	}

	public void setDesc_title(String desc_title) {
		this.desc_title = desc_title;
	}

	public int getPosition_flag() {
		return position_flag;
	}

	public void setPosition_flag(String position) {
		if (position.equals("top")) {
			position_flag = NEWS_POSITION_FLAG_TOP;
		} else if (position.equals("left_top")) {
			position_flag = NEWS_POSITION_FLAG_LEFT_TOP;
		} else if (position.equals("right_top")) {
			position_flag = NEWS_POSITION_FLAG_RIGHT_TOP;
		} else if (position.equals("left_bottom")) {
			position_flag = NEWS_POSITION_FLAG_LEFT_BOTTOM;
		} else if (position.equals("right_bottom")) {
			position_flag = NEWS_POSITION_FLAG_RIGHT_BOTTOM;
		} 
	}

	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof News){
			News obj=(News)o;
			if(obj.info_id==this.info_id){
				return true;
			}
		}
		return false;
	}
	
	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
		public News createFromParcel(Parcel in) {
			return new News(in);
		}

		public News[] newArray(int size) {
			return new News[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtil.writeLongToParcel(out, info_id);
		ParcelUtil.writeIntToParcel(out, show_order);
		ParcelUtil.writeStringToParcel(out, main_title);
		ParcelUtil.writeStringToParcel(out, url_key);
		ParcelUtil.writeIntToParcel(out, start_count);
		ParcelUtil.writeStringToParcel(out, news_from);
		ParcelUtil.writeStringToParcel(out, image_url);
		ParcelUtil.writeStringToParcel(out, thumb_url);
		ParcelUtil.writeStringToParcel(out, link_url);
		ParcelUtil.writeLongToParcel(out, start_time);
		ParcelUtil.writeLongToParcel(out, end_time);
		ParcelUtil.writeStringToParcel(out, show_date);
		ParcelUtil.writeStringToParcel(out, sub_title);
		ParcelUtil.writeStringToParcel(out, pfv_url);
		
		ParcelUtil.writeStringToParcel(out, desc_title);
		ParcelUtil.writeIntToParcel(out, position_flag);
		ParcelUtil.writeIntToParcel(out, page_no);
	}

	
	public void writeToOutputStream(DataOutputStream out) throws IOException {
		CacheUtil.writeLong(out, info_id);
		CacheUtil.writeInt(out, show_order);
		CacheUtil.writeString(out, main_title);
		CacheUtil.writeString(out, url_key);
		CacheUtil.writeInt(out, start_count);
		CacheUtil.writeString(out, news_from);
		CacheUtil.writeString(out, image_url);
		CacheUtil.writeString(out, thumb_url);
		CacheUtil.writeString(out, link_url);
		CacheUtil.writeLong(out, start_time);
		CacheUtil.writeLong(out, end_time);
		CacheUtil.writeString(out, show_date);
		CacheUtil.writeString(out, sub_title);
		CacheUtil.writeString(out, pfv_url);
		
		CacheUtil.writeString(out, desc_title);
		CacheUtil.writeInt(out, position_flag);
		CacheUtil.writeInt(out, page_no);
	}
	
	public News readFromInputStream(DataInputStream in) throws IOException {
		info_id = CacheUtil.readLong(in);
		show_order = CacheUtil.readInt(in);
		main_title = CacheUtil.readString(in);
		url_key = CacheUtil.readString(in);
		start_count = CacheUtil.readInt(in);
		news_from = CacheUtil.readString(in);
		image_url = CacheUtil.readString(in);
		thumb_url = CacheUtil.readString(in);
		link_url = CacheUtil.readString(in);
		start_time = CacheUtil.readLong(in);
		end_time = CacheUtil.readLong(in);
		show_date = CacheUtil.readString(in);
		sub_title = CacheUtil.readString(in);
		pfv_url = CacheUtil.readString(in);
		
		desc_title = CacheUtil.readString(in);
		position_flag = CacheUtil.readInt(in);
		page_no = CacheUtil.readInt(in);
		return this;
	}
}
