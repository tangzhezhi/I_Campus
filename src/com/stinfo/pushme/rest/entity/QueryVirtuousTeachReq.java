package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryVirtuousTeachReq extends BaseRequest {
	private String userId;
	private String classId="";
	private String userType;
	private String schoolId;
	private String studentId="";
	private String flag;
	private String sessionkey;
	private String showCount = "10";
	
	private int currentPage;
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSessionkey() {
		return sessionkey;
	}

	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	@Override
	public String getPath() {
//		return   AppConstant.BASE_URL +"queryVirtuous.ashx";
		return AppConstant.School_Platform_BASE_URL + "jyb/queryVirtuous";
		
		
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("showCount", this.showCount);
		paramsHashMap.put("sessionkey", this.sessionkey);
		paramsHashMap.put("classId", String.valueOf(this.classId));
		paramsHashMap.put("currentPage", String.valueOf(this.currentPage==0?1:this.currentPage));
		if(this.studentId!=null && !this.studentId.equals("")){
			paramsHashMap.put("studentId", this.studentId);
		}
		paramsHashMap.put("flag", this.flag);
		return paramsHashMap;
	}

}
