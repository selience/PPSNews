package com.pps.news.bean;

import org.json.JSONException;
import org.json.JSONObject;
import com.pps.news.parser.ResultType;

public class Result {

	private int code;
	private String error;
	private String message;
	private ResultType data;
	private Exception exception;

	
	public void addResult(ResultType result) {
		this.data = result;
	}

	public ResultType getValue() {
		return data;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	// 解析出状态码
	public void setErrorCode() {
		if (message!=null && message.length()>0) {
			try {
				JSONObject json = new JSONObject(message);
				if (json.has("retcode")) {
					setCode(json.getInt("retcode"));
				}
				if (json.has("message")) {
					setError(json.getString("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
}
