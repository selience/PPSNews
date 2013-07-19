package com.pps.news;

import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Alarm;
import com.pps.news.common.AlarmAlertWakeLock;
import com.pps.news.constant.Constants;

public class AlarmAlertActivity extends BaseActivity implements OnClickListener, OnTouchListener {

	private View touchView;
	private TextView txtHour;
	private TextView txtMinute;
	private TextView txtDate;
	private TextView txtTurn;
	private TextView txtUpdate;
	
	private int startY;
	private Alarm alarm;
	private boolean mPlaying;
	private Vibrator vibrator;
	private MediaPlayer mMediaPlayer;

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
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		AlarmAlertWakeLock.acquireCpuWakeLock(this);
		if (alarm != null) {
			txtHour.setText(String.valueOf(alarm.hour));
			txtMinute.setText(String.valueOf(alarm.minutes));
			play(alarm);
		}
		
		txtHour.setText(DateFormat.format("kk", System.currentTimeMillis()));
		txtMinute.setText(DateFormat.format("mm", System.currentTimeMillis()));
		txtDate.setText(DateFormat.format(getText(R.string.alarm_month_and_day), System.currentTimeMillis()));
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
	
	private void play(Alarm alarm) {
		stop();

		if (!alarm.silent) {
			Uri alert = alarm.alert;
			if (alert == null) {
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			}

			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mp.stop();
					mp.release();
					mMediaPlayer = null;
					return true;
				}
			});
			try {
				mMediaPlayer.setDataSource(this, alert);
				startAlarm(mMediaPlayer);
			} catch (Exception e) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}

		if (alarm.vibrate) {
			vibrator.vibrate(new long[] { 500, 500 }, 0);
		} else {
			vibrator.cancel();
		}
		mPlaying = true;
	}

	private void startAlarm(MediaPlayer player) throws IllegalStateException,
			IOException {
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.setLooping(true);
			player.prepare();
			player.start();
		}
	}

	private void stop() {
		if (mPlaying) {
			mPlaying = false;
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			vibrator.cancel();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stop();
		AlarmAlertWakeLock.releaseCpuLock();
	}

}
