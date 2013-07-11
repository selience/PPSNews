package com.pps.news;

import java.util.List;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.pps.news.adapter.AlarmAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Alarm;
import com.pps.news.constant.Constants;
import com.pps.news.database.DatabaseHelper;

/**
 * @file AlarmActivity.java
 * @create 2013-7-2 下午05:40:36
 * @author lilong
 * @description TODO 闹钟设置
 */
public class AlarmActivity extends BaseActivity implements OnClickListener,AdapterView.OnItemClickListener {

	private ListView listView;
	private AlarmAdapter mAdapter;
	private DatabaseHelper helper;
	private TextView txtSummary;
	private ProgressBar mProgressBar;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.alarm_layout);
		findViewById(R.id.icon).setOnClickListener(this);
		findViewById(R.id.imageView).setOnClickListener(this);
		listView = (ListView) findViewById(android.R.id.list);
		txtSummary = (TextView) findViewById(R.id.summary);
		mProgressBar = (ProgressBar)findViewById(R.id.progress);
		helper = new DatabaseHelper(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new GetAlarmListTask().execute();
	}
	
	private void onRefresh(List<Alarm> data) {
		if (data == null || data.isEmpty()) 
			return;

		if (mAdapter == null) {
			mAdapter = new AlarmAdapter(this, data);
			listView.setAdapter(mAdapter);
		} else {
			mAdapter.addAll(data);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		case R.id.imageView:
			startActivity(new Intent(this, AlarmSettingActivity.class));
			break;
		}
	}

	class GetAlarmListTask extends AsyncTask<String, Integer, List<Alarm>> {

		@Override
		protected List<Alarm> doInBackground(String... params) {
			return helper.query();
		}

		@Override
		protected void onPostExecute(List<Alarm> data) {
			txtSummary.setText(data.size()+"");
			onRefresh(data);
			mProgressBar.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Alarm model = (Alarm)parent.getItemAtPosition(position);
		Intent intent = new Intent(this, AlarmSettingActivity.class);
		intent.putExtra(Constants.ALARM_EXTRAS, model);
		startActivity(intent);
	}
}
