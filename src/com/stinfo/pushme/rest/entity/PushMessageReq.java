package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PushMessageReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String userList = "";
	private String content = "";
	private String operator = "";
	private String type = "1";
	private String receiveType = "02";
	private String userIds = "";
	
	
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "pushMsg.ashx";
		return AppConstant.School_Platform_BASE_URL + "message/pushPersonalMessage";
	}

	@Override
	public HashMap<String, String> toParamsMap() {

		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("operator", this.operator);
		paramsHashMap.put("type", this.type);
		paramsHashMap.put("receiveType", this.receiveType);
		paramsHashMap.put("sms", this.content);
		return paramsHashMap;
	}
}
