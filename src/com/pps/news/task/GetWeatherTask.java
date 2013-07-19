package com.pps.news.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import com.pps.news.bean.Result;
import com.pps.news.bean.Weather;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.ParseWeatherJson;
import com.pps.news.util.UIUtil;

public class GetWeatherTask extends GenericTask {

	public GetWeatherTask(TaskListener taskListener, String taskName) {
		super(taskListener, taskName);
	}

	@Override
	protected Result doInBackground(String... params) {
		BetterHttp httpClient = BetterHttp.getInstance();
		Result result = httpClient.doHttpGet(getWeatherUrl(params[0]));
		ParseWeatherJson parseJson = new ParseWeatherJson();
		Weather weather = parseJson.parseJson(result.getMessage());
		result.addResult(weather);
		return result;
	}

	@SuppressLint("SimpleDateFormat")
	private String getWeatherUrl(String city) {
		if (city != null && !city.equals("")) {
			StringBuilder sb = new StringBuilder(city);
			if (city.contains("市")) {
				int position = sb.indexOf("市");
				sb.deleteCharAt(position);
			}
			Date date = new Date();
			SimpleDateFormat month = new SimpleDateFormat("MM");
			SimpleDateFormat day = new SimpleDateFormat("dd");
			return "http://vodguide2.pps.tv/cache/weather2/"
					+ UIUtil.stringToUTF(sb.toString())
					+ month.format(date.getTime()) + day.format(date.getTime())
					+ ".txt";
		}
		return null;
	}
}
