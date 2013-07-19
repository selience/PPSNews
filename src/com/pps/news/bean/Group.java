package com.pps.news.bean;

import java.util.ArrayList;
import java.util.Collection;
import com.pps.news.parser.ResultType;

/**
 * @file Group.java
 * @create 2013-6-7 下午01:35:40
 * @author lilong dreamxsky@gmail.com
 * @description TODO
 */
@SuppressWarnings("serial")
public class Group<T> extends ArrayList<T> implements ResultType {

	private int total;

	private int total_page;

	private int cur_page;

	public Group() {
		super();
	}

	public Group(Collection<T> collection) {
		super(collection);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public int getCur_page() {
		return cur_page;
	}

	public void setCur_page(int cur_page) {
		this.cur_page = cur_page;
	}
}
