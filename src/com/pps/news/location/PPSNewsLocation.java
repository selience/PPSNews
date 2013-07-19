package com.pps.news.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.pps.news.app.NewsApplication;
import com.pps.news.util.Log;

/**
 * @file PPSNewsLocation.java
 * @create 2013-7-18 下午05:55:38
 * @author lilong http://developer.baidu.com/map/geosdk-android-classv3.1.htm
 * @description TODO 定位操作
 */
public class PPSNewsLocation {

	private LocationManager locationManager = null;
	private LocationClient mLocationClient;
	private static PPSNewsLocation location;
	private Context context;
	public static BDLocation myLocation = new BDLocation();

	public static PPSNewsLocation getInstance() {
		if (location == null) {
			location = new PPSNewsLocation(NewsApplication.getInstance());
		}
		return location;
	}

	private PPSNewsLocation(Context c) {
		this.context = c;
		myLocation.setLatitude(0);
		myLocation.setLongitude(0);
		myLocation.setAddrStr("");
	}

	/**
	 * 获取坐标 首先使用百度定位
	 * 
	 * @return
	 */
	public void getLocation() {
		if(context!=null)
		{
			Log.v("location","百度定位Start+context:"+context);
			getLocationByBaidu();
		}
	}

	/**
	 * 通过百度定位获取经纬度
	 */
	private void getLocationByBaidu() {
		mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setProdName("pps");
		option.setCoorType("bd09ll"); //返回的定位结果是百度经纬度,默认值gcj02
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		option.setProdName("ppsNews"); // 设置产品线名称
		option.setAddrType("all"); //返回的定位结果包含地址信息
		option.setScanSpan(300000); // 定时定位，低于1000毫秒，只定位一次。
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location.getLocType() == 65 || location.getLocType() == 161
						|| location.getLocType() == 61) {
					myLocation = location;
					Log.v("location",
							"baidu:" + location.getLongitude() + ":"
									+ location.getLatitude() + "addr:"
									+ location.getAddrStr());
				} else {
					Log.v("location","Google定位Start");
					getLocationByGoogle();
				}
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {

			}
		});
		mLocationClient.start();
	}

	/**
	 * 根据谷歌获取经纬度
	 * 
	 * @return
	 */
	private void getLocationByGoogle() {
		Location location = null;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过网络获取
		String provider = LocationManager.NETWORK_PROVIDER;
		if (provider != null) {
			location = locationManager.getLastKnownLocation(provider);
		}
		if (myLocation == null) {
			// 网络没有获取到，通过GPS获取
			provider = LocationManager.GPS_PROVIDER;
			location = locationManager.getLastKnownLocation(provider);
		}
		if (myLocation == null) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			provider = locationManager.getBestProvider(criteria, true);
			if (provider != null) {
				location = locationManager.getLastKnownLocation(provider);
			}
		}
		if (location != null) {
			myLocation.setLatitude(location.getLatitude());
			myLocation.setLongitude(location.getLongitude());
			Log.v("location", "google:" + location.getLongitude() + ":"
					+ location.getLatitude());
			myLocation.setAddrStr(getAddressByGoogle(location));
		} else {
			myLocation.setLatitude(0);
			myLocation.setLongitude(0);
			myLocation.setAddrStr("");
			Log.v("location",
					"error:" + myLocation.getLongitude() + ":"
							+ myLocation.getLatitude() + "addr:"
							+ myLocation.getAddrStr());
		}
	}

	/**
	 * 通过经纬度到Google map上获取地理位置
	 * 
	 * @param itude
	 * @return
	 * @throws Exception
	 */
	private static String getAddressByGoogle(Location location) {
		String resultString = "";
		String urlString = String.format(
				"http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s",
				location.getLatitude(), location.getLongitude());
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(urlString);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();
			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark")
						.toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString(
							"address");
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			get.abort();
			client = null;
		}
		Log.v("location", "addr:" + resultString);
		return resultString;
	}
}
