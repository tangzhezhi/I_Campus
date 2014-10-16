package com.stinfo.pushme.rest.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.UserInfo;

/**
 * 用户信息 应答字符串 解析成对象
 * @author lenovo
 *
 */
public class UserLoginResp extends BaseResponse {
	private String userType = "-1";
	private String sessionKey = "";
	private UserInfo userInfo = null;
	private String userInfoStr = "";

	public String getSessionKey() {
		return sessionKey;
	}

	public String getUserType() {
		return userType;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	public String getUserInfoStr() {
		return userInfoStr;
	}

	public UserLoginResp(String jsonStr) throws JSONException {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			JSONObject respObj = rootObj.getJSONObject("response");
			this.sessionKey = respObj.getString("sessionkey");
			this.userType = respObj.getString("usertype");
			this.userInfoStr = respObj.getString("userInfo");

			UserInfoParser userParser = new UserInfoParser(this.userType, userInfoStr);
			this.userInfo = userParser.getUserInfo();
		}
	}
}
