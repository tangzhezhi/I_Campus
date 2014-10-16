package com.stinfo.pushme.entity;

import java.io.Serializable;

public class SMS implements Serializable {
	private static final long serialVersionUID = -7924820638792633505L;
    
    private int smsId;
	private String userList;
	private String content;
	private String createTime;
	
	public int getSmsId() {
		return smsId;
	}

	public void setSmsId(int smsId) {
		this.smsId = smsId;
	}

	public String getUserList() {
		return userList;
	}

	public void setUserList(String userList) {
		this.userList = userList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
