package com.pps.news.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;

public class NewsParser extends Parser<News> {

	@Override
	public News parse(JSONObject json) throws JSONException {
		News news = new News();
		news.setInfo_id(json.optLong("info_id"));
		news.setShow_order(json.optInt("show_order"));
		news.setMain_title(json.optString("main_title"));
		news.setUrl_key(json.optString("url_key"));
		news.setStart_count(json.optInt("start_count"));
		news.setNews_from(json.optString("news_from"));
		news.setImage_url(json.optString("image_url"));
		news.setThumb_url(json.optString("thumb_url"));
		news.setLink_url(json.optString("link_url"));
		news.setStart_time(json.optLong("start_time"));
		news.setEnd_time(json.optLong("end_time"));
		news.setShow_date(json.optString("show_date"));
		news.setSub_title(json.optString("sub_title"));
		news.setPfv_url(json.optString("pfv_url"));

		news.setDesc_title(json.optString("desc_title"));
		news.setPosition_flag(json.optString("position_flag"));
		news.setPage_no(json.optInt("page_no"));
		return news;
	}

	@Override
	public Group<News> parse(JSONArray array) throws JSONException {
		return new GroupParser<News>(this).parse(array);
	}
	
	public Group<News> parse(String json) {
		try {
			if (json != null) {
				JSONArray array = new JSONArray(json);
				if (array.length() > 0) {
					return parse(array);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
