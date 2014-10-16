package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryMsgTemplateReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String lastCreateTime = "";
	private String currentPage = "1";
	private String showCount = "10";
	private String schoolNo = "";
	private String groupCode ="";
	
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

	public String getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(String lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}
	
	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getShowCount() {
		return showCount;
	}

	public void setShowCount(String showCount) {
		this.showCount = showCount;
	}

	public String getSchoolNo() {
		return schoolNo;
	}

	public void setSchoolNo(String schoolNo) {
		this.schoolNo = schoolNo;
	}
	
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryMsgTemplate.ashx";
		return AppConstant.School_Platform_BASE_URL + "template/getTemplate";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("addUser", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("groupCode", this.groupCode);
		paramsHashMap.put("currentPage", this.currentPage);
		paramsHashMap.put("showCount", this.showCount);
		paramsHashMap.put("schoolNo", this.schoolNo);
		return paramsHashMap;
	}
}
