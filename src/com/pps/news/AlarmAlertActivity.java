package com.pps.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Alarm;
import com.pps.news.constant.Constants;
import com.pps.news.service.AlarmService;

public class AlarmAlertActivity extends BaseActivity implements OnClickListener, OnTouchListener {

	private View touchView;
	private TextView txtHour;
	private TextView txtMinute;
	private TextView txtDate;
	private TextView txtTurn;
	private TextView txtUpdate;
	
	private int startY;
	private Alarm alarm;

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.news_notice_layout);
		alarm = getIntent().getParcelableExtra(Constants.ALARM_EXTRAS);
		txtTurn = (TextView) findViewById(R.id.turn);
		txtUpdate = (TextView) findViewById(R.id.turn_and_update);
		txtTurn.setOnClickListener(this);
		txtUpdate.setOnClickListener(this);
		txtHour = (TextView) findViewById(R.id.hour);
		txtMinute = (TextView) findViewById(R.id.minute);
		txtDate = (TextView) findViewById(R.id.dateString);
		touchView = findViewById(R.id.touchView);
		touchView.setOnTouchListener(this);
		
		txtHour.setText(DateFormat.format("kk", System.currentTimeMillis()));
		txtMinute.setText(DateFormat.format("mm", System.currentTimeMillis()));
		txtDate.setText(DateFormat.format(getText(R.string.alarm_month_and_day), System.currentTimeMillis()));
		
		if (alarm != null) {
			txtHour.setText(String.valueOf(alarm.hour));
			txtMinute.setText(String.valueOf(alarm.minutes));
		}
	} 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.turn:
			finish();
			break;
		case R.id.turn_and_update:
			startActivity(new Intent(this, NewsActivity.class));
			finish();
			break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int distanceY = (int)event.getRawY() - startY;
			int top = v.getTop() + distanceY;
			int bottom = v.getBottom() + distanceY;

			if (top < txtTurn.getBottom()) {
				top = txtTurn.getBottom();
				bottom = top + v.getHeight();
			}
			if (bottom > txtUpdate.getBottom()) {
				bottom = txtUpdate.getBottom();
				top = bottom - v.getHeight();
			}
			v.layout(v.getLeft(), top, v.getRight(), bottom);
			startY = (int)event.getRawY();
			v.postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
			if (v.getTop()<=txtTurn.getBottom()) {
				finish();
			} else if (v.getBottom()>=txtUpdate.getTop()) {
				startActivity(new Intent(this, NewsActivity.class));
				finish();
			} else {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)v.getLayoutParams();
				lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
				v.setLayoutParams(lp);
			}
			break;
		}
		return true;
	}
	
	private void sendKillService() {
		stopService(new Intent(this, AlarmService.class));
	}
	
	@Override
	protected void onDestroy() {
		sendKillService();
		super.onDestroy();
	}
}
