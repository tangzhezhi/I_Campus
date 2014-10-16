package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Course;

public class QueryCourseResp extends BaseResponse {
	private ArrayList<Course> courseList = new ArrayList<Course>();

	public ArrayList<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(ArrayList<Course> courseList) {
		this.courseList = courseList;
	}

	public QueryCourseResp(String jsonStr) throws JSONException {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			JSONArray homeworkArray = rootObj.getJSONArray("response");
			for (int i = 0; i < homeworkArray.length(); i++) {
				JSONObject homeworkObj = homeworkArray.getJSONObject(i);
				Course homework = new Course();
				homework.setCoursecode(homeworkObj.getString("coursecode"));
				homework.setCoursename(homeworkObj.getString("coursename"));
				this.courseList.add(0, homework);	// 服务器按升序排序
			}
		}
	}
}
