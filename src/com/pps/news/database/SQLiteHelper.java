package com.pps.news.database;

import com.pps.news.bean.Alarm.Columns;
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

	private static final String DATABASE_NAME = "news.db";
	private static final int DATABASE_VERSION = 1;

	public static final String ALARM_TABLE = "alarms";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_ALARM_SQL);
		// insert default alarms
		String insertMe = "INSERT INTO " + ALARM_TABLE +
	          "(hour, minutes, daysofweek, alarmtime, enabled, vibrate, message, alert) " +
	          "VALUES ";
		db.execSQL(insertMe + "(7, 0, 127, 0, 0, 1, '', '');");
		db.execSQL(insertMe + "(8, 30, 31, 0, 0, 1, '', '');");
		db.execSQL(insertMe + "(9, 00, 0, 0, 0, 1, '', '');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL(DROP_TABLE_ALARM_SQL);
         onCreate(db);
	}

	private final String CREATE_TABLE_ALARM_SQL = 
		"CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + "(" + 
			Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			Columns.HOUR + " INTEGER," +
			Columns.MINUTES + " INTEGER," +
			Columns.DAYS_OF_WEEK + " INTEGER," +
			Columns.ALARM_TIME + " INTEGERm," +
			Columns.ENABLED + " INTEGER," +
			Columns.VIBRATE + " INTEGER," +
			Columns.MESSAGE + " TEXT," + 
			Columns.ALERT + " TEXT" +
		");";
	
	
	private final String DROP_TABLE_ALARM_SQL = "DROP TABLE IF EXISTS " + ALARM_TABLE;
	
}
