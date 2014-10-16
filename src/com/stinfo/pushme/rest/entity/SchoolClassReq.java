package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class SchoolClassReq extends BaseRequest {
	private String sessionKey = "";
	private String schoolno = "";


	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSchoolno() {
		return schoolno;
	}

	public void setSchoolno(String schoolno) {
		this.schoolno = schoolno;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL + "account/getSchoolClass";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("schoolno", this.schoolno);
		return paramsHashMap;
	}
}
