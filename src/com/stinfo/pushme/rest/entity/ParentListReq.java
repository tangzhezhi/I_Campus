package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class ParentListReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String classId = "";

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

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "parentList.ashx";
		return AppConstant.School_Platform_BASE_URL + "account/getClassParents";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("classid", this.classId);
		return paramsHashMap;
	}
}
