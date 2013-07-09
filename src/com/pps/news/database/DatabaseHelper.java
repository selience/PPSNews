package com.pps.news.database;

import java.util.ArrayList;
import java.util.List;
import com.pps.news.bean.Alarm;
import com.pps.news.bean.Alarm.Columns;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @file DatabaseHelper.java
 * @create 2013-7-9 上午11:18:15
 * @author lilong
 * @description TODO
 */
public class DatabaseHelper {

	private SQLiteHelper sqliteHelper;

	public DatabaseHelper(Context context) {
		sqliteHelper = new SQLiteHelper(context);
	}

	public long insert(Alarm alarm) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Columns.HOUR, alarm.hour);
		values.put(Columns.MINUTES, alarm.minutes);
		values.put(Columns.DAYS_OF_WEEK, alarm.daysOfWeek.getCoded());
		values.put(Columns.ALARM_TIME, alarm.time);
		values.put(Columns.ENABLED, alarm.enabled?1:0);
		values.put(Columns.VIBRATE, alarm.vibrate?1:0);
		values.put(Columns.MESSAGE, alarm.label);
		values.put(Columns.ALERT, alarm.alert.toString());
		long rowId = db.insert(SQLiteHelper.ALARM_TABLE, null, values);
		db.close();
		return rowId;
	}
	
	public long delete(int _id) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		long rowId = db.delete(SQLiteHelper.ALARM_TABLE, Columns._ID + "=" + _id, null);
		db.close();
		return rowId;
	}
	
	public long update(int _id, boolean enabled) {
		ContentValues values = new ContentValues();
		values.put(Columns.ENABLED, enabled?1:0);
		return update(values, _id);
	}
	
	public long update(Alarm alarm) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Columns.HOUR, alarm.hour);
		values.put(Columns.MINUTES, alarm.minutes);
		values.put(Columns.DAYS_OF_WEEK, alarm.daysOfWeek.getCoded());
		values.put(Columns.ALARM_TIME, alarm.time);
		values.put(Columns.ENABLED, alarm.enabled?1:0);
		values.put(Columns.VIBRATE, alarm.vibrate?1:0);
		values.put(Columns.MESSAGE, alarm.label);
		values.put(Columns.ALERT, alarm.alert.toString());
		long rowId = update(values, alarm.id);
		db.close();
		return rowId;
	}
	
	public long update(ContentValues values, int _id) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		long rowId = db.update(SQLiteHelper.ALARM_TABLE, values, Columns._ID + "=" + _id, null);
		db.close();
		return rowId;
	}
	
	public List<Alarm> query() {
		return query(null);
	}
	
	public List<Alarm> query(String selection) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		Cursor cursor = db.query(SQLiteHelper.ALARM_TABLE, null, selection, null, null, null, Columns._ID +" ASC");
		List<Alarm> data = new ArrayList<Alarm>();
		while (cursor.moveToNext()) {
			data.add(new Alarm(cursor));
		}
		cursor.close();
		db.close();
		return data;
	}
}
