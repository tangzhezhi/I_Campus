package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryCourseReq extends BaseRequest {
	


	@Override
	public String getPath() {
//		return AppConstant.BASE_URL + "queryHomeWork.ashx";
		return AppConstant.School_Platform_BASE_URL + "course/nonChecked/getCourses";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("1", "1");
		return paramsHashMap;
	}

}
