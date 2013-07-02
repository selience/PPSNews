package com.pps.news.bean;

import com.pps.news.util.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @file AlarmModel.java
 * @create 2013-6-27 下午05:50:53
 * @author lilong
 * @description TODO
 */
public class AlarmModel implements Parcelable {
	private int id;
	private String week = "";
	private int hour;
	private int minute;
	private int enable;
	private int vibrate;
	private String ringtone = "";

	public AlarmModel(){
	}

	private AlarmModel(Parcel in) {
		id = ParcelUtil.readIntFromParcel(in);
		week = ParcelUtil.readStringFromParcel(in);
		hour = ParcelUtil.readIntFromParcel(in);
		minute = ParcelUtil.readIntFromParcel(in);
		enable = ParcelUtil.readIntFromParcel(in);
		vibrate = ParcelUtil.readIntFromParcel(in);
		ringtone = ParcelUtil.readStringFromParcel(in);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public boolean isEnable() {
		return enable == 1;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(boolean isEnable) {
		this.enable = (isEnable ? 1 : 0);
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public boolean isVibrate() {
		return vibrate == 1;
	}

	public int getVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean isVibrate) {
		this.vibrate = (isVibrate ? 1 : 0);
	}

	public void setVibrate(int vibrate) {
		this.vibrate = vibrate;
	}

	public String getRingtone() {
		return ringtone;
	}

	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtil.writeIntToParcel(out, id);
		ParcelUtil.writeStringToParcel(out, week);
		ParcelUtil.writeIntToParcel(out, hour);
		ParcelUtil.writeIntToParcel(out, minute);
		ParcelUtil.writeIntToParcel(out, enable);
		ParcelUtil.writeIntToParcel(out, vibrate);
		ParcelUtil.writeStringToParcel(out, ringtone);
	}

	public static final Parcelable.Creator<AlarmModel> CREATOR  = new Parcelable.Creator<AlarmModel>() {
	public AlarmModel createFromParcel(Parcel in) {
		    return new AlarmModel(in);
		}
		
		public AlarmModel[] newArray(int size) {
		    return new AlarmModel[size];
		}
	};

}
