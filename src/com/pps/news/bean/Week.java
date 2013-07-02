package com.pps.news.bean;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.util.SparseArray;

public class Week {

	public static String[] WEEKDAYS = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日",};
	
	public static String[] SHORT_WEEKDAYS = {"周一","周二","周三","周四","周五","周六","周日"};
	
	public static int[] DAY_MAP = {
        Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,  
        Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY,
    };
	
	static SparseArray<String> mWeekDays = new SparseArray<String>(SHORT_WEEKDAYS.length);
	
	static {
		for (int i=0;i < DAY_MAP.length;i++) {
			mWeekDays.put(DAY_MAP[i], SHORT_WEEKDAYS[i]);
		}
	}

	// Returns days of week encoded in an array of booleans.
    public boolean[] getBooleanArray() {
        boolean[] ret = new boolean[7];
        for (int i = 0; i < 7; i++) {
        }
        return ret;
    }

	public static String[] allKeys() {
		String[] keys = new String[mWeekDays.size()];
		for (int i=0;i<mWeekDays.size();i++) {
			keys[i] = String.valueOf(mWeekDays.keyAt(i));
		}
		return keys;
	}
	
	/** 返回值Value追加结果   */
	public static String split(String[] keys, char sperator) {
		return split(Arrays.asList(keys), sperator);
	}
	
	/** 返回值Value追加结果   */
	public static String split(List<String> keys, char sperator) {
		if (keys!=null && keys.size()>0) {
			StringBuilder sb = new StringBuilder();
			for (String key : keys) {
				sb.append(mWeekDays.get(Integer.parseInt(key)));
				sb.append(sperator);
			}
			sb = sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		return null;
	}
	
	public static String join(String[] str, char sperator) {
		return join(Arrays.asList(str), sperator);
	}
	
	
	public static String join(List<String> list, char sperator) {
		if (list!=null && list.size()>0) {
			StringBuilder sb = new StringBuilder();
			for (String t : list) {
				sb.append(t);
				sb.append(sperator);
			}
			sb = sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		return null;
	}
}
