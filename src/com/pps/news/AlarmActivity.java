package com.pps.news;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pps.news.adapter.AlarmAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.AlarmModel;
import com.pps.news.database.AlarmHelper;

public class AlarmActivity extends BaseActivity implements OnClickListener {

	private ListView listView;
	private AlarmAdapter mAdapter;
	private AlarmHelper alarmHelper;
	private TextView txtSummary;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.alarm_layout);
		findViewById(R.id.ic_back).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		listView = (ListView) findViewById(android.R.id.list);
		txtSummary = (TextView) findViewById(R.id.subTitle);
		alarmHelper = new AlarmHelper(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlarmModel model = (AlarmModel)parent.getItemAtPosition(position);
				Intent intent = new Intent(AlarmActivity.this, AlarmSettingActivity.class);
				intent.putExtra("alarm", model);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		new GetAlarmListTask().execute();
	}
	
	private void onRefresh(List<AlarmModel> data) {
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
		case R.id.ic_back:
			finish();
			break;
		case R.id.add:
			startActivity(new Intent(this, AlarmSettingActivity.class));
			break;
		}
	}

	class GetAlarmListTask extends AsyncTask<String, Integer, List<AlarmModel>> {

		@Override
		protected List<AlarmModel> doInBackground(String... params) {
			return alarmHelper.list();
		}

		@Override
		protected void onPostExecute(List<AlarmModel> data) {
			txtSummary.setText(data.size()+"");
			onRefresh(data);
		}
		
	}
}
