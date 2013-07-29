package com.pps.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.pps.news.app.BaseActivity;

public class SplashActivity extends BaseActivity {
	
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		handler.postDelayed(mRunnable, 2000L);
	}
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			startActivity(new Intent(getBaseContext(), NewsActivity.class));
			finish();
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(mRunnable);
	}
	
}
