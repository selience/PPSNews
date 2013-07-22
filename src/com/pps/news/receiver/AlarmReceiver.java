package com.pps.news.receiver;

import com.pps.news.AlarmAlertActivity;
import com.pps.news.bean.Alarm;
import com.pps.news.constant.Constants;
import com.pps.news.service.AlarmService;
import com.pps.news.util.DateUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Alarm alarm = null;
		final byte[] data = intent.getByteArrayExtra(Constants.ALARM_EXTRAS);
		if (data != null) {
			Parcel in = Parcel.obtain();
			in.unmarshall(data, 0, data.length);
			in.setDataPosition(0);
			alarm = Alarm.CREATOR.createFromParcel(in);
		}

		if (alarm == null)
			return;

		Intent alarmAlert = new Intent(context, AlarmAlertActivity.class);
		alarmAlert.putExtra(Constants.ALARM_EXTRAS, alarm);
		alarmAlert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		context.startActivity(alarmAlert);

		if (!alarm.daysOfWeek.isRepeatSet()) {
			DateUtils.enableAlarm(context, alarm, false);
		} else {
			DateUtils.setNextAlert(context);
		}
		
		// 震动或播放声音
		Intent playAlarm = new Intent(context, AlarmService.class);
        playAlarm.putExtra(Constants.ALARM_INTENT_EXTRA, alarm);
        context.startService(playAlarm);
	}

}
