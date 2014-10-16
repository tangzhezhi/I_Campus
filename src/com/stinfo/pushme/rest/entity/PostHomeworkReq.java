package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PostHomeworkReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String subject = "";
	private String classId = "";
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "postHomeWork.ashx";
		return AppConstant.School_Platform_BASE_URL + "homework/sendHomework";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("authorid", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("subject", this.subject);
		paramsHashMap.put("classid", this.classId);
		paramsHashMap.put("content", this.content);
		return paramsHashMap;
	}
}
