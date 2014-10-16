package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryMsgTemplateGroupReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String lastCreateTime = "";
	private String schoolNo;
	
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
	
	public String getSchoolNo() {
		return schoolNo;
	}

	public void setSchoolNo(String schoolNo) {
		this.schoolNo = schoolNo;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryMsgTemplate.ashx";
		return AppConstant.School_Platform_BASE_URL + "template/getTemplateGroup";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("addUser", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("schoolNo", this.schoolNo);
		return paramsHashMap;
	}
}
