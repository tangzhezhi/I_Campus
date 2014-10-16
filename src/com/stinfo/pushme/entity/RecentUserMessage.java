package com.stinfo.pushme.entity;

import java.io.Serializable;

public class RecentUserMessage implements Serializable {
	private static final long serialVersionUID = 8379565407259427241L;
	
	private String cacheUserId="";
	private String objectId = "";
	private String objectType = "0";
	private int groupType = 0;
	private String content = "";
	private int unreadCount = 0;
	private String updateTime = "";
	private String userName = "";
	private String sex = "1";
	private String phone = "";
	private String picUrl = "";
	private String userType = "";
	private String uservalue = "";
	
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if (userName != null) {
			this.userName = userName;
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
		if (phone != null) {
			this.phone = phone;
		}
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		if (picUrl != null) {
			this.picUrl = picUrl;
		}
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCacheUserId() {
		return cacheUserId;
	}

	public void setCacheUserId(String cacheUserId) {
		this.cacheUserId = cacheUserId;
	}

	public String getUservalue() {
		return uservalue;
	}

	public void setUservalue(String uservalue) {
		this.uservalue = uservalue;
	}
	
}
