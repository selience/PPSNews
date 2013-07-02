package com.pps.news.adapter;

import java.util.List;
import com.pps.news.R;
import com.pps.news.bean.AlarmModel;
import com.pps.news.bean.Week;
import com.pps.news.database.AlarmHelper;
import com.pps.news.util.UIUtil;
import android.content.Context;
import android.media.Ringtone;
import android.net.Uri;
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
	private AlarmHelper helper = null;
	private List<AlarmModel> list = null;
	
	public AlarmAdapter(Context context, List<AlarmModel> data) {
		this.list = data;
		this.context = context;
		this.helper = new AlarmHelper(context);
	}

	public void addAll(List<AlarmModel> data) {
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
		
		final AlarmModel model = list.get(position);
		if (model != null) {
			mHolder.ckEnable.setOnCheckedChangeListener(null);
			mHolder.ckEnable.setTag(model.getId());
			mHolder.ckEnable.setChecked(model.isEnable());
			mHolder.ckEnable.setOnCheckedChangeListener(this);
			
			mHolder.txtTime.setText(UIUtil.parseTimeString(model.getHour(), model.getMinute()));
			mHolder.txtRepeat.setText(Week.split(model.getWeek().split(","), ' '));
			Ringtone ringtone = UIUtil.getRingtoneWithUri(context, Uri.parse(model.getRingtone()));
			if (ringtone != null) {
				mHolder.txtRingtone.setText(ringtone.getTitle(context));
				ringtone.stop();
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
		int id = Integer.valueOf(buttonView.getTag().toString());
		System.out.println("update :: "+helper.update(isChecked, id));
	}
}
