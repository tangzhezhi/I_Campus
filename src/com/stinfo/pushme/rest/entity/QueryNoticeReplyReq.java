package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryNoticeReplyReq extends BaseRequest {
	private String sessionKey = "";
	private int currentPage=1;
	private int showCount=10;
	private String noticeId="";
	
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

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL+"notice/getAnswerList";
//		return AppConstant.BASE_URL + "queryNotice.ashx";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		paramsHashMap.put("showCount", String.valueOf(this.showCount));
		paramsHashMap.put("noticeId", this.noticeId);
		return paramsHashMap;
	}
}
