package com.pps.news.weather;

import com.pps.news.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeatherCityAdapter extends BaseAdapter {
	private String str = "北京,上海,广州,深圳,武汉,南京,西安,成都,郑州,杭州,东莞,重庆,长沙,天津,苏州,沈阳,福州,无锡,哈尔滨,厦门,"
			+ "石家庄,合肥,南昌,济南,佛山,大连,常州,太原,青岛,南宁,长春,昆明,兰州,宁波,汕头";
	private String[] data;
	private Context context;

	public WeatherCityAdapter(Context context) {
		data = str.split(",");
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.weather_city_listview_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.weather_city_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.name.setText(data[position]);
		return convertView;
	}

	private final class Holder {
		TextView name;
	}

}
