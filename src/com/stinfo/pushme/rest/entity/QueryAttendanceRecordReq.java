package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryAttendanceRecordReq extends BaseRequest {
	private String classid="";
	private String beginDate="";
	private String endDate="";
	private String sessionKey = "";
	private String stuid="";
	private String teacherid="";
	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStuid() {
		return stuid;
	}

	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	
	public String getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}

	@Override
	public String getPath() {
		if(classid!=null && !classid.equals("")){
			return AppConstant.School_Platform_BASE_URL + "wayCardRecord/getCardRecordByClass";
		}
		else if(stuid!=null && !stuid.equals("")){
			return AppConstant.School_Platform_BASE_URL + "wayCardRecord/getCardRecordStu";
		}
		else if(teacherid!=null && !teacherid.equals("")){
			return AppConstant.School_Platform_BASE_URL + "wayCardRecord/getCardRecordTea";
		}
		else{
			return null;
		}
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("sessionkey", this.sessionKey);
		if(classid!=null && !classid.equals("")){
			paramsHashMap.put("classid", this.classid);
		}
		else if(stuid!=null && !stuid.equals("")){
			paramsHashMap.put("stuid", this.stuid);
		}
		else if(teacherid!=null && !teacherid.equals("")){
			paramsHashMap.put("teacherid", this.teacherid);
		}
		
		paramsHashMap.put("beginDate", this.beginDate);
		paramsHashMap.put("endDate", this.endDate);
		return paramsHashMap;
	}

}
