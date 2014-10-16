package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PostDailyReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String userList = "";
	private String content = "";
	private String userListType="";
	
	private String parentids;
	private String studentids;

	public String getParentids() {
		return parentids;
	}

	public void setParentids(String parentids) {
		this.parentids = parentids;
	}

	public String getStudentids() {
		return studentids;
	}

	public void setStudentids(String studentids) {
		this.studentids = studentids;
	}

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
	
	
	public String getUserListType() {
		return userListType;
	}

	public void setUserListType(String userListType) {
		this.userListType = userListType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "postDaily.ashx";
		return AppConstant.School_Platform_BASE_URL + "daily/sendDaily";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("authorid", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("parentids", this.parentids);
		paramsHashMap.put("studentids", this.studentids);
//		paramsHashMap.put("userList", "["+this.userList+"]");
//		paramsHashMap.put("userListType", this.userListType);
		paramsHashMap.put("content", this.content);
		return paramsHashMap;
	}
}
