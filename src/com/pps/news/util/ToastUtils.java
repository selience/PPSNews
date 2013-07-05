package com.pps.news.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public final class ToastUtils {

	private ToastUtils() {
	}

	private static Toast toast = null;
	private static Handler handler = null;
	
	static {
		handler = new Handler(Looper.getMainLooper());
	}

	public static void showMessage(final Context act, final int resId,
			final int duration) {
		showMessage(act, act.getText(resId), duration);
	}
	
	public static void showMessage(final Context act, final CharSequence text,
			final int duration) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(act, text, duration);
				} else {
					toast.setText(text);
					toast.setDuration(duration);
				}
				toast.show();
			}
		});
	}
}
