package com.pps.news.task;

import com.pps.news.bean.Result;

import android.os.AsyncTask;

/**
 * @file GenericTask.java
 * @create 2013-4-9 下午03:10:17
 * @author lilong dreamxsky@gmail.com
 * @description TODO 异步任务处理
 */
public abstract class GenericTask extends AsyncTask<String, Integer, Result> {

	protected String taskName;
	private TaskListener mListener = null;

	public GenericTask(TaskListener taskListener, String taskName) {
		this.taskName = taskName;
		this.mListener = taskListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mListener != null) {
			mListener.onTaskStart(taskName);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (mListener != null) {
			mListener.onTaskFinished(taskName, result);
		}
	}

	public void cancelTask(boolean isCancelable) {
		if (isCancelable && getStatus() == Status.RUNNING) {
			cancel(true);
		}
	}

	@Override
	abstract protected Result doInBackground(String... params);

	
	protected String getTaskName() {
		return this.taskName;
	}
}
