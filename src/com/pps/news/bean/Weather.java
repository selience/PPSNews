package com.pps.news.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.pps.news.parser.ResultType;
import com.pps.news.util.CacheUtil;

public class Weather implements ResultType {

	private String tempOneTemp;
	private String tempOneImg;
	private String tempOneInfo;
	private String tempTwoTemp;
	private String tempTwoImg;
	private String tempTwoInfo;
	private String tempThirdTemp;
	private String tempThirdImg;
	private String tempThirdInfo;
	private String info;
	private String tempInfo;
	private String time;
	private String img;
	private String pm;
	private String pmInfo;

	public String getTempOneImg() {
		return tempOneImg;
	}

	public void setTempOneImg(String tempOneImg) {
		this.tempOneImg = tempOneImg;
	}

	public String getTempTwoImg() {
		return tempTwoImg;
	}

	public void setTempTwoImg(String tempTwoImg) {
		this.tempTwoImg = tempTwoImg;
	}

	public String getTempThirdImg() {
		return tempThirdImg;
	}

	public void setTempThirdImg(String tempThirdImg) {
		this.tempThirdImg = tempThirdImg;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getPmInfo() {
		return pmInfo;
	}

	public void setPmInfo(String pmInfo) {
		this.pmInfo = pmInfo;
	}

	public String getTempOneTemp() {
		return tempOneTemp;
	}

	public void setTempOneTemp(String tempOneTemp) {
		this.tempOneTemp = tempOneTemp;
	}

	public String getTempTwoTemp() {
		return tempTwoTemp;
	}

	public void setTempTwoTemp(String tempTwoTemp) {
		this.tempTwoTemp = tempTwoTemp;
	}

	public String getTempThirdTemp() {
		return tempThirdTemp;
	}

	public void setTempThirdTemp(String tempThirdTemp) {
		this.tempThirdTemp = tempThirdTemp;
	}

	public String getTempInfo() {
		return tempInfo;
	}

	public void setTempInfo(String tempInfo) {
		this.tempInfo = tempInfo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTempOneInfo() {
		return tempOneInfo;
	}

	public void setTempOneInfo(String tempOneInfo) {
		this.tempOneInfo = tempOneInfo;
	}

	public String getTempTwoInfo() {
		return tempTwoInfo;
	}

	public void setTempTwoInfo(String tempTwoInfo) {
		this.tempTwoInfo = tempTwoInfo;
	}

	public String getTempThirdInfo() {
		return tempThirdInfo;
	}

	public void setTempThirdInfo(String tempThirdInfo) {
		this.tempThirdInfo = tempThirdInfo;
	}

	public void writeToOutputStream(DataOutputStream out) throws IOException {
		CacheUtil.writeString(out, tempOneTemp);
		CacheUtil.writeString(out, tempOneImg);
		CacheUtil.writeString(out, tempOneInfo);
		CacheUtil.writeString(out, tempTwoTemp);
		CacheUtil.writeString(out, tempTwoImg);
		CacheUtil.writeString(out, tempTwoInfo);
		CacheUtil.writeString(out, tempThirdTemp);
		CacheUtil.writeString(out, tempThirdImg);
		CacheUtil.writeString(out, tempThirdInfo);
		CacheUtil.writeString(out, info);
		CacheUtil.writeString(out, tempInfo);
		CacheUtil.writeString(out, time);
		CacheUtil.writeString(out, img);
		CacheUtil.writeString(out, pm);
		CacheUtil.writeString(out, pmInfo);
	}

	public Weather readFromInputStream(DataInputStream in) throws IOException {
		tempOneTemp = CacheUtil.readString(in);
		tempOneImg = CacheUtil.readString(in);
		tempOneInfo = CacheUtil.readString(in);
		tempTwoTemp = CacheUtil.readString(in);
		tempTwoImg = CacheUtil.readString(in);
		tempTwoInfo = CacheUtil.readString(in);
		tempThirdTemp = CacheUtil.readString(in);
		tempThirdImg = CacheUtil.readString(in);
		tempThirdInfo = CacheUtil.readString(in);
		info = CacheUtil.readString(in);
		tempInfo = CacheUtil.readString(in);
		time = CacheUtil.readString(in);
		img = CacheUtil.readString(in);
		pm = CacheUtil.readString(in);
		pmInfo = CacheUtil.readString(in);
		return this;
	}
}
