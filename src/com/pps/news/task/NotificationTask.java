package com.pps.news.task;

import java.util.Observable;

public class NotificationTask extends Observable {

	/** 添加评论  */
	public static final String NOTIFICATION_ADD_COMMENT_TASK = "NOTIFICATION_ADD_COMMENT_TASK";
	/** 删除评论*/
	public static final String NOTIFICATION_DELETE_COMMENT_TASK = "NOTIFICATION_DELETE_COMMENT_TASK";
	
	
	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	
	public static class TaskData {
		
		private String key;
		
		private Object value;
		
		public TaskData(String _key, Object _value) {
			this.key = _key;
			this.value = _value;
		}
		
		public String getKey() {
			return key;
		}
		
		public Object getValue() {
			return value;
		}
	}
	
}
