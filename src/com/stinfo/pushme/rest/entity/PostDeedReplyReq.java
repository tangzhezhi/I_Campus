package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PostDeedReplyReq extends BaseRequest {
	private String sessionKey = "";
	private String content= "";
	private String behaviorid="";
	private String coperid="";
	private String cusername="";
	private String replyoperid="";
	private String replyusername="";
	
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
	
	public String getBehaviorid() {
		return behaviorid;
	}

	public void setBehaviorid(String behaviorid) {
		this.behaviorid = behaviorid;
	}

	public String getCoperid() {
		return coperid;
	}

	public void setCoperid(String coperid) {
		this.coperid = coperid;
	}

	public String getCusername() {
		return cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getReplyoperid() {
		return replyoperid;
	}

	public void setReplyoperid(String replyoperid) {
		this.replyoperid = replyoperid;
	}

	public String getReplyusername() {
		return replyusername;
	}

	public void setReplyusername(String replyusername) {
		this.replyusername = replyusername;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL + "behavior/setComment";
	}
	

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("coperid", this.coperid);
		paramsHashMap.put("cusername", this.cusername);
		paramsHashMap.put("behaviorid", this.behaviorid);
		paramsHashMap.put("replyoperid", this.replyoperid);
		paramsHashMap.put("replyusername", this.replyusername);
		paramsHashMap.put("comment", this.content);
		return paramsHashMap;
	}
}
