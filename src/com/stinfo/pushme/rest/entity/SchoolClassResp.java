package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.TeacherRole;

public class SchoolClassResp {
	private static final String TAG = "SchoolClassResp";
	private int ack = 0;
	private ArrayList<TeacherRole> teacherRoleList =null;

    public int getAck() {
        return ack;
    }
		
	public SchoolClassResp(String jsonStr) throws JSONException {
		parseSchoolClass(jsonStr);
	}
	
	public ArrayList<TeacherRole> getTeacherRoleList() {
		return teacherRoleList;
	}

	public void setTeacherRoleList(ArrayList<TeacherRole> teacherRoleList) {
		this.teacherRoleList = teacherRoleList;
	}

	private void parseSchoolClass(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");
		
		if (rootObj.has("response")) {
			teacherRoleList = new ArrayList<TeacherRole>();
			JSONArray userArray = rootObj.getJSONArray("response");
			for (int i = 0; i < userArray.length(); i++) {
				try {
					JSONObject userObj = userArray.getJSONObject(i);
					TeacherRole classInfo = new TeacherRole();
					classInfo.setClassId(userObj.getString("classid"));
					classInfo.setClassName(userObj.getString("classname"));
					this.teacherRoleList.add(classInfo);
				} catch (JSONException e) {
					Log.e(TAG, "解析学校班级出错: " + e);
					continue;
				}
			}
		}
	}
}
