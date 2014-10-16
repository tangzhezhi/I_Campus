package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.stinfo.pushme.common.AppConstant.SchoolLeader;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.SchoolInfo;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.SystemUser;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;

public class UserInfoParser {
	private static final String TAG = "UserInfoParser";
	private UserInfo userInfo = null;
	
	private SchoolInfo schoolInfo = null;
	
	public SchoolInfo getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(SchoolInfo schoolInfo) {
		this.schoolInfo = schoolInfo;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public UserInfoParser(String userType, String userInfo) throws JSONException {
		Log.d(TAG, "Begin UserInfoParser! \tuserType=" + String.valueOf(userType) + "|userInfo=" + userInfo);
		if (TextUtils.isEmpty(userInfo)) {
			throw new JSONException("It's invalid json data");
		}

		JSONObject userObj = new JSONObject(userInfo);
		if (userType.equals(UserType.SYSTEM) ) {
			this.userInfo = parseSysUserInfo(userObj);
		} else if (userType.equals(UserType.STUDENT)){
			this.userInfo = parseStudentInfo(userObj);
		} else if (userType.equals(UserType.PARENT)) {
			this.userInfo = parseParentInfo(userObj);
		} else if (userType.equals(UserType.TEACHER)) {
			this.userInfo = parseTeacherInfo(userObj);
		} else {
			throw new JSONException("Invalid userType!");
		}
	}

	private SystemUser parseSysUserInfo(JSONObject userObj) throws JSONException {
		SystemUser sysUser = new SystemUser();
		sysUser.setUserId(userObj.getString("userId"));
		sysUser.setUserName(userObj.getString("userName"));
		sysUser.setPicUrl(userObj.getString("picUrl"));
		return sysUser;
	}

	private Student parseStudentInfo(JSONObject userObj) throws JSONException {
		Student student = new Student();

		student.setUserId(userObj.getString("id"));
		student.setUsercode(userObj.getString("usercode"));
		student.setUserName(userObj.getString("username"));
		student.setUservalue(userObj.getString("uservalue"));
		student.setUserType(userObj.getString("usertype"));
		if(userObj.has("school") && !userObj.get("school").equals(null)){
			JSONObject schoolInfo = new JSONObject(userObj.getString("school"));
			student.setSchoolId(schoolInfo.getString("schoolno"));
			student.setSchoolname(schoolInfo.getString("schoolname"));
		}
		
		if(userObj.has("studentno") || userObj.get("studentno").equals(null)){
			student.setStudentNo("");
		}
		else{
			student.setStudentNo(String.valueOf(userObj.get("studentno")));
		}
		
		if(userObj.has("sex") || userObj.get("sex").equals(null)){
			student.setSex("");
		}
		else{
			student.setSex(String.valueOf(userObj.get("sex")));
		}
		
		if(userObj.has("phone") || userObj.get("phone").equals(null)){
			student.setPhone("");
		}
		else{
			student.setPhone(String.valueOf(userObj.get("phone")));
		}
		
		if(userObj.has("picurl") || userObj.get("picurl").equals(null)){
			student.setPicUrl("");
		}
		else{
			student.setPicUrl(String.valueOf(userObj.get("picurl")));
		}
		
//		student.setStudentNo(String.valueOf(!userObj.has("studentno")?"":userObj.get("studentno")));
		
//		student.setSex(String.valueOf(!userObj.has("sex")?"0":userObj.getString("sex")));
//		student.setPhone(String.valueOf(!userObj.has("phone")?"":userObj.get("phone")));
//		student.setPicUrl(String.valueOf(!userObj.has("picurl")?"":userObj.get("picurl")));
		JSONObject classObj = new JSONObject(userObj.getString("classes"));
		student.setClassId(classObj.getString("classid"));
		student.setClassName(classObj.getString("classname"));
		return student;
	}

	private Parent parseParentInfo(JSONObject userObj) throws JSONException {
		Parent parent = new Parent();

		parent.setUserId(userObj.getString("id"));
		parent.setUsercode(userObj.getString("usercode"));
		parent.setUserName(userObj.getString("username"));
		parent.setUservalue(userObj.getString("uservalue"));
		parent.setUserType(userObj.getString("usertype"));
		if(userObj.has("sex") || userObj.get("sex").equals(null)){
			parent.setSex("0");
		}
		else{
			parent.setSex(String.valueOf(userObj.get("sex")));
		}
		
		if(userObj.has("phone") || userObj.get("phone").equals(null)){
			parent.setPhone("");
		}
		else{
			parent.setPhone(String.valueOf(userObj.get("phone")));
		}
		
		if(userObj.has("picurl") || userObj.get("picurl").equals(null)){
			parent.setPicUrl("");
		}
		else{
			parent.setPicUrl(String.valueOf(userObj.get("picurl")));
		}
		
//		parent.setPhone(String.valueOf(!userObj.has("phone")?"":userObj.get("phone")));
//		parent.setPicUrl(String.valueOf(!userObj.has("picurl")?"":userObj.get("picurl")));
		JSONArray childArray = userObj.getJSONArray("children");
		
		for (int i = 0; i < childArray.length(); i++) {
			Student child = parseStudentInfo(childArray.getJSONObject(i));
			parent.addOrderItem(child);
		}

		return parent;
	}

	private Teacher parseTeacherInfo(JSONObject userObj) throws JSONException {
		Teacher teacher = new Teacher();

		teacher.setUserId(userObj.getString("id"));
		teacher.setUserName(userObj.getString("username"));
		teacher.setUsercode(userObj.getString("usercode"));
		teacher.setUservalue(userObj.getString("uservalue"));
//		teacher.setTeacherId(userObj.getString("uservalue"));
		teacher.setUserType(userObj.getString("usertype"));
		if(userObj.has("sex") ){
			if(userObj.get("sex").equals(null)){
				{
					teacher.setSex("0");
				}
			}
			else{
				teacher.setSex(String.valueOf(userObj.get("sex")));
			}
		} 
		
		
		
		if(userObj.has("phone") ){
			if(userObj.get("phone").equals(null)){
				{
					teacher.setPhone("");
				}
			}
			else{
				teacher.setPhone(String.valueOf(userObj.get("phone")));
			}
		} 
		
		
		if(userObj.has("picurl") ){
			if(userObj.get("picurl").equals(null)){
				{
					teacher.setPicUrl("");
				}
			}
			else{
				teacher.setPicUrl(String.valueOf(userObj.get("picurl")));
			}
		} 
		
//		teacher.setSex(String.valueOf((!userObj.has("sex")?"0":userObj.get("sex"))));
//		teacher.setPhone(userObj.getString("phone"));
//		teacher.setPicUrl(String.valueOf(!userObj.has("picurl")?"":userObj.get("picurl")));
		
		teacher.setBusiness("1");
		
		JSONObject schoolInfo = new JSONObject(userObj.getString("school"));
		teacher.setSchoolId(schoolInfo.getString("schoolno"));
		teacher.setSchoolname(schoolInfo.getString("schoolname"));
		
		if(userObj.has("classes")){
			
			if(userObj.get("classes") instanceof JSONArray){
				 if(userObj.getJSONArray("classes").length() > 0){
						JSONArray classesArray = userObj.getJSONArray("classes");
						teacher.setTeacherRoleList(parseTeacherRole(classesArray));
				 }
				 else{
						Log.d(TAG, "此人是classes 为空,不属于任何班级");
						ArrayList<TeacherRole> trlist = new ArrayList<TeacherRole>();
						TeacherRole tr = new TeacherRole();
						tr.setClassId("0");
						tr.setClassmaster("1");
						tr.setClassName("行政部");
						trlist.add(tr);
						teacher.setTeacherRoleList(trlist);
				 }
			}
			

		}
		else{
			Log.d(TAG, "此人是classes格式不对");
		}
		

		
		JSONArray roleArray = userObj.getJSONArray("roles");
		
		String rolename = "";
		String rolecode = "";
		
		for(int i = 0; i < roleArray.length(); i++){
			JSONObject itemObj = roleArray.getJSONObject(i);
			rolename = rolename + itemObj.getString("rolename") + ",";
			rolecode = rolecode + itemObj.getString("rolecode") + ",";
		}
		
		if(rolecode.length() > 0){
			if(rolecode.contains("1")){
				teacher.setSchoolLeader(SchoolLeader.yes);
			}
			else{
				teacher.setSchoolLeader(SchoolLeader.no);
			}
			
			rolename = rolename.substring(0, rolename.length()-1);
			rolecode = rolecode.substring(0, rolecode.length()-1);
		}
		teacher.setRolename(rolename);
		teacher.setRolecode(rolecode);
		
		return teacher;
	}

	private ArrayList<TeacherRole> parseTeacherRole(JSONArray roleArray) throws JSONException {
		ArrayList<TeacherRole> teacherRoleList = new ArrayList<TeacherRole>();
		for (int i = 0; i < roleArray.length(); i++) {
			TeacherRole roleItem = new TeacherRole();
			JSONObject itemObj = roleArray.getJSONObject(i);

			roleItem.setClassId(itemObj.getString("classid"));
			roleItem.setClassName(itemObj.getString("classname"));
			roleItem.setClassmaster(String.valueOf(!itemObj.has("classmaster")?"false":itemObj.get("classmaster")));
			
			if(itemObj.has("courses") && itemObj.get("courses") instanceof JSONArray){
				JSONArray coursesDescArray = itemObj.getJSONArray("courses");
				
				String coursename = "";
				String coursecode = "";
				for(int j = 0; j < coursesDescArray.length(); j++){
					JSONObject coursesDesc = coursesDescArray.getJSONObject(j);
					coursename = coursename + coursesDesc.getString("coursename") + ",";
					coursecode = coursecode + coursesDesc.getString("coursecode") + ",";
				}
				
				if(coursecode.length() > 0){
					coursename = coursename.substring(0, coursename.length()-1);
					coursecode = coursecode.substring(0, coursecode.length()-1);
				}
				
				roleItem.setCoursename(coursename);
				roleItem.setCoursecode(coursecode);
			}
			
			teacherRoleList.add(roleItem);
		}

		return teacherRoleList;
	}
}
