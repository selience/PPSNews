package com.pps.news.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.pps.news.NewsActivity;
import com.pps.news.R;
import com.pps.news.common.AlarmAlertWakeLock;

public class AlarmService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		AlarmAlertWakeLock.acquireCpuWakeLock(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int notifyId = 0x100;
		Intent notificationIntent = new Intent(this, NewsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, notifyId,
				notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationManager nm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.icon)
				.setTicker("Local Notification Ticker")
				.setWhen(System.currentTimeMillis()).setAutoCancel(true)
				.setVibrate(new long[] { 0, 100, 200, 300 })
				.setContentTitle("Local Notification")
				.setContentText("This is content text.");
		Notification n = builder.getNotification();
		nm.notify(notifyId, n);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AlarmAlertWakeLock.releaseCpuLock();
	}

}
