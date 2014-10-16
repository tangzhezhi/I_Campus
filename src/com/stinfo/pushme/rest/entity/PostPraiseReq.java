package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

/**
 * 新增点赞
 * @author lenovo
 */
public class PostPraiseReq extends BaseRequest {
	private String sessionKey = "";
	private String behaviorid = "";  // 关联字段
	private String operid = ""; 	//点赞人 登陆用户ID
	private String opername=""; //点赞人 登陆用户名
	
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

	public String getOperid() {
		return operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public String getOpername() {
		return opername;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL + "behavior/setPraise";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("behaviorid", this.behaviorid);
		paramsHashMap.put("operid", this.operid);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("opername", this.opername);
		return paramsHashMap;
	}
}
