package com.pps.news.weather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import com.pps.news.R;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Result;
import com.pps.news.bean.Weather;
import com.pps.news.location.PPSNewsLocation;
import com.pps.news.task.GetWeatherTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ImageCache;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends BaseActivity implements TaskListener, Observer, OnClickListener {
	private static final String GET_WEATHER_TASK = "GET_WEATHER_TASK";
	
	private TextView titleName, pmTv;
	private TextView weatherInfo, weatherTemp, weatherTime, weatherPm, weatherPmInfo;
	private ImageView weatherImage, weatherOne, weatherTwo, weatherThird;
	private ImageView weatherBg;
	private TextView weatherOneTv, weatherOneTmep, weatherOneInfo;
	private TextView weatherTwoTv, weatherTwoTmep, weatherTwoInfo;
	private TextView weatherThirdTv, weatherThirdTmep, weatherThirdInfo;
	private ImageView weatherRefresh, weatherLocation;
	private LinearLayout weatherBottomLayout;

	private String city;
	private ImageCache imageFetcher;
	private Map<String, ImageView> mPhotoMaps;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		initViewId();

		city = getIntent().getStringExtra("city");
		if (city == null) {
			city = PPSNewsLocation.myLocation.getCity();
			if (city == null || city.equals("")) {
				city = "上海";
			}
		}
		titleName.setText(city + "天气");
		
		mPhotoMaps = new WeakHashMap<String, ImageView>();
		imageFetcher = ImageCache.getInstance();
		imageFetcher.addObserver(this);
		
		Weather weather = CacheUtil.getWeatherCache();
		if (weather != null) {
			getDataOk(weather);
		} else {
			getDataError();
		}
		new GetWeatherTask(this, GET_WEATHER_TASK).execute(city);
	}

	public void initViewId() {
		titleName = (TextView) this.findViewById(R.id.weather_title);
		weatherInfo = (TextView) this.findViewById(R.id.weather_info);
		weatherTemp = (TextView) this.findViewById(R.id.weather_temper);
		weatherTime = (TextView) this.findViewById(R.id.weather_time);
		weatherPm = (TextView) this.findViewById(R.id.weather_pm);
		weatherPmInfo = (TextView) this.findViewById(R.id.weather_pm_info);
		weatherImage = (ImageView) this.findViewById(R.id.weather_image_big);
		weatherOne = (ImageView) this.findViewById(R.id.weather_one_image);
		weatherTwo = (ImageView) this.findViewById(R.id.weather_two_image);
		weatherThird = (ImageView) this.findViewById(R.id.weather_third_image);
		weatherOneTv = (TextView) this.findViewById(R.id.weather_one_tv);
		weatherOneTmep = (TextView) this.findViewById(R.id.weather_one_temperature);
		weatherTwoTv = (TextView) this.findViewById(R.id.weather_two_tv);
		weatherTwoTmep = (TextView) this.findViewById(R.id.weather_two_temperature);
		weatherThirdTv = (TextView) this.findViewById(R.id.weather_third_tv);
		weatherThirdTmep = (TextView) this.findViewById(R.id.weather_third_temperature);
		weatherBg = (ImageView) this.findViewById(R.id.weather_bg_imageview);
		pmTv = (TextView) this.findViewById(R.id.weather_pm_tv);
		weatherBottomLayout = (LinearLayout) this.findViewById(R.id.wather_bottom_layout);
		weatherOneInfo = (TextView) this.findViewById(R.id.weather_one_info);
		weatherTwoInfo = (TextView) this.findViewById(R.id.weather_two_info);
		weatherThirdInfo = (TextView) this.findViewById(R.id.weather_third_info);
		weatherRefresh = (ImageView) findViewById(R.id.weather_refresh);
		weatherLocation = (ImageView) findViewById(R.id.weather_location);
		weatherRefresh.setOnClickListener(this);
		weatherLocation.setOnClickListener(this);
	}

	@SuppressLint("SimpleDateFormat")
	private void initMessage() {
		Date date = new Date();
		DateFormat week = new SimpleDateFormat("EEEE");
		String one = week.format(date.getTime() + 86400000L);
		String two = week.format(date.getTime() + 86400000L * 2);
		String third = week.format(date.getTime() + 86400000L * 3);
		weatherOneTv.setText(one);
		weatherTwoTv.setText(two);
		weatherThirdTv.setText(third);
	}

	private void getDataOk(Weather weather) {
		if (weather != null) {
			initMessage();
			weatherInfo.setVisibility(View.VISIBLE);
			weatherTemp.setVisibility(View.VISIBLE);
			weatherTime.setVisibility(View.VISIBLE);
			weatherPm.setVisibility(View.VISIBLE);
			weatherPmInfo.setVisibility(View.VISIBLE);
			weatherImage.setVisibility(View.VISIBLE);
			pmTv.setVisibility(View.VISIBLE);
			weatherInfo.setText(weather.getInfo());
			weatherTemp.setText(weather.getTempInfo());
			weatherOneTmep.setText(weather.getTempOneTemp());
			weatherTwoTmep.setText(weather.getTempTwoTemp());
			weatherThirdTmep.setText(weather.getTempThirdTemp());
			weatherTime.setText(weather.getTime());
			weatherPm.setText(weather.getPm());
			weatherOneInfo.setText(weather.getTempOneInfo());
			weatherTwoInfo.setText(weather.getTempTwoInfo());
			weatherThirdInfo.setText(weather.getTempThirdInfo());
			String airQuality = getAirQuality(weather.getPm());
			if (airQuality != null) {
				weatherPmInfo.setText(airQuality);
			} else {
				weatherPmInfo.setVisibility(View.GONE);
			}
			setPhotos(getBigImgUrl(weather.getImg()), weatherImage);
			setPhotos(getWeatherBgUrl(weather.getImg()), weatherBg);
			setPhotos(getSmallImgUrl(weather.getTempOneImg()), weatherOne);
			setPhotos(getSmallImgUrl(weather.getTempTwoImg()), weatherTwo);
			setPhotos(getSmallImgUrl(weather.getTempThirdImg()), weatherThird);
			
			weatherBottomLayout.setVisibility(View.VISIBLE);
		} 
	}

	private void getDataError() {
		weatherBottomLayout.setVisibility(View.GONE);
		weatherInfo.setVisibility(View.GONE);
		weatherTemp.setVisibility(View.GONE);
		weatherTime.setVisibility(View.GONE);
		weatherPm.setVisibility(View.GONE);
		weatherPmInfo.setVisibility(View.GONE);
		pmTv.setVisibility(View.GONE);
		weatherImage.setVisibility(View.VISIBLE);
		weatherImage.setImageResource(R.drawable.weather_big_error);
	}

	private String getWeatherBgUrl(String temperature) {

		if (temperature != null && !temperature.equals("")) {
			String name = null;
			if (temperature.equals("1") || temperature.equals("31")) {
				name = "cloud.jpg";
			} else if (temperature.equals("18") || temperature.equals("20")
					|| temperature.equals("29") || temperature.equals("32")
					|| temperature.equals("35") || temperature.equals("36")) {
				name = "sand.jpg";
			} else if (temperature.equals("0")) {
				name = "sun.jpg";
			} else if (temperature.equals("5") || temperature.equals("6")
					|| temperature.equals("13") || temperature.equals("14")
					|| temperature.equals("15") || temperature.equals("16")
					|| temperature.equals("17") || temperature.equals("19")
					|| temperature.equals("26") || temperature.equals("27")
					|| temperature.equals("28") || temperature.equals("34")) {
				name = "snow.jpg";
			} else if (temperature.equals("3") || temperature.equals("4")
					|| temperature.equals("7") || temperature.equals("8")
					|| temperature.equals("9") || temperature.equals("10")
					|| temperature.equals("11") || temperature.equals("12")
					|| temperature.equals("21") || temperature.equals("22")
					|| temperature.equals("23") || temperature.equals("24")
					|| temperature.equals("25") || temperature.equals("33")) {
				name = "rain.jpg";
			} else if (temperature.equals("2") || temperature.equals("30")) {
				name = "cloudy.jpg";
			}
			return "http://image1.webscache.com/vodguide/style/img/mobile/background/"
					+ name + "?t=2";

		}
		return null;
	}

	// 返回空气质量
	private String getAirQuality(String pm) {
		boolean isGigital = Pattern.matches("[0-9]*", pm);
		if (!TextUtils.isEmpty(pm) && isGigital) {
			int result = Integer.parseInt(pm);
			if (result >= 0 && result <= 50) {
				return "优";
			} else if (result > 50 && result <= 100) {
				return "良";
			} else if (result > 100 && result <= 150) {
				return "轻度污染";
			} else if (result > 150 && result <= 200) {
				return "中度污染";
			} else if (result > 200 && result <= 300) {
				return "重度污染";
			} else {
				return "严重污染";
			}
		}
		return null;
	}

	@Override
	public void onTaskStart(String taskName) {
		weatherRefresh.startAnimation(AnimationUtils.loadAnimation(this, 
				R.anim.weather_refresh_anim));
	}

	@Override
	public void onTaskFinished(String taskName, Result result) {
		handlerException(result.getException());
		weatherRefresh.clearAnimation();
		if (taskName.equals(GET_WEATHER_TASK)) {
			if (result.getValue() != null) {
				Weather weather = (Weather) result.getValue();
				getDataOk(weather);
				CacheUtil.saveWeatherCache(weather);
			} 
		}
	}

	public static String getSmallImgUrl(String imgId) {
		return "http://image1.webscache.com/vodguide/style/img/mobile/android/w"
				+ imgId + ".png";
	}

	private String getBigImgUrl(String imgId) {
		return "http://image1.webscache.com/vodguide/style/img/mobile/2x/big/w"
				+ imgId + ".png";
	}

	
	private void setPhotos(String photoUrl, ImageView imageView) {
		if (imageFetcher.exists(photoUrl)) {
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			mPhotoMaps.put(photoUrl, imageView);
			imageFetcher.request(photoUrl);
		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		final String imageUrl = data.toString();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (String key : mPhotoMaps.keySet()) {
					if (key.equals(imageUrl)) {
						setPhotos(key, mPhotoMaps.get(key));
					}
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPhotoMaps.clear();
		imageFetcher.deleteObserver(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weather_location:
			startActivityForResult(new Intent(this, WeatherCityActivity.class), 0x100);
			break;
		case R.id.weather_refresh:
			new GetWeatherTask(this, GET_WEATHER_TASK).execute(city);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0x100:
			if (data == null) return;
			city = data.getStringExtra("city");
			titleName.setText(city + "天气");
			new GetWeatherTask(this, GET_WEATHER_TASK).execute(city);
			break;
		default:
			break;
		}
	}
}
