package com.pps.news.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;

public class Utility {

	private Utility() {
	}
	
	public static String readComment(Context context) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			InputStream is = context.getAssets().open("comment_sample.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String input = null;
			while ((input = br.readLine())!=null) {
				sb.append(input);
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}
}
