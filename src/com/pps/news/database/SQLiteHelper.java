package com.pps.news.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @file SQLiteHelper.java
 * @create 2013-6-27 下午06:32:09
 * @author lilong
 * @description TODO
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = ".news";
	private static final int DATABASE_VERSION = 1;

	public static final String ALARM_TABLE = "t_alarm";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_ALARM_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DELETE FROM " + ALARM_TABLE);
		db.execSQL(CREATE_TABLE_ALARM_SQL);
	}

	private final String CREATE_TABLE_ALARM_SQL = 
		"CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + "(" + 
			AlarmColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			AlarmColumns.HOUR + " INTEGER," +
			AlarmColumns.MINUTE + " INTEGER," +
			AlarmColumns.WEEK + " TEXT," + 
			AlarmColumns.ENABLE + " INTEGER," +
			AlarmColumns.VIBRATE + " INTEGER," +
			AlarmColumns.RINGTONE + " TEXT" +
		")";
	
	
	public class AlarmColumns {
		
		public static final String ID = "id";
		
		public static final String HOUR = "hour";
		
		public static final String MINUTE = "minute";
		
		public static final String WEEK = "week";
		
		public static final String ENABLE = "enable";
		
		public static final String VIBRATE = "vibrate";
		
		public static final String RINGTONE = "ringtone";
	}

}
