package com.pps.news;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;

public class NoticeActivity extends BaseActivity implements OnClickListener {
	
	private TextView txtHour;
	private TextView txtMinute;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.notice);
		findViewById(R.id.turn).setOnClickListener(this);
		findViewById(R.id.turn_and_update).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.turn:
			break;
		case R.id.turn_and_update:
			break;
		}
	}

}
