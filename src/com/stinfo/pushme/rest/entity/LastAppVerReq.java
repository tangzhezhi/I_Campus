package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class LastAppVerReq extends BaseRequest {

	private String appName = "";
	private int appType = 1;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "lastAppVer.ashx";
		return AppConstant.School_Platform_BASE_URL + "appVersion/nonChecked/lastAppVer";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("appname", this.appName);
		paramsHashMap.put("apptype", "1");
		return paramsHashMap;
	}
}
