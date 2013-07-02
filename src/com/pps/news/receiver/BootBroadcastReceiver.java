package com.pps.news.receiver;

import com.pps.news.common.CommonOperate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			CommonOperate.startAlarmService(context);
		}
	}

}
