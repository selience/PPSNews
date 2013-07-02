package com.pps.news.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.pps.news.R;
import com.pps.news.bean.Week;
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

	private boolean[] defaultValues;
	private OnItemChangedListener mOnClickListener;
	private List<String> keys = new ArrayList<String>();
	
	public WeekPickerDialog(Context context) {
		super(context);
	}
	
	public WeekPickerDialog(Context context, int theme) {
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		
		findViewById(R.id.ic_back).setVisibility(View.GONE);
		((TextView)findViewById(R.id.subTitle)).setText("Week");
		((TextView)findViewById(R.id.title)).setText("重复设置");
		findViewById(R.id.confirm).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);

		defaultValues = new boolean[Week.SHORT_WEEKDAYS.length];
		ListView listView = (ListView) findViewById(android.R.id.list);
		WeekAdapter adapter = new WeekAdapter(getContext(), Arrays.asList(Week.SHORT_WEEKDAYS));
		listView.setAdapter(adapter);
		
		int width = UIUtil.getScreenWidth(getContext()) - 80;
		int height = UIUtil.getScreenHeight(getContext()) - 230;
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.dialog);
		ViewGroup.LayoutParams lp = rl.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(width, height);
		} else {
			lp.width = width;
			lp.height = height;
		}
		rl.setLayoutParams(lp);
		
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm:
			if (mOnClickListener!=null) {
				mOnClickListener.onItemClick(v, keys);
				dismiss();
			}
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
	}
	
	public void setValues(boolean[] values) {
		this.defaultValues = values;
	}
	
	public void setOnClickListener(OnItemChangedListener listener) {
		this.mOnClickListener = listener;
	}

	public static interface OnItemChangedListener {
		
		void onItemClick(View v, List<String> data);
	}
	
	class WeekAdapter extends BaseAdapter {
		private List<String> list;
		private Context context;
		
		public WeekAdapter(Context context, List<String> data) {
			this.list = data;
			this.context = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
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
				convertView = View.inflate(context, R.layout.dialog_list_item, null);
				mHolder = new StateHolder();
				mHolder.textView = (TextView) convertView.findViewById(R.id.textView);
				mHolder.ckSelect = (CheckBox) convertView.findViewById(R.id.ckSelect);
				mHolder.ckSelect.setTag(position);
				mHolder.ckSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						String key = buttonView.getTag().toString();
						if (isChecked) {
							keys.add(key);
						} else {
							if (keys.contains(key)) {
								keys.remove(key);
							}
						}
					}
				});
				convertView.setTag(mHolder);
			} else {
				mHolder = (StateHolder)convertView.getTag();
			}
			mHolder.textView.setText(list.get(position));
			mHolder.ckSelect.setChecked(defaultValues[position]);
			
			return convertView;
		}
		
		
		class StateHolder {
			TextView textView;
			CheckBox ckSelect;
		}
	}
}
