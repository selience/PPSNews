package com.pps.news.weather;

import com.pps.news.R;
import com.pps.news.app.BaseActivity;
import com.pps.news.location.PPSNewsLocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WeatherCityActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	private String city;
	private TextView titleName;
	private ListView listView;
	private WeatherCityAdapter adapter;
	private TextView dingweiCity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_city_main);
		initViewId();
		
		city = PPSNewsLocation.myLocation.getCity();
		if (city == null || city.equals("")) {
			city = "上海";
			dingweiCity.setText(city);
		} else {
			StringBuilder sb = new StringBuilder(city);
			if (city.contains("市")) {
				int position = sb.indexOf("市");
				sb.deleteCharAt(position);
			}
			city = sb.toString();
			dingweiCity.setText(city);
		}
		
		titleName.setText(R.string.weather_title_tv);
		adapter = new WeatherCityAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void initViewId() {
		findViewById(R.id.icon).setVisibility(View.GONE);
		((TextView)findViewById(R.id.summary)).setText("City");
		titleName = (TextView) this.findViewById(R.id.title);
		listView = (ListView) this.findViewById(R.id.weather_city_listview);
		dingweiCity = (TextView) this.findViewById(R.id.weather_city_tv);
		dingweiCity.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.city = parent.getItemAtPosition(position).toString();
		selectedItem();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weather_city_tv:
			selectedItem();
			break;
		}
	}

	private void selectedItem() {
		Intent intent = new Intent(this, WeatherActivity.class);
		intent.putExtra("city", city);
		setResult(RESULT_OK, intent);
		finish();
	}
}
