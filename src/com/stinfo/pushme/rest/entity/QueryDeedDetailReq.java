package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryDeedDetailReq extends BaseRequest {
	private String sessionKey = "";
	private String behaviorid="";
	

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getBehaviorid() {
		return behaviorid;
	}

	public void setBehaviorid(String behaviorid) {
		this.behaviorid = behaviorid;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL+"behavior/getBehaviorDetail";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("behaviorid", this.behaviorid);
		return paramsHashMap;
	}
}
