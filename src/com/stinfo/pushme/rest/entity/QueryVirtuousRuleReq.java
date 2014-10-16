package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryVirtuousRuleReq extends BaseRequest {
	private String sessionkey;
	private String schoolno;
	
	public String getSessionKey() {
		return sessionkey;
	}

	public void setSessionKey(String sessionkey) {
		this.sessionkey = sessionkey;
	}

	public String getSchoolno() {
		return schoolno;
	}

	public void setSchoolno(String schoolno) {
		this.schoolno = schoolno;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryHomeWork.ashx";
		return AppConstant.School_Platform_BASE_URL + "jyb/getMoralDetails";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionkey);
		paramsHashMap.put("schoolno", this.schoolno);
		return paramsHashMap;
	}

}
