package com.pps.news.receiver;

import com.pps.news.util.DateUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmInitReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			DateUtils.disableExpiredAlarms(context);
		}
		DateUtils.setNextAlert(context);
	}

}
