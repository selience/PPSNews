package com.pps.news.service;

import java.util.Calendar;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.pps.news.NewsActivity;
import com.pps.news.R;
import com.pps.news.database.AlarmHelper;

public class AlarmService extends Service {

	private Calendar c;
	private AlarmHelper helper;
	private AlarmManager alarmManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		c = Calendar.getInstance();
//		helper = new AlarmHelper(getBaseContext());
//		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int notifyId = 0x100;
		Intent notificationIntent = new Intent(this, NewsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notifyId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
        	   .setContentIntent(contentIntent)
			   .setSmallIcon(R.drawable.icon)
			   .setTicker("Local Notification Ticker")
			   .setWhen(System.currentTimeMillis())
			   .setAutoCancel(true)
			   .setVibrate(new long[]{0, 100, 200, 300})
			   .setContentTitle("Local Notification")
			   .setContentText("This is content text.");
        Notification n = builder.getNotification();
        nm.notify(notifyId, n);
		
		/*List<AlarmModel> data = helper.list();
		if (data!=null && data.size()>0) {
			c.clear();
			for (AlarmModel model : data) {
				String[] weeks = model.getWeek().split(",");
				for (String item : weeks) {
					c.set(Calendar.DAY_OF_WEEK, Integer.valueOf(item));
					c.set(Calendar.HOUR_OF_DAY, model.getHour());
					c.set(Calendar.MINUTE, model.getMinute());
				}
				
				alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), null);
			}
		}*/
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	
}
