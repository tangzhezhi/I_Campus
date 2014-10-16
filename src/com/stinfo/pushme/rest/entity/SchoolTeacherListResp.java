package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.stinfo.pushme.entity.TeacherRoster;

public class SchoolTeacherListResp {
	private int ack = 0;
	private ArrayList<TeacherRoster> teacherList = new ArrayList<TeacherRoster>();
	private String schoolId;

	public int getAck() {
		return ack;
	}

	public ArrayList<TeacherRoster> getTeacherList() {
		return teacherList;
	}

	public SchoolTeacherListResp(String jsonStr, String schoolId) throws JSONException {
		this.schoolId = schoolId;
		parseTeacherRoster(jsonStr);
	}

	private void parseTeacherRoster(String jsonStr) throws JSONException {
		try {
			JSONObject rootObj = new JSONObject(jsonStr);
			this.ack = rootObj.getInt("ack");

			if (rootObj.has("response")) {
//			JSONObject respObj = rootObj.getJSONObject("response");
				JSONArray userArray = rootObj.getJSONArray("response");
				for (int i = 0; i < userArray.length(); i++) {
					
					
					try {
						JSONObject userObj = userArray.getJSONObject(i);
						TeacherRoster teacherRoster = new TeacherRoster();
						
						JSONArray classes = userObj.getJSONArray("classes");
						if(classes.length() > 0 ){
							JSONObject classesDesc = classes.getJSONObject(0);
							teacherRoster.setClassId(classesDesc.get("classid")==null?"":classesDesc.getString("classid"));
							teacherRoster.setClassName(classesDesc.get("classname")==null?"":classesDesc.getString("classname"));
						}
						
						String rolename = "";
						String rolecode = "";
						
						
						
						if( userObj.get("roles")!=null && !userObj.get("roles").equals(null)){
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
						

						
						teacherRoster.setUserId(userObj.get("id")==null?"":userObj.getString("id"));
						teacherRoster.setUsercode(userObj.get("usercode")==null?"":userObj.getString("usercode"));
						teacherRoster.setUserName(userObj.get("username")==null?"":userObj.getString("username"));
						teacherRoster.setUservalue(userObj.get("uservalue")==null?"":userObj.getString("uservalue"));
						teacherRoster.setBusiness("1");
						
						
						teacherRoster.setTeacherId(userObj.get("uservalue")==null?"":userObj.getString("uservalue"));
						teacherRoster.setSex(userObj.get("sex")==null?"":userObj.getString("sex"));
						teacherRoster.setPhone(userObj.get("phone")==null?"":userObj.getString("phone"));
						teacherRoster.setPicUrl(userObj.get("picurl")==null?"":userObj.getString("picurl"));
						teacherRoster.setSchoolId(this.schoolId);
						this.teacherList.add(teacherRoster);
					} catch (Exception e) {
						Log.e("SchoolTeacherListResp", "parseTeacherRoster：解析出错"+e);
						continue;
					}
					
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new JSONException("parseTeacherRoster：解析出错"+e);
		}
	}
}
