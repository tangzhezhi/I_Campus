package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryDailyReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int queryType = 0;
	private String queryValue = "";
	private String lastCreateTime = "";
	private int currentPage;
	private String parentid= "";
	private String studentid= "";
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
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

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public String getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(String lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryDaily.ashx";
		return AppConstant.School_Platform_BASE_URL + "daily/getDaily";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("authorid", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("parentid", this.parentid);
		paramsHashMap.put("studentid", this.studentid);
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		return paramsHashMap;
	}
}