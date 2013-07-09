package com.pps.news.widget;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import com.pps.news.R;
import com.pps.news.bean.DaysOfWeek;
import com.pps.news.util.UIUtil;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeekPickerDialog extends Dialog implements OnClickListener {

	private Context context;
	private CharSequence[] mEntries;
	
	private DaysOfWeek mDaysOfWeek = new DaysOfWeek(0);
	private DaysOfWeek mNewDaysOfWeek = new DaysOfWeek(0);
	
	private OnItemChangedListener mOnItemChangedListener;
	
	public WeekPickerDialog(Context context) {
		this(context, R.style.Theme_Dialog);
	}
	
	public WeekPickerDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		String[] weekdays = new DateFormatSymbols().getWeekdays();
        String[] values = new String[] {
            weekdays[Calendar.MONDAY],
            weekdays[Calendar.TUESDAY],
            weekdays[Calendar.WEDNESDAY],
            weekdays[Calendar.THURSDAY],
            weekdays[Calendar.FRIDAY],
            weekdays[Calendar.SATURDAY],
            weekdays[Calendar.SUNDAY],
        };
        
        mEntries = values;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.week_dialog_layout);
		findViewById(R.id.ic_back).setVisibility(View.GONE);
		((TextView)findViewById(R.id.subTitle)).setText("Week");
		((TextView)findViewById(R.id.title)).setText("重复设置");
		findViewById(R.id.confirm).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		ListView listView = (ListView) findViewById(android.R.id.list);
		
		int width = UIUtil.getScreenWidth(context) - UIUtil.dip2px(context, 40);
		int height = UIUtil.getScreenHeight(context) - UIUtil.dip2px(context, 115);
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.dialog);
		ViewGroup.LayoutParams lp = rl.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(width, height);
		} else {
			lp.width = width;
			lp.height = height;
		}
		rl.setLayoutParams(lp);
		
		listView.setAdapter(new WeekAdapter());
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox ckSelect = (CheckBox) view.findViewById(R.id.ckSelect);
				ckSelect.setChecked(!ckSelect.isChecked());
			}
		});
	}

	@Override
	public void onClick(View v) { 
		switch (v.getId()) {
		case R.id.confirm:
			mDaysOfWeek.set(mNewDaysOfWeek);
			if (mOnItemChangedListener!=null) {
				mOnItemChangedListener.onItemClick(mDaysOfWeek);
				dismiss();
			}
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
	}
	
	public void setDaysOfWeek(DaysOfWeek dow) {
        mDaysOfWeek.set(dow);
        mNewDaysOfWeek.set(dow);
    }
	
	public DaysOfWeek getDaysOfWeek() {
        return mDaysOfWeek;
    }
	
	public void setOnItemChangedListener(OnItemChangedListener listener) {
		this.mOnItemChangedListener = listener;
	}

	public static interface OnItemChangedListener {
		
		void onItemClick(DaysOfWeek mDayOfWeek);
	}
	
	class WeekAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
		
		@Override
		public int getCount() {
			return mEntries.length;
		}

		@Override
		public Object getItem(int position) {
			return mEntries[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			StateHolder mHolder = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.week_dialog_list_item, null);
				mHolder = new StateHolder();
				mHolder.textView = (TextView) convertView.findViewById(R.id.textView);
				mHolder.ckSelect = (CheckBox) convertView.findViewById(R.id.ckSelect);
				convertView.setTag(mHolder);
			} else {
				mHolder = (StateHolder)convertView.getTag();
			}
			mHolder.textView.setText(mEntries[position]);
			mHolder.ckSelect.setTag(position);
			mHolder.ckSelect.setChecked(mDaysOfWeek.getBooleanArray()[position]);
			mHolder.ckSelect.setOnCheckedChangeListener(this);
			return convertView;
		}
		
		
		class StateHolder {
			TextView textView;
			CheckBox ckSelect;
		}


		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int which = Integer.valueOf(buttonView.getTag().toString());
			mNewDaysOfWeek.set(which, isChecked);
		}
	}
}
