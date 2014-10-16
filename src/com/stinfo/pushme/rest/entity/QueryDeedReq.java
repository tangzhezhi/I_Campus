package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryDeedReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int currentPage;
	private String showCount = "10";
	private String plates="1";
	private String sort= "id";
	private String schoolcode="";
	
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

	public String getPlates() {
		return plates;
	}

	public void setPlates(String plates) {
		this.plates = plates;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSchoolcode() {
		return schoolcode;
	}

	public void setSchoolcode(String schoolcode) {
		this.schoolcode = schoolcode;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL+"behavior/queryBehavior";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("schoolcode", this.schoolcode);
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		paramsHashMap.put("showCount", this.showCount);
		paramsHashMap.put("plates", this.plates);
		paramsHashMap.put("sort", this.sort);
		return paramsHashMap;
	}
}
