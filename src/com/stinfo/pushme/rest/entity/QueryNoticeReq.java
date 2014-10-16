package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryNoticeReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int queryType = 0;
	private String queryValue = "";
	private String lastCreateTime = "";
	private int currentPage;
	private int showCount;
	private String receivetype="";
	private String receiveobject="";
	private String type="";
	private String authorid="";
	
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}

	public String getReceivetype() {
		return receivetype;
	}

	public void setReceivetype(String receivetype) {
		this.receivetype = receivetype;
	}

	public String getReceiveobject() {
		return receiveobject;
	}

	public void setReceiveobject(String receiveobject) {
		this.receiveobject = receiveobject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL+"notice/getNotice";
//		return AppConstant.BASE_URL + "queryNotice.ashx";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		paramsHashMap.put("type", this.type);
		paramsHashMap.put("authorid", this.userId);
//		if(this.type.equals("3")||this.type.equals("2")){
//			paramsHashMap.put("authorid", this.userId);
//		}
		
		paramsHashMap.put("receivetype", this.receivetype);
		paramsHashMap.put("receiveobject", this.receiveobject);
		return paramsHashMap;
	}
}
