package com.pps.news.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pps.news.bean.Group;


/**
 * @file Parser.java
 * @create 2013-6-7 下午01:34:16
 * @author lilong dreamxky@gmail.com
 * @description TODO
 */
public abstract class Parser<T> {

	public abstract T parse(JSONObject json) throws JSONException;
	
	
	public Group<T> parse(JSONArray array) throws JSONException {
        throw new JSONException("Unexpected JSONArray parse type encountered.");
    }
}