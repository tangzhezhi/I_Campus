package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;


public class UserLoginReq extends BaseRequest {
	private String userId = "";
	private String password = "";
	private String usercode="";
	
	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "userLogin.ashx";
		return AppConstant.School_Platform_BASE_URL + "account/nonChecked/login";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("usercode", this.usercode);
		paramsHashMap.put("password", this.password);
		return paramsHashMap;
	}
}
