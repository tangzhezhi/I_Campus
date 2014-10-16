package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PostNoticeReplyReq extends BaseRequest {
	private String authorid  = "";
	private String sessionKey = "";
	private String noticeId= "";
	private String content= "";


	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "postNotice.ashx";
		return AppConstant.School_Platform_BASE_URL + "notice/answer";
	}
	

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("authorid", this.authorid);
		paramsHashMap.put("content", this.content);
		paramsHashMap.put("noticeId", this.noticeId);
		return paramsHashMap;
	}
}
