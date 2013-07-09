package com.pps.news.adapter;

import java.util.Calendar;
import java.util.List;
import com.pps.news.R;
import com.pps.news.bean.Alarm;
import com.pps.news.util.DateUtils;
import com.pps.news.util.UIUtil;
import android.content.Context;
import android.media.Ringtone;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * @file AlarmAdapter.java
 * @create 2013-6-27 下午06:42:11
 * @author lilong
 * @description TODO
 */
public class AlarmAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
	private Context context;
	private List<Alarm> list = null;
	
	public AlarmAdapter(Context context, List<Alarm> data) {
		this.list = data;
		this.context = context;
	}

	public void addAll(List<Alarm> data) {
		this.list = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (list == null)
			return null;
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		StateHolder mHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.alarm_list_item, null);
			mHolder = new StateHolder();
			mHolder.ckEnable = (CheckBox) convertView.findViewById(R.id.ckEnable);
			mHolder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			mHolder.txtRepeat = (TextView) convertView.findViewById(R.id.txtRepeat);
			mHolder.txtRingtone = (TextView) convertView.findViewById(R.id.txtRingtone);
			convertView.setTag(mHolder);
		} else {
			mHolder = (StateHolder)convertView.getTag();
		}
		
		final Alarm alarm = list.get(position);
		if (alarm != null) {
			mHolder.ckEnable.setTag(alarm);
			mHolder.ckEnable.setChecked(alarm.enabled);
			mHolder.ckEnable.setOnCheckedChangeListener(this);
	
			final Calendar c = Calendar.getInstance();
	        c.set(Calendar.HOUR_OF_DAY, alarm.hour);
	        c.set(Calendar.MINUTE, alarm.minutes); 
			mHolder.txtTime.setText(DateUtils.formatTime(context, c));
			
			final String daysOfWeekStr = alarm.daysOfWeek.toString(context, false);
			if (daysOfWeekStr != null && daysOfWeekStr.length() != 0) {
				mHolder.txtRepeat.setText(daysOfWeekStr);
				mHolder.txtRepeat.setVisibility(View.VISIBLE);
			} else {
				mHolder.txtRepeat.setVisibility(View.GONE);
			}
			
		 	Ringtone ringtone = UIUtil.getRingtoneWithUri(context, alarm.alert);
			if (ringtone != null) {
				mHolder.txtRingtone.setText(ringtone.getTitle(context));
				mHolder.txtRingtone.setVisibility(View.VISIBLE);
				ringtone.stop();
			} else {
				mHolder.txtRingtone.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class StateHolder {
		CheckBox ckEnable;
		TextView txtTime;
		TextView txtRepeat;
		TextView txtRingtone;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		Alarm alarm = (Alarm)buttonView.getTag();
		if (alarm != null) {
			DateUtils.enableAlarm(context, alarm, isChecked);
		}
	}
}
