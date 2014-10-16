package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryDeedReplyReq extends BaseRequest {
	private String sessionKey = "";
	private int currentPage=1;
	private int showCount=10;
	private String behaviorid="";
	
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
		return AppConstant.School_Platform_BASE_URL+"behavior/getComments";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		paramsHashMap.put("showCount", String.valueOf(this.showCount));
		paramsHashMap.put("behaviorid", this.behaviorid);
		return paramsHashMap;
	}
}
