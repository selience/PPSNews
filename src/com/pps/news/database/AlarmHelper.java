package com.pps.news.database;

import java.util.ArrayList;
import java.util.List;
import com.pps.news.bean.AlarmModel;
import com.pps.news.database.SQLiteHelper.AlarmColumns;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @file AlarmHelper.java
 * @create 2013-6-27 下午06:10:44
 * @author lilong
 * @description TODO 闹钟常用设置
 */
public class AlarmHelper {

	private SQLiteHelper sqliteHelper;
	
	public AlarmHelper(Context context) {
		sqliteHelper = new SQLiteHelper(context);
	}
	
	public long add(AlarmModel model) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(AlarmColumns.HOUR, model.getHour());
		values.put(AlarmColumns.MINUTE, model.getMinute());
		values.put(AlarmColumns.WEEK, model.getWeek());
		values.put(AlarmColumns.ENABLE, model.getEnable());
		values.put(AlarmColumns.VIBRATE, model.getVibrate());
		values.put(AlarmColumns.RINGTONE, model.getRingtone());
		long rowId = db.insert(SQLiteHelper.ALARM_TABLE, null, values);
		db.close();
		return rowId;
	}
	
	public List<AlarmModel> list() {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		Cursor cursor = db.query(SQLiteHelper.ALARM_TABLE, null, null, null, null, null, AlarmColumns.ID +" ASC");
		List<AlarmModel> data = new ArrayList<AlarmModel>();
		while (cursor.moveToNext()) {
			AlarmModel model = new AlarmModel();
			model.setId(cursor.getInt(cursor.getColumnIndex(AlarmColumns.ID)));
			model.setHour(cursor.getInt(cursor.getColumnIndex(AlarmColumns.HOUR)));
			model.setMinute(cursor.getInt(cursor.getColumnIndex(AlarmColumns.MINUTE)));
			model.setWeek(cursor.getString(cursor.getColumnIndex(AlarmColumns.WEEK)));
			model.setEnable(cursor.getInt(cursor.getColumnIndex(AlarmColumns.ENABLE)));
			model.setVibrate(cursor.getInt(cursor.getColumnIndex(AlarmColumns.VIBRATE)));
			model.setRingtone(cursor.getString(cursor.getColumnIndex(AlarmColumns.RINGTONE)));
			data.add(model);
		}
		cursor.close();
		db.close();
		return data;
	}
	
	public long delete(int id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		long rowId = db.delete(SQLiteHelper.ALARM_TABLE, AlarmColumns.ID + "=" + id, null);
		db.close();
		return rowId;
	}
	
	
	public long update(boolean isEnable, int id) {
		ContentValues values = new ContentValues();
		values.put(AlarmColumns.ENABLE, isEnable?1:0);
		return update(values, id);
	}
	
	public long update(AlarmModel model) {
		ContentValues values = new ContentValues();
		values.put(AlarmColumns.HOUR, model.getHour());
		values.put(AlarmColumns.MINUTE, model.getMinute());
		values.put(AlarmColumns.WEEK, model.getWeek());
		values.put(AlarmColumns.ENABLE, model.getEnable());
		values.put(AlarmColumns.VIBRATE, model.getVibrate());
		values.put(AlarmColumns.RINGTONE, model.getRingtone());
		return update(values, model.getId());
	}
	
	public long update(ContentValues values, int id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		long rowId = db.update(SQLiteHelper.ALARM_TABLE, values, AlarmColumns.ID + "=" + id, null);
		db.close();
		return rowId;
	}
}
