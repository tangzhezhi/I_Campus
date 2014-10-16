package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class SchoolTeacherListReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String schoolId = "";

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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "schoolTeacherList.ashx";
		return AppConstant.School_Platform_BASE_URL + "account/getSchoolTeachers";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("schoolno", this.schoolId);
		return paramsHashMap;
	}
}
