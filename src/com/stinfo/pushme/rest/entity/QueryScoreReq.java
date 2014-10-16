package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryScoreReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private String classId;
	private String examRoundId;
	private String userType;
	private String studentUserId;
	
	public String getStudentUserId() {
		return studentUserId;
	}

	public void setStudentUserId(String studentUserId) {
		this.studentUserId = studentUserId;
	}

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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getExamRoundId() {
		return examRoundId;
	}

	public void setExamRoundId(String examRoundId) {
		this.examRoundId = examRoundId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryScoreOfExamRound.ashx";
		return AppConstant.School_Platform_BASE_URL + "examination/getScoreInExamturn";
	}
	
	
	
	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
//		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("classid", String.valueOf(this.classId));
		paramsHashMap.put("examturnid", this.examRoundId);
//		paramsHashMap.put("userType", this.userType);
		if(studentUserId!=null && !"".equals(studentUserId)){
			paramsHashMap.put("studentid", this.studentUserId);
		}
		return paramsHashMap;
	}
}
