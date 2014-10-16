package com.stinfo.pushme.entity;

import java.io.Serializable;

import android.text.TextUtils;

import com.stinfo.pushme.util.PinYinUtil;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 5541050827738663565L;
	private String userId = "";
	private String userName = "";
	private String sex = "0";
	private String phone = "";
	private String picUrl = "";
	private String pinYin = "";
	private String initial = "0";
	private String usercode = "";
	private String business = "0";
	private String uservalue="";
	private String userType="";
	private String msgUserType="";
	
	private String schoolLeader="";
	
	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName.trim();
		this.pinYin = PinYinUtil.getPinYin(this.userName);
		if (TextUtils.isEmpty(this.pinYin)) {
			this.initial = "0";
		} else {
			this.initial = String.valueOf(this.pinYin.charAt(0));
		}
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getPinYin() {
		return pinYin;
	}

	public String getInitial() {
		return initial;
	}
	
	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	@Override
	public String toString() {
		return userName;
	}

	public String getUservalue() {
		return uservalue;
	}

	public void setUservalue(String uservalue) {
		this.uservalue = uservalue;
	}

	public String getSchoolLeader() {
		return schoolLeader;
	}

	public void setSchoolLeader(String schoolLeader) {
		this.schoolLeader = schoolLeader;
	}

	public String getMsgUserType() {
		return msgUserType;
	}

	public void setMsgUserType(String msgUserType) {
		this.msgUserType = msgUserType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
}
