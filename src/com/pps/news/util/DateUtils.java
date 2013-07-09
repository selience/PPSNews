package com.pps.news.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import com.pps.news.bean.Alarm;
import com.pps.news.bean.Alarm.Columns;
import com.pps.news.bean.DaysOfWeek;
import com.pps.news.constant.Constants;
import com.pps.news.database.DatabaseHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.text.format.DateFormat;

public final class DateUtils {

	private DateUtils() {
	}

	private final static String M12 = "h:mm";
	private final static String M24 = "kk:mm";

	public static String formatTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? M24 : M12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	static boolean get24HourMode(final Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}

	/**
	 * Given an alarm in hours and minutes, return a time suitable for setting
	 * in AlarmManager.
	 * 
	 * @param hour
	 *            Always in 24 hour 0-23
	 * @param minute
	 *            0-59
	 * @param daysOfWeek
	 *            0-59
	 */
	public static Calendar calculateAlarm(int hour, int minute,
			DaysOfWeek daysOfWeek) {

		// start with now
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		// if alarm is behind current time, advance one day
		if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int addDays = daysOfWeek.getNextAlarm(c);
		if (addDays > 0)
			c.add(Calendar.DAY_OF_WEEK, addDays);
		return c;
	}

	public static void disableExpiredAlarms(final Context context) {
		DatabaseHelper helper = new DatabaseHelper(context);
		List<Alarm> data = helper.query(Columns.ENABLED + "=1");
		long now = System.currentTimeMillis();
		if (data != null) {
			for (Alarm alarm : data) {
				if (alarm.time != 0 && alarm.time < now) {
					enableAlarmInternal(context, alarm, false);
				}
			}
		}
	}

	public static Alarm calculateNextAlert(final Context context) {
		Alarm alarm = null;
		long minTime = Long.MAX_VALUE;
		long now = System.currentTimeMillis();
		DatabaseHelper helper = new DatabaseHelper(context);
		List<Alarm> data = helper.query(Columns.ENABLED + "=1");
		if (data != null) {
			Iterator<Alarm> iterator = data.iterator();
			while (iterator.hasNext()) {
				Alarm a = iterator.next();
				if (a.time == 0) {
					a.time = calculateAlarm(a.hour, a.minutes, a.daysOfWeek)
							.getTimeInMillis();
				} else if (a.time < now) {
					// Expired alarm, disable it and move along.
					enableAlarmInternal(context, a, false);
					continue;
				}
				if (a.time < minTime) {
					minTime = a.time;
					alarm = a;
				}
			}
		}
		return alarm;
	}

	public static void enableAlarm(final Context context, final Alarm alarm,
			boolean enabled) {
		enableAlarmInternal(context, alarm, enabled);
		setNextAlert(context);
	}

	private static void enableAlarmInternal(final Context context,
			final Alarm alarm, boolean enabled) {
		ContentValues values = new ContentValues(2);
		values.put(Alarm.Columns.ENABLED, enabled ? 1 : 0);
		if (enabled) {
			long time = 0;
			if (!alarm.daysOfWeek.isRepeatSet()) {
				time = calculateAlarm(alarm.hour, alarm.minutes,
						alarm.daysOfWeek).getTimeInMillis();
			}
			values.put(Alarm.Columns.ALARM_TIME, time);
		}
		DatabaseHelper helper = new DatabaseHelper(context);
		helper.update(values, alarm.id);
	}

	/**
	 * 系统启动，闹钟设置，时间改变调用此方法启动闹钟
	 */
	public static void setNextAlert(final Context context) {
		Alarm alarm = calculateNextAlert(context);
		if (alarm != null) {
			enableAlert(context, alarm, alarm.time);
		} else {
			disableAlert(context);
		}
	}

	private static void enableAlert(Context context, final Alarm alarm,
			final long atTimeInMillis) {
		Intent intent = new Intent(Constants.ALARM_ALERT_ACTION);
		Parcel out = Parcel.obtain();
		alarm.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(Constants.ALARM_EXTRAS, out.marshall());
		
		AlarmManager am = (AlarmManager) context
		.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
	}

	private static void disableAlert(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0,
				new Intent(Constants.ALARM_ALERT_ACTION),
				PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}

}
