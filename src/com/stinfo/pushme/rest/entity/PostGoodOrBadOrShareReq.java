package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

/**
 * 闪光台/曝光台/爱心分享请求
 * @author lenovo
 */
public class PostGoodOrBadOrShareReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String title = "";
	private String content = "";
	private String plates="1";
	private String releaseoperid = "";
	private String releaseusername="";
	private String type = "2";
	private String picPathArr = "";
	
	private String schoolcode ="";
	
	public String getSchoolcode() {
		return schoolcode;
	}

	public void setSchoolcode(String schoolcode) {
		this.schoolcode = schoolcode;
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


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlates() {
		return plates;
	}

	public void setPlates(String plates) {
		this.plates = plates;
	}

	public String getReleaseoperid() {
		return releaseoperid;
	}

	public void setReleaseoperid(String releaseoperid) {
		this.releaseoperid = releaseoperid;
	}

	public String getReleaseusername() {
		return releaseusername;
	}

	public void setReleaseusername(String releaseusername) {
		this.releaseusername = releaseusername;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPicPathArr() {
		return picPathArr;
	}

	public void setPicPathArr(String picPathArr) {
		this.picPathArr = picPathArr;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL + "behavior/setBehavior";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("schoolcode", this.schoolcode);
		paramsHashMap.put("releaseoperid", this.userId);
		paramsHashMap.put("releaseusername", this.releaseusername);
		paramsHashMap.put("sessionkey", this.sessionKey);
		
		paramsHashMap.put("title", this.title);
		paramsHashMap.put("type", this.type);
		paramsHashMap.put("plates", this.plates);
		paramsHashMap.put("picPathArr", this.picPathArr);
		paramsHashMap.put("content", this.content);
		return paramsHashMap;
	}
}
