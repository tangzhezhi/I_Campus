package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PushAccountReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String pushUserId = "";
	private String pushChannelId = "";
	private int deviceType = 0;

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

	public String getPushUserId() {
		return pushUserId;
	}

	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}

	public String getPushChannelId() {
		return pushChannelId;
	}

	public void setPushChannelId(String pushChannelId) {
		this.pushChannelId = pushChannelId;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "pushAccount.ashx";
		return AppConstant.School_Platform_BASE_URL + "accountPush/pushAccount";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userid", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("channelId", this.pushChannelId);
		paramsHashMap.put("baiDuUserId", this.pushUserId);
		paramsHashMap.put("devicetype", String.valueOf(this.deviceType));
		return paramsHashMap;
	}
}
