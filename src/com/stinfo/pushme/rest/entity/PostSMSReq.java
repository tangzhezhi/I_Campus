package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PostSMSReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String userList = "";
	private String content = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
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

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "postSMS.ashx";
		return AppConstant.School_Platform_BASE_URL + "message/postSMS";
	}

	@Override
	public HashMap<String, String> toParamsMap() {

		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("sessionKey", this.sessionKey);
		paramsHashMap.put("userList", this.userList);
		paramsHashMap.put("content", this.content);
		return paramsHashMap;
	}
}
