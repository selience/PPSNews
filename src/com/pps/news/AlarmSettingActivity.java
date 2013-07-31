package com.pps.news;

import java.util.Calendar;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Alarm;
import com.pps.news.bean.DaysOfWeek;
import com.pps.news.constant.Constants;
import com.pps.news.database.DatabaseHelper;
import com.pps.news.util.DateUtils;
import com.pps.news.util.ToastUtils;
import com.pps.news.util.UIUtil;
import com.pps.news.widget.BaseAlertDialog;
import com.pps.news.widget.WeekPickerDialog;
import com.pps.news.widget.WeekPickerDialog.OnItemChangedListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @file AlarmSettingActivity.java
 * @create 2013-7-2 下午05:40:18
 * @author lilong
 * @description TODO  闹钟编辑
 */
public class AlarmSettingActivity extends BaseActivity implements OnClickListener, OnItemChangedListener {
	private static final int SETTING_DIALOG_ID_TIME = 0x0;
	private static final int SETTING_DIALOG_ID_DELETE = 0x1;
	
	private static final int SETTING_ALARM_EDIT_ITEM = 0x200;
	private static final int SETTING_ALARM_ADD_ITEM = 0x201;
	
	private TextView txtTime;
	private TextView txtWeekDays;
	private TextView txtRingTone;
	private CheckBox ckEnable;
	private CheckBox ckVibrate;
	private WeekPickerDialog dialog;
	
	private int status;
	private Alarm alarm;
	private Calendar c;
	private DatabaseHelper helper;
	
	private int hourOfDay, minute;
	private Uri ringtoneUri;
	private DaysOfWeek daysOfWeek;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_setting);
		alarm = getIntent().getParcelableExtra(Constants.ALARM_EXTRAS);
		findViewById(R.id.setting_time).setOnClickListener(this);
		findViewById(R.id.setting_repeat).setOnClickListener(this);
		findViewById(R.id.setting_ringtone).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		findViewById(R.id.confirm).setOnClickListener(this);
		
		txtTime = (TextView) findViewById(R.id.sub_time);
		txtWeekDays = (TextView) findViewById(R.id.sub_weekdays);
		txtRingTone = (TextView) findViewById(R.id.sub_ringtone);
		ckEnable = (CheckBox) findViewById(R.id.sub_enable);
		ckVibrate = (CheckBox) findViewById(R.id.sub_vibrate);
		
		c = Calendar.getInstance();
		helper = new DatabaseHelper(this);
		
		if (alarm != null) {
			status = SETTING_ALARM_EDIT_ITEM;
			hourOfDay = alarm.hour;
			minute = alarm.minutes;
			ringtoneUri = alarm.alert;
			daysOfWeek = alarm.daysOfWeek;
	    	ckEnable.setChecked(alarm.enabled);
			ckVibrate.setChecked(alarm.vibrate);
			((ViewStub)findViewById(R.id.toolbar)).inflate();
			findViewById(R.id.imageView).setOnClickListener(this);
		} else {
			alarm = new Alarm();
			status = SETTING_ALARM_ADD_ITEM;
			hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			ringtoneUri = alarm.alert;
			daysOfWeek = alarm.daysOfWeek;
		}
		updateTime();
		txtWeekDays.setText(alarm.daysOfWeek.toString(this, true));
		Ringtone ringtone = UIUtil.getRingtoneWithUri(this, ringtoneUri);
		if (ringtone != null) {
			txtRingTone.setText(ringtone.getTitle(this));
			ringtone.stop();
		}
		dialog = new WeekPickerDialog(this);
		dialog.setDaysOfWeek(daysOfWeek);
		dialog.setOnItemChangedListener(this);
	}

	private void updateTime() {
		c.clear();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		txtTime.setText(DateUtils.formatTime(this, c));
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SETTING_DIALOG_ID_TIME:
			return new TimePickerDialog(this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int newHour, int newMinute) {
					hourOfDay = newHour;
					minute = newMinute;
					updateTime();
				}
			}, hourOfDay, minute, DateFormat.is24HourFormat(this));
		case SETTING_DIALOG_ID_DELETE:
			return new BaseAlertDialog(this).setMessage(R.string.delete_tips)
			.setPositiveClickListener(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (alarm!=null) {
						helper.delete(alarm.id);
						DateUtils.setNextAlert(AlarmSettingActivity.this);
						dialog.dismiss();
						finish();
					}
				}
			});
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.setting_time:
			showDialog(SETTING_DIALOG_ID_TIME);
			break;
		case R.id.setting_repeat:
			dialog.show();
			break;
		case R.id.setting_ringtone:
			startActivityForResult(UIUtil.newRingtoneIntent(ringtoneUri.toString()), 0x100);
			break;
		case R.id.imageView:
			showDialog(SETTING_DIALOG_ID_DELETE);
			break;
		case R.id.confirm:
			long time = 0;
			if (!daysOfWeek.isRepeatSet()) {
				time = DateUtils.calculateAlarm(hourOfDay, minute, daysOfWeek).getTimeInMillis();
			} 
			alarm.hour = hourOfDay;
			alarm.minutes = minute;
			alarm.daysOfWeek = daysOfWeek;
			alarm.enabled = ckEnable.isChecked();
			alarm.vibrate = ckVibrate.isChecked();
			alarm.alert = ringtoneUri;
			alarm.time = time;
			if (status == SETTING_ALARM_ADD_ITEM) {
				if (helper.insert(alarm) > 0) {
					DateUtils.setNextAlert(this);
					ToastUtils.showMessage(this, R.string.add_success_tips, Toast.LENGTH_SHORT);
				}
			} else if (status == SETTING_ALARM_EDIT_ITEM) {
				if (helper.update(alarm) > 0) {
					DateUtils.setNextAlert(this);
					ToastUtils.showMessage(this, R.string.update_success_tips, Toast.LENGTH_SHORT);
				}
			}
			finish();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x100 && resultCode == RESULT_OK) {
		   Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		   if (uri != null) {
			   ringtoneUri = uri;
			   Ringtone ringTone = RingtoneManager.getRingtone(this, uri);
			   if (ringTone != null) {
				   txtRingTone.setText(ringTone.getTitle(this));
				   ringTone.stop();
			   }
		   }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(DaysOfWeek mDayOfWeek) {
		this.daysOfWeek = mDayOfWeek;
		txtWeekDays.setText(mDayOfWeek.toString(this, true));
	}

}
