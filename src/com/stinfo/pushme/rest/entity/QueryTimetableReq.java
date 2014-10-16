package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryTimetableReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String classId;
	private String startDate;
	private String endate;

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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndate() {
		return endate;
	}

	public void setEndate(String endate) {
		this.endate = endate;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryTimetable.ashx";
		return AppConstant.School_Platform_BASE_URL + "course/getClassCourses";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
//		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("classid", this.classId);
//		paramsHashMap.put("startDate", this.startDate);
//		paramsHashMap.put("endate", this.endate);
		return paramsHashMap;
	}
}
