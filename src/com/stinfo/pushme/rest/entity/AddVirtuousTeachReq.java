package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class AddVirtuousTeachReq extends BaseRequest {
	private String userId;
	private String classId;
	private String userType;
	private String schoolId;
	private String coin;
	private String studentId;
	private String virtousType;
	private String virtousScore;
	private String virtousReason;
	
	private String sessionkey;
	
	
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
	
	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	@Override
	public String getPath() {
//		return AppConstant.BASE_URL +"postVirtuousTeach.ashx";
		return AppConstant.School_Platform_BASE_URL + "jyb/setJyb";
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getVirtousType() {
		return virtousType;
	}

	public void setVirtousType(String virtousType) {
		this.virtousType = virtousType;
	}

	public String getVirtousScore() {
		return virtousScore;
	}

	public void setVirtousScore(String virtousScore) {
		this.virtousScore = virtousScore;
	}

	public String getVirtousReason() {
		return virtousReason;
	}

	public void setVirtousReason(String virtousReason) {
		this.virtousReason = virtousReason;
	}

	public String getSessionkey() {
		return sessionkey;
	}

	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionkey);
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("classId", String.valueOf(this.classId));
		paramsHashMap.put("userType", this.userType);
		paramsHashMap.put("schoolId", this.schoolId);
		paramsHashMap.put("coin", this.coin);
		paramsHashMap.put("studentId", this.studentId);
		paramsHashMap.put("virtousType", this.virtousType);
		paramsHashMap.put("virtousScore", this.virtousScore);
		paramsHashMap.put("virtousReason", this.virtousReason);
		return paramsHashMap;
	}

}
