package com.pps.news.bean;

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

}
