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
public class Group<T> extends ArrayList<T> implements ResultType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8991180342373777435L;

	
	public Group() {
		super();
	}

	public Group(Collection<T> collection) {
		super(collection);
	}

}
