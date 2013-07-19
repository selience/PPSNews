package com.pps.news.util;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
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

	// 把字符串转换成UTF-8的格式
	public static String stringToUTF(String str) {
		if (str != null && !str.equals("")) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
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
	
	/** 获取设备IP地址 */
	public static String getIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return ipIntToString(wifiInfo.getIpAddress());
	}

	// see http://androidsnippets.com/obtain-ip-address-of-current-device
	public static String ipIntToString(int ip) {
		try {
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Intent newShareIntent(Context context, String subject, String message, String dialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND); //启动分享发送的属性  
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject); //分享的主题
        shareIntent.putExtra(Intent.EXTRA_TEXT, message); //分享的内容  
        shareIntent.setType("text/plain"); //分享发送的数据类型  
        return Intent.createChooser(shareIntent, dialogTitle); 
    }
}
