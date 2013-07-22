package com.pps.news.service;

import java.io.IOException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.pps.news.bean.Alarm;
import com.pps.news.common.AlarmAlertWakeLock;
import com.pps.news.constant.Constants;
import com.pps.news.util.Log;

public class AlarmService extends Service {
	private static final boolean DEBUG = true;
	private static final String TAG = "AlarmService";
	private static final int ALARM_TIMEOUT_SECONDS = 10 * 60;
	private static final long[] sVibratePattern = new long[] { 500, 500 };

	private boolean mPlaying;
	private Vibrator mVibrator;
	private MediaPlayer mMediaPlayer;
	private TelephonyManager mTelephonyManager;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate() {
		if (DEBUG) Log.v(TAG, "onCreate");
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		AlarmAlertWakeLock.acquireCpuWakeLock(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (DEBUG) Log.v(TAG, "onBind");
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (DEBUG) Log.v(TAG, "onStartCommand");
		
		if (intent == null) {
			stopSelf();
			return START_NOT_STICKY;
		}

		final Alarm alarm = intent.getParcelableExtra(
                Constants.ALARM_INTENT_EXTRA);

		if (alarm == null) {
			stopSelf();
			return START_NOT_STICKY;
		}

		play(alarm);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (DEBUG) Log.v(TAG, "onDestroy");
		
		stop();
		AlarmAlertWakeLock.releaseCpuLock();
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_NONE);
	}

	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String ignored) {
			if (state != TelephonyManager.CALL_STATE_IDLE) {
				stopSelf();
			}
		}
	};

	private void play(Alarm alarm) {
		stop();

		if (!alarm.silent) {
			Uri alert = alarm.alert;
			if (alert == null) {
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			}

			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {
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
			} catch (Exception ex) {
				mMediaPlayer.reset();
			}
		}

		if (alarm.vibrate) {
			mVibrator.vibrate(sVibratePattern, 0);
		} else {
			mVibrator.cancel();
		}

		enableKiller(alarm);
		mPlaying = true;
	}

	private void startAlarm(MediaPlayer player) throws IOException,
			IllegalArgumentException, IllegalStateException {
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.setLooping(true);
			player.prepare();
			player.start();
		}
	}

	public void stop() {
		if (mPlaying) {
			mPlaying = false;

			// Stop audio playing
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}

			// Stop vibrator
			mVibrator.cancel();
		}

		// Stop Listen
		disableKiller();
	}

	// 一定时间后杀掉闹钟服务，避免闹钟长时间响铃和震动
	private void enableKiller(Alarm alarm) {
		mHandler.postDelayed(mRunnable, 1000 * ALARM_TIMEOUT_SECONDS);
	}

	private void disableKiller() {
		mHandler.removeCallbacks(mRunnable);
	}

	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			stopSelf();
		}
	};
}
