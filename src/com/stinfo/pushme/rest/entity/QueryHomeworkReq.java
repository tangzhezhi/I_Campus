package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryHomeworkReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int queryType = 0;
	private String queryValue = "";
	private String subject = "";
	private String lastCreateTime = "";
	private String classid;
	private String createtime;
	private int currentPage;
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(String lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryHomeWork.ashx";
		return AppConstant.School_Platform_BASE_URL + "homework/getHomework";
		
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		
		if(this.userId!=null && !this.userId.equals("")){
			paramsHashMap.put("authorid", this.userId);
		}
		
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("classid", this.classid);
		paramsHashMap.put("subject", this.subject);
		return paramsHashMap;
	}
}
