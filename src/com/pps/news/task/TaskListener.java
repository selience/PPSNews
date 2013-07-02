/**
 * 
 */
package com.pps.news.task;

import com.pps.news.bean.Result;

/**
 * @file TaskListener.java
 * @create 2012-10-11 下午5:13:03
 * @author Jacky.Lee
 * @description TODO
 */
public interface TaskListener {

	
	void onTaskStart(String taskName);
	
	void onTaskFinished(String taskName, Result result);
}
