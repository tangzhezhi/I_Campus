package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.entity.HomeworkComparator;

public class QueryHomeworkResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Homework> homeworkList = new ArrayList<Homework>();

	public int getCount() {
		return count;
	}

	public ArrayList<Homework> getHomeworkList() {
		return homeworkList;
	}

	public QueryHomeworkResp(String jsonStr) throws JSONException {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
//			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray homeworkArray = rootObj.getJSONArray("response");
			for (int i = 0; i < homeworkArray.length(); i++) {
				JSONObject homeworkObj = homeworkArray.getJSONObject(i);
				Homework homework = new Homework();

				homework.setAuthorId(homeworkObj.getString("authorid"));
				homework.setSubject(homeworkObj.getString("subject"));
				homework.setClassId(homeworkObj.getString("classid"));
				homework.setContent(homeworkObj.getString("content"));
				homework.setCreateTime(homeworkObj.getString("createtime"));
				this.homeworkList.add(0, homework);	// 服务器按升序排序
			}
			Collections.sort(homeworkList, new HomeworkComparator());
		}
	}
}
