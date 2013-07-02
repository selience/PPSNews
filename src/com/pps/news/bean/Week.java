package com.pps.news.bean;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.util.SparseArray;

public class Week {

	public static String[] WEEKDAYS = { "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六", "星期日", };

	public static String[] SHORT_WEEKDAYS = { "周一", "周二", "周三", "周四", "周五",
			"周六", "周日" };

	public static Integer[] DAY_MAP = { Calendar.MONDAY, Calendar.TUESDAY,
			Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
			Calendar.SATURDAY, Calendar.SUNDAY, };

	static SparseArray<String> mWeekDays = new SparseArray<String>(
			SHORT_WEEKDAYS.length);

	static {
		for (int i = 0; i < DAY_MAP.length; i++) {
			mWeekDays.put(DAY_MAP[i], SHORT_WEEKDAYS[i]);
		}
	}

	public static SparseArray<String> get() {
		return mWeekDays;
	}

	public static String appendValuesAtKey(String[] keys, char sperator) {
		return appendValuesAtKey(Arrays.asList(keys), sperator);
	}
	
	public static String appendValuesAtKey(List<String> keys, char sperator) {
		if (keys != null && keys.size() > 0) {
			Collections.sort(keys); // sort keys
			StringBuilder sb = new StringBuilder();
			for (String key : keys) {
				sb.append(mWeekDays.get(Integer.valueOf(key)));
				sb.append(sperator);
			}
			sb = sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}
		return null;
	}

	public static <T> String join(T[] t, char sperator) {
		return join(Arrays.asList(t), sperator);
	}

	public static <T> String join(List<T> list, char sperator) {
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (T t : list) {
				sb.append(t);
				sb.append(sperator);
			}
			sb = sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}
		return null;
	}
}
