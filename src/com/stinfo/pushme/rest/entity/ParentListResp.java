package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;

public class ParentListResp {
	private static final String TAG = "ParentListResp";
	private int ack = 0;
	private ArrayList<ParentRoster> parentList = new ArrayList<ParentRoster>();
	private ClassInfo classInfo;

    public int getAck() {
        return ack;
    }
		
	public ArrayList<ParentRoster> getParentList() {
		return parentList;
	}

	public ParentListResp(String jsonStr, ClassInfo classInfo) throws JSONException {
		this.classInfo = classInfo;
		parseParentRoster(jsonStr);
	}
	
	private void parseParentRoster(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");
		
		if (rootObj.has("response")) {
			JSONArray userArray = rootObj.getJSONArray("response");
//			JSONArray userArray = respObj.getJSONArray("parentList");
			for (int i = 0; i < userArray.length(); i++) {
				try {
					JSONObject userObj = userArray.getJSONObject(i);
					ParentRoster parentRoster = new ParentRoster();
					parentRoster.setUserId(userObj.getString("id"));
					parentRoster.setUsercode(userObj.getString("usercode"));
					parentRoster.setUserName(userObj.getString("username"));
					parentRoster.setUservalue(userObj.getString("uservalue"));
					parentRoster.setParentValue(userObj.getString("parentValue"));
					
					if(userObj.has("business") || userObj.get("business").equals(null)){
						parentRoster.setBusiness("0");
					}
					else{
						parentRoster.setBusiness(userObj.getString("business"));
					}
					
					parentRoster.setSex((!userObj.has("sex")?"":userObj.getString("sex")));
					parentRoster.setPhone(!userObj.has("phone")?"":userObj.getString("phone"));
					parentRoster.setPicUrl((!userObj.has("picurl")?"":userObj.getString("picurl")));
					
					
					JSONArray children = userObj.getJSONArray("children");
					if(children!=null){
						JSONObject child = children.getJSONObject(0);
						parentRoster.setChildUserId(child.getString("id"));
						parentRoster.setChildName(child.getString("username"));
					}
					
					parentRoster.setSchoolId(this.classInfo.getSchoolId());
					parentRoster.setClassId(this.classInfo.getClassId());
					parentRoster.setClassName(this.classInfo.getClassName());
					this.parentList.add(parentRoster);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "解析家长花名册出错: " + e);
					continue;
				}
			}
		}
	}
}
