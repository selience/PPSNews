package com.pps.news.common;

import android.content.Context;
import android.content.Intent;
import com.pps.news.service.AlarmService;

public final class CommonOperate {

	private static final String ACTION_ALARM_SERVICE = "com.pps.news.intent.AlarmService";
	
	/** 启动闹钟服务   */
	public static void startAlarmService(Context context) {
		Intent mIntent = new Intent(context, AlarmService.class);
		mIntent.setAction(ACTION_ALARM_SERVICE);
		context.startService(mIntent);
	}
	
}
