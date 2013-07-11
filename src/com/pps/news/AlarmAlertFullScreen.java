package com.pps.news;

import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Alarm;
import com.pps.news.common.AlarmAlertWakeLock;
import com.pps.news.constant.Constants;
import com.pps.news.widget.SlideRelativeLayout;

public class AlarmAlertFullScreen extends BaseActivity implements OnClickListener {

	private TextView txtHour;
	private TextView txtMinute;
	private TextView txtDate;
	private SlideRelativeLayout mPanel;
	
	private Alarm alarm;
	private Handler handler;
	private boolean mPlaying;
	private Vibrator vibrator;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.news_notice_layout);
		alarm = getIntent().getParcelableExtra(Constants.ALARM_EXTRAS);
		findViewById(R.id.turn).setOnClickListener(this);
		findViewById(R.id.turn_and_update).setOnClickListener(this);
		txtHour = (TextView) findViewById(R.id.hour);
		txtMinute = (TextView) findViewById(R.id.minute);
		txtDate = (TextView) findViewById(R.id.dateString);
		mPanel = (SlideRelativeLayout) findViewById(R.id.container);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		AlarmAlertWakeLock.acquireCpuWakeLock(this);
		if (alarm != null) {
			txtHour.setText(String.valueOf(alarm.hour));
			txtMinute.setText(String.valueOf(alarm.minutes));
			play(alarm);
		} else {
			handler = new Handler();
			handler.post(mRunnable);
		}

		CharSequence newTime = DateFormat.format(
				getText(R.string.alarm_month_and_day),
				System.currentTimeMillis());
		txtDate.setText(newTime);
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
	protected void onDestroy() {
		super.onDestroy();
		stop();
		mPanel.release();
		AlarmAlertWakeLock.releaseCpuLock();
		if (handler != null) {
			handler.removeCallbacks(mRunnable);
		}
	}

	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			txtHour.setText(DateFormat.format("kk", System.currentTimeMillis()));
			txtMinute.setText(DateFormat.format("mm",
					System.currentTimeMillis()));
			handler.postDelayed(mRunnable, 1000L);
		}
	};
}
