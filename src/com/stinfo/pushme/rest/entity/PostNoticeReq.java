package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PostNoticeReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int type = 0;
	private String objectId = "";
	private String title = "";
	private String content = "";
	private String receiversId="";
	private String receivetype="";
	private String receiveobject="";
	private String receiveobjects="";
	

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "postNotice.ashx";
		return AppConstant.School_Platform_BASE_URL + "notice/sendNotice";
	}
	
	
	public String getReceiversId() {
		return receiversId;
	}

	public void setReceiversId(String receiversId) {
		this.receiversId = receiversId;
	}

	public String getReceivetype() {
		return receivetype;
	}

	public void setReceivetype(String receivetype) {
		this.receivetype = receivetype;
	}

	public String getReceiveobject() {
		return receiveobject;
	}

	public void setReceiveobject(String receiveobject) {
		this.receiveobject = receiveobject;
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("authorid", this.userId);
		paramsHashMap.put("type", String.valueOf(this.type));
		
		if(this.type==2){
			paramsHashMap.put("receiveobjects",this.receiveobject);
		}
		else{
			paramsHashMap.put("receiveobject",this.receiveobject);
		}
		paramsHashMap.put("receivetype",this.receivetype);
		paramsHashMap.put("title", this.title);
		paramsHashMap.put("content", this.content);
		return paramsHashMap;
	}
}
