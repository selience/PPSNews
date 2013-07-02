package com.pps.news.widget;

import java.util.ArrayList;
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

	private List<String> defaultKeys;
	private List<String> mKeys = new ArrayList<String>(7);
	private OnItemChangedListener mOnClickListener;
	
	public WeekPickerDialog(Context context, int theme, List<String> data) {
		super(context, theme);
		this.defaultKeys = data;
		this.mKeys.addAll(data);
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
		
		WeekAdapter adapter = new WeekAdapter(getContext(), Week.SHORT_WEEKDAYS);
		listView.setAdapter(adapter);
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
			if (mOnClickListener!=null) {
				mOnClickListener.onItemClick(v, mKeys);
				dismiss();
			}
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
	}
	
	public void setOnClickListener(OnItemChangedListener listener) {
		this.mOnClickListener = listener;
	}

	public static interface OnItemChangedListener {
		
		void onItemClick(View v, List<String> data);
	}
	
	class WeekAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
		private String[] WEEKS;
		private Context context;
		
		public WeekAdapter(Context context, String[] data) {
			this.context = context;
			this.WEEKS = data;
		}
		
		@Override
		public int getCount() {
			return WEEKS.length;
		}

		@Override
		public Object getItem(int position) {
			return WEEKS[position];
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
			String key = String.valueOf(Week.DAY_MAP[position]);
			mHolder.textView.setText(WEEKS[position]);
			mHolder.ckSelect.setTag(key);
			mHolder.ckSelect.setOnCheckedChangeListener(null);
			mHolder.ckSelect.setChecked(defaultKeys.contains(key));
			mHolder.ckSelect.setOnCheckedChangeListener(this);
			
			return convertView;
		}
		
		
		class StateHolder {
			TextView textView;
			CheckBox ckSelect;
		}


		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			String key = buttonView.getTag().toString();
			if (isChecked) {
				mKeys.add(key);
			} else {
				if (mKeys.contains(key)) {
					mKeys.remove(key);
				}
			}
		}
	}
}
