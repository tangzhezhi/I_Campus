package com.stinfo.pushme.rest.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class LastAppVerResp extends BaseResponse {
	private int versionCode = 0;
    private String versionName = "";
    private String updateTime = "";
    private String appUrl = "";

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getAppUrl() {
        return appUrl;
    }
    
    public LastAppVerResp(String jsonStr) throws JSONException {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

    private void parseResponseData(String jsonStr) throws JSONException {
    	JSONObject rootObj = new JSONObject(jsonStr);
    	if (rootObj.has("response")) {
        	JSONObject respObj = rootObj.getJSONObject("response");
            if(respObj.has("versioncode") && !respObj.get("versioncode").equals(null)){
            	this.versionCode = respObj.getInt("versioncode");
            }
            else{
            	this.versionCode = 0;
            }
        	
            if(respObj.has("versionname") && !respObj.get("versionname").equals(null)){
            	 this.versionName = respObj.getString("versionname");
            }
            else{
            	 this.versionName  = "1";
            }
           
            this.updateTime = respObj.getString("updatetime");
            this.appUrl = respObj.getString("appurl");
    	}
    }
}
