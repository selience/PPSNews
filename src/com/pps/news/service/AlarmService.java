package com.pps.news.service;

import java.util.Calendar;
import java.util.List;

import com.pps.news.bean.AlarmModel;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class AlarmService extends IntentService {

	//开启一个单独的工作线程，并且在任务完成时自动终止服务
	private Calendar c;
	private AlarmManager alarmManager;
	
	public AlarmService() {
		this("Alarm-Thread");
	}
	
	public AlarmService(String name) {
		super(name);
		setIntentRedelivery(true);
		c = Calendar.getInstance();
		//alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("onHandleIntent::"+c.get(Calendar.DAY_OF_WEEK));
		//		List<AlarmModel> data = null;
//		for (int i=0;i<data.size();i++) {
//			AlarmModel model = data.get(i);
//			c.clear();
//		}
	}
}
