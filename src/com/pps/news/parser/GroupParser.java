package com.pps.news.parser;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pps.news.bean.Group;

public class GroupParser<T> {

	private Parser<T> mSubParser;

	public GroupParser(Parser<T> subParser) {
		mSubParser = subParser;
	}

	public Group<T> parse(JSONObject json) throws JSONException {
		Group<T> group = new Group<T>();
		Iterator<String> it = (Iterator<String>) json.keys();
		while (it.hasNext()) {
			String key = it.next();
			if (key.equals("items")) {
				Object obj = json.get(key);
				if (obj instanceof JSONArray) {
					parse(group, (JSONArray) obj);
				}
			}
		}
		return group;
	}

	public Group<T> parse(JSONArray array) throws JSONException {
		Group<T> group = new Group<T>();
		parse(group, array);
		return group;
	}

	private void parse(Group<T> group, JSONArray array) {
		for (int i = 0, m = array.length(); i < m; i++) {
			try {
				Object element = array.get(i);
				Object item = mSubParser.parse((JSONObject) element);
				if (item != null) {
					group.add((T)item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
