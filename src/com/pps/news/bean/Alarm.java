package com.pps.news.bean;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * @file Alarm.java
 * @create 2013-7-9 上午10:39:39
 * @author lilong
 * @description TODO
 */
public class Alarm implements Parcelable {

	private static final String ALARM_ALERT_SILENT = "silent";
	
	public int id;
	public boolean enabled;
	public int hour;
	public int minutes;
	public DaysOfWeek daysOfWeek;
	public long time;
	public boolean vibrate;
	public String label;
	public Uri alert;
	public boolean silent;

	public Alarm() {
		label = "";
		daysOfWeek = new DaysOfWeek(0);
		alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	}
	
	public Alarm(Cursor c) {
		id = c.getInt(Columns.ALARM_ID_INDEX);
		enabled = c.getInt(Columns.ALARM_ENABLED_INDEX) == 1;
		hour = c.getInt(Columns.ALARM_HOUR_INDEX);
		minutes = c.getInt(Columns.ALARM_MINUTES_INDEX);
		daysOfWeek = new DaysOfWeek(c.getInt(Columns.ALARM_DAYS_OF_WEEK_INDEX));
		time = c.getLong(Columns.ALARM_TIME_INDEX);
		vibrate = c.getInt(Columns.ALARM_VIBRATE_INDEX) == 1;
		label = c.getString(Columns.ALARM_MESSAGE_INDEX);
		String alertString = c.getString(Columns.ALARM_ALERT_INDEX);
		if (ALARM_ALERT_SILENT.equals(alertString)) {
			silent = true;
		} else {
			if (alertString != null && alertString.length() != 0) {
				alert = Uri.parse(alertString);
			}
			if (alert == null) {
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			}
		}
	}

	public Alarm(Parcel p) {
		id = p.readInt();
		enabled = p.readInt() == 1;
		hour = p.readInt();
		minutes = p.readInt();
		daysOfWeek = new DaysOfWeek(p.readInt());
		time = p.readLong();
		vibrate = p.readInt() == 1;
		label = p.readString();
		alert = (Uri) p.readParcelable(null);
		silent = p.readInt() == 1;
	}

	public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
		public Alarm createFromParcel(Parcel p) {
			return new Alarm(p);
		}

		public Alarm[] newArray(int size) {
			return new Alarm[size];
		}
	};

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel p, int flags) {
		p.writeInt(id);
		p.writeInt(enabled ? 1 : 0);
		p.writeInt(hour);
		p.writeInt(minutes);
		p.writeInt(daysOfWeek.getCoded());
		p.writeLong(time);
		p.writeInt(vibrate ? 1 : 0);
		p.writeString(label);
		p.writeParcelable(alert, flags);
		p.writeInt(silent ? 1 : 0);
	}

	public static class Columns implements BaseColumns {

		/**
		 * Hour in 24-hour localtime 0 - 23.
		 */
		public static final String HOUR = "hour";

		/**
		 * Minutes in localtime 0 - 59
		 */
		public static final String MINUTES = "minutes";

		/**
		 * Days of week coded as integer
		 */
		public static final String DAYS_OF_WEEK = "daysofweek";

		/**
		 * Alarm time in UTC milliseconds from the epoch.
		 */
		public static final String ALARM_TIME = "alarmtime";

		/**
		 * True if alarm is active
		 */
		public static final String ENABLED = "enabled";

		/**
		 * True if alarm should vibrate
		 */
		public static final String VIBRATE = "vibrate";

		/**
		 * Message to show when alarm triggers
		 */
		public static final String MESSAGE = "message";

		/**
		 * Audio alert to play when alarm triggers
		 */
		public static final String ALERT = "alert";

		public static final int ALARM_ID_INDEX = 0;
		public static final int ALARM_HOUR_INDEX = 1;
		public static final int ALARM_MINUTES_INDEX = 2;
		public static final int ALARM_DAYS_OF_WEEK_INDEX = 3;
		public static final int ALARM_TIME_INDEX = 4;
		public static final int ALARM_ENABLED_INDEX = 5;
		public static final int ALARM_VIBRATE_INDEX = 6;
		public static final int ALARM_MESSAGE_INDEX = 7;
		public static final int ALARM_ALERT_INDEX = 8;
	}
}
