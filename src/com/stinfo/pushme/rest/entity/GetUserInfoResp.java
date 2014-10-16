package com.stinfo.pushme.rest.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.UserInfo;

public class GetUserInfoResp extends BaseResponse {
	private String userType = "-1";
	private UserInfo userInfo = null;

	public String getUserType() {
		return userType;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public GetUserInfoResp(String jsonStr) throws JSONException {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			JSONObject respObj = rootObj.getJSONObject("response");
			this.userType = respObj.getString("userType");

			String userInfo = respObj.getString("userInfo");
			UserInfoParser userParser = new UserInfoParser(this.userType, userInfo);
			this.userInfo = userParser.getUserInfo();
		}
	}
}
