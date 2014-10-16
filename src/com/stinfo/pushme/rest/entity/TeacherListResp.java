package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.TeacherRoster;

public class TeacherListResp {
	private int ack = 0;
	private ArrayList<TeacherRoster> teacherList = new ArrayList<TeacherRoster>();
	private ClassInfo classInfo;

    public int getAck() {
        return ack;
    }
		
	public ArrayList<TeacherRoster> getTeacherList() {
		return teacherList;
	}

	public TeacherListResp(String jsonStr, ClassInfo classInfo) throws JSONException {
		this.classInfo = classInfo;
		parseTeacherRoster(jsonStr);
	}
	
	private void parseTeacherRoster(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");
		
		if (rootObj.has("response")) {
//			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray userArray = rootObj.getJSONArray("response");
			for (int i = 0; i < userArray.length(); i++) {
				
				try {
					JSONObject userObj = userArray.getJSONObject(i);
					TeacherRoster teacherRoster = new TeacherRoster();
					
					teacherRoster.setUserId(userObj.getString("id"));
					teacherRoster.setUserName(userObj.getString("username"));
					teacherRoster.setTeacherId(userObj.getString("uservalue"));
					teacherRoster.setUservalue(userObj.getString("uservalue"));
					
					teacherRoster.setBusiness("1");
					
					
					teacherRoster.setSex(userObj.getString("sex"));
					teacherRoster.setPhone(userObj.getString("phone"));
					teacherRoster.setPicUrl(userObj.getString("picurl")==null?"":userObj.getString("picurl"));
					
					String rolename = "";
					String rolecode = "";
					
					
					
					if(userObj.get("roles")!=null && !userObj.get("roles").equals(null)){
						
						if( userObj.get("roles") instanceof JSONArray){
							JSONArray roles = userObj.getJSONArray("roles");
							
							
							for (int j = 0; j < roles.length(); j++) {
								JSONObject rolesDesc = roles.getJSONObject(j);
								rolename = rolename + rolesDesc.getString("rolename") + ",";
								rolecode = rolecode + rolesDesc.getString("rolecode") + ",";
							}
							
							if(rolecode.length() > 0){
								rolename = rolename.substring(0, rolename.length()-1);
								rolecode = rolecode.substring(0, rolecode.length()-1);
							}
							
							teacherRoster.setRolename(rolename);
							teacherRoster.setRolecode(rolecode);
						}
					}
					
					teacherRoster.setSchoolId(this.classInfo.getSchoolId());
					teacherRoster.setClassId(this.classInfo.getClassId());
					teacherRoster.setClassName(this.classInfo.getClassName());
					this.teacherList.add(teacherRoster);
				} catch (Exception e) {
					Log.d("TeacherListResp", "parseTeacherRoster解析异常"+e);
					continue;
				}
				
			}
		}
	}
}
