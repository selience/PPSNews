package com.pps.news.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @file UIUtil.java
 * @create 2013-6-27 下午02:31:57
 * @author lilong (dreamxsky@gmail.com)
 * @description TODO  
 */
@SuppressLint("SimpleDateFormat")
public final class UIUtil {

	private UIUtil() {
	}

	public static int sdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static boolean isNullOrEmpty(final CharSequence str) {
		return (str == null || str.equals("") || str.equals("null") || str
				.equals("NULL"));
	}

	public static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void hideSoftInputFromWindow(View view) {
		InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		} 
	}
	
	public static String md5ForString(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToMd5String(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}

		return cacheKey;
	}

	private static String bytesToMd5String(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			final int b = bytes[i] & 255;
			if (b < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(b));
		}
		return sb.toString();
	}

	public static String formatDate(long milliseconds) {
		return formatDate(milliseconds, "MM月dd日");
	}

	public static String formatDate(long milliseconds, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(new Date(milliseconds));
	}
	
	public static Date formatDate(String timeString) {
		 try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 格式化日期字符串  单位：秒  */
	public static String getTimeState(long timestamp) {

		try {
			long _timestamp = timestamp * 1000;
			if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
				return "刚刚";
			} else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
				return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
						+ "分钟前";
			} else {
				Calendar now = Calendar.getInstance();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(_timestamp);
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
					return sdf.format(c.getTime());
				}
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
					return sdf.format(c.getTime());
				} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
					SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy年M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// And to convert the image URI to the direct file system path of the image file 
	public static String getRealPathFromURI(Activity activity, Uri contentUri) {   
		// can post image         
		String [] proj={MediaStore.Audio.Media.DATA};         
		Cursor cursor = activity.managedQuery(contentUri,                  
						proj, 	// Which columns to return                      
						null,   // WHERE clause; which rows to return (all rows)            
						null,   // WHERE clause selection arguments (none)           
						null);  // Order-by clause (ascending by name)      
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);        
		cursor.moveToFirst();          
		return cursor.getString(column_index); 
	}
	
	/** 选择系统铃声   */
	public static Intent newRingtoneIntent(String uriString) {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
		Uri ringtoneUri = null;
		if (uriString != null && uriString.length() > 0) {
			ringtoneUri = Uri.parse(uriString);
		} else {
			ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		}
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtoneUri);
		return intent;
	}
	
	public static Ringtone getRingtoneWithUri(Context context, Uri ringtoneUri) {
		return RingtoneManager.getRingtone(context, ringtoneUri);
	}
	
}
