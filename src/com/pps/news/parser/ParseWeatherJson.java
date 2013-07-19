package com.pps.news.parser;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pps.news.bean.Weather;

public class ParseWeatherJson {

	@SuppressLint("SimpleDateFormat")
	public Weather parseJson(String result) {
		if (result == null || result.equals("")) {
			return null;
		}
		try {
			Weather weather = new Weather();
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.has("temp1")) {
				String temp1Str = jsonObject.getString("temp1");
				JSONArray jaOne = new JSONArray(temp1Str);
				if (jaOne.length() >= 2) {
					String high = getFilterString(jaOne.getString(0));
					String low = jaOne.getString(1);
					weather.setTempInfo(high + "/" + low);
				}
			}
			if (jsonObject.has("temp2")) {
				String temp2Str = jsonObject.getString("temp2");
				JSONArray jaTwo = new JSONArray(temp2Str);
				if (jaTwo.length() >= 2) {
					String high = getFilterString(jaTwo.getString(0));
					String low = getFilterString(jaTwo.getString(1));
					weather.setTempOneTemp(high + "°~" + low + "°");
				}
			}
			if (jsonObject.has("temp3")) {
				String temp3Str = jsonObject.getString("temp3");
				JSONArray jaThird = new JSONArray(temp3Str);
				if (jaThird.length() >= 2) {
					String high = getFilterString(jaThird.getString(0));
					String low = getFilterString(jaThird.getString(1));
					weather.setTempTwoTemp(high + "°~" + low + "°");
				}
			}

			if (jsonObject.has("temp4")) {
				String temp4Str = jsonObject.getString("temp4");
				JSONArray jaFour = new JSONArray(temp4Str);
				if (jaFour.length() >= 2) {
					String high = getFilterString(jaFour.getString(0));
					String low = getFilterString(jaFour.getString(1));
					weather.setTempThirdTemp(high + "°~" + low + "°");
				}
			}
			Date date = new Date();
			SimpleDateFormat month = new SimpleDateFormat("MM");
			SimpleDateFormat day = new SimpleDateFormat("dd");
			String m = month.format(date.getTime());
			String d = day.format(date.getTime());
			weather.setTime(m + "/" + d);
			if (jsonObject.has("weather1")) {
				weather.setInfo(jsonObject.getString("weather1"));
			}
			if (jsonObject.has("weather2")) {
				weather.setTempOneInfo(jsonObject.getString("weather2"));
			}

			if (jsonObject.has("weather3")) {
				weather.setTempTwoInfo(jsonObject.getString("weather3"));
			}

			if (jsonObject.has("weather4")) {
				weather.setTempThirdInfo(jsonObject.getString("weather4"));
			}
			if (jsonObject.has("pm2_5")) {
				weather.setPm(jsonObject.getString("pm2_5"));
			}

			if (jsonObject.has("quality")) {
				weather.setPmInfo(jsonObject.getString("quality"));
			}

			if (jsonObject.has("img1")) {
				weather.setImg(jsonObject.getString("img1"));
			}

			if (jsonObject.has("img3")) {
				weather.setTempOneImg(jsonObject.getString("img3"));
			}

			if (jsonObject.has("img5")) {
				weather.setTempTwoImg(jsonObject.getString("img5"));
			}

			if (jsonObject.has("img7")) {
				weather.setTempThirdImg(jsonObject.getString("img7"));
			}
			return weather;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getFilterString(String content) {
		if (content != null && !content.equals("")) {
			if (content.contains("℃")) {
				StringBuilder sb = new StringBuilder(content);
				int position = sb.indexOf("℃");
				sb.deleteCharAt(position);
				return sb.toString();
			}
		}
		return null;
	}
}
