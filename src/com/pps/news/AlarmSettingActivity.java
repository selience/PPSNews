package com.pps.news;

import java.util.Calendar;
import java.util.List;

import com.pps.news.app.BaseActivity;
import com.pps.news.bean.AlarmModel;
import com.pps.news.bean.Week;
import com.pps.news.database.AlarmHelper;
import com.pps.news.util.UIUtil;
import com.pps.news.widget.WeekPickerDialog;
import com.pps.news.widget.WeekPickerDialog.OnItemChangedListener;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmSettingActivity extends BaseActivity implements OnClickListener, OnItemChangedListener {
	private static final int SETTING_DIALOG_ID_TIME = 0x0;
	
	private static final int SETTING_ALARM_EDIT_ITEM = 0x200;
	private static final int SETTING_ALARM_ADD_ITEM = 0x201;
	
	private TextView txtTime;
	private TextView txtWeekDays;
	private TextView txtRingTone;
	private CheckBox ckEnable;
	private CheckBox ckVibrate;

	private int status;
	private AlarmModel model;
	private AlarmHelper alarmHelper;
	
	private Calendar cal;
	private int hourOfDay, minute;
	private Uri ringtoneUri;
	private String weeks;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.alarm_setting);
		model = getIntent().getParcelableExtra("alarm");
		
		findViewById(R.id.ic_back).setOnClickListener(this);
		findViewById(R.id.setting_time).setOnClickListener(this);
		findViewById(R.id.setting_repeat).setOnClickListener(this);
		findViewById(R.id.setting_ringtone).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		findViewById(R.id.confirm).setOnClickListener(this);
		Button btnDelte = (Button)findViewById(R.id.delete);
		btnDelte.setOnClickListener(this);
		
		txtTime = (TextView) findViewById(R.id.sub_time);
		txtWeekDays = (TextView) findViewById(R.id.sub_weekdays);
		txtRingTone = (TextView) findViewById(R.id.sub_ringtone);
		ckEnable = (CheckBox) findViewById(R.id.sub_enable);
		ckVibrate = (CheckBox) findViewById(R.id.sub_vibrate);
		((TextView)findViewById(R.id.subTitle)).setText("Alarm");
		
		alarmHelper=new AlarmHelper(this);
		cal = Calendar.getInstance();
		
		if (model != null) {
			status = SETTING_ALARM_EDIT_ITEM;
			hourOfDay = model.getHour();
			minute = model.getMinute();
			ringtoneUri = Uri.parse(model.getRingtone());
			ckEnable.setChecked(model.isEnable());
			ckVibrate.setChecked(model.isVibrate());
			txtTime.setText(UIUtil.parseTimeString(model.getHour(), model.getMinute()));
			weeks = model.getWeek();
			txtWeekDays.setText(Week.split(weeks.split(","), ' '));
			btnDelte.setEnabled(true);
		} else {
			status = SETTING_ALARM_ADD_ITEM;
			hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
			weeks = Week.join(Week.allKeys(), ',');
			ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // 获取默认铃声
			txtWeekDays.setText(Week.join(Week.SHORT_WEEKDAYS, ' '));
			txtTime.setText(UIUtil.parseTimeString(hourOfDay, minute));
			btnDelte.setEnabled(false);
		}
		Ringtone ringtone = UIUtil.getRingtoneWithUri(this, ringtoneUri);
		txtRingTone.setText(ringtone.getTitle(this));
		ringtone.stop();
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
					txtTime.setText(UIUtil.parseTimeString(newHour, newMinute));
				}
			}, cal.get(Calendar.HOUR_OF_DAY), 
			   cal.get(Calendar.MINUTE), 
			   DateFormat.is24HourFormat(this));
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ic_back:
		case R.id.cancel:
			finish();
			break;
		case R.id.setting_time:
			showDialog(SETTING_DIALOG_ID_TIME);
			break;
		case R.id.setting_repeat:
			WeekPickerDialog dialog = new WeekPickerDialog(this, R.style.Theme_Dialog);
			dialog.setOnClickListener(this);
			dialog.show();
			break;
		case R.id.setting_ringtone:
			startActivityForResult(UIUtil.newRingtoneIntent(null), 0x100);
			break;
		case R.id.delete:
			if (model!=null && alarmHelper.delete(model.getId())>0) {
				finish();
			}
			break;
		case R.id.confirm:
			if (model == null) {
				model = new AlarmModel();
			}
			model.setHour(hourOfDay);
			model.setMinute(minute);
			model.setWeek(weeks);
			model.setEnable(ckEnable.isChecked());
			model.setVibrate(ckVibrate.isChecked());
			model.setRingtone(ringtoneUri.toString());
			
			if (status == SETTING_ALARM_ADD_ITEM) {
				if (alarmHelper.add(model) > 0) {
					Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
				}
			} else if (status == SETTING_ALARM_EDIT_ITEM) {
				if (alarmHelper.update(model) > 0) {
					Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show();
				}
			}
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(View v, List<String> keys) {
		// TODO Auto-generated method stub
		weeks = Week.join(keys, ',');
		txtWeekDays.setText(Week.split(keys, ' '));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x100 && resultCode == RESULT_OK) {
		   Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		   if (uri != null) {
			   ringtoneUri = uri;
			   Ringtone ringTone = RingtoneManager.getRingtone(this, uri);
			   txtRingTone.setText(ringTone.getTitle(this));
			   ringTone.stop();
		   }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
