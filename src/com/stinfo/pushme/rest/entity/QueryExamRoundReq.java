package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryExamRoundReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String classId="";
	private String year="";
	private String examDate="";
	private String userType="";
	
	private String atrem="";
	private String name="";
	

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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getExamDate() {
		return examDate;
	}

	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getAtrem() {
		return atrem;
	}

	public void setAtrem(String atrem) {
		this.atrem = atrem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryExamRound.ashx";
		return AppConstant.School_Platform_BASE_URL + "examination/getExamturn";
	}
	
	
	
	
	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("classid", this.classId);
		paramsHashMap.put("ayear", this.year);
		paramsHashMap.put("atrem", this.atrem);
		paramsHashMap.put("name", this.name);
		return paramsHashMap;
	}
}
