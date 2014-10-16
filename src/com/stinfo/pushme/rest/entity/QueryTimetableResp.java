package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.Timetable;
import com.stinfo.pushme.util.DateTimeUtil;

public class QueryTimetableResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Timetable> timetableEntityList = new ArrayList<Timetable>();
	private ArrayList<Timetable> timetableList = new ArrayList<Timetable> ();
	
	
	public ArrayList<Timetable> getTimetableEntityList() {
		return timetableEntityList;
	}

	public void setTimetableEntityList(ArrayList<Timetable> timetableEntityList) {
		this.timetableEntityList = timetableEntityList;
	}

	public int getCount() {
		return count;
	}
	
	public ArrayList<Timetable> getTimetableList() {
		return timetableList;
	}


	public void setTimetableList(ArrayList<Timetable> timetableList) {
		this.timetableList = timetableList;
	}


	public QueryTimetableResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
//			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray noticeArray = rootObj.getJSONArray("response");
			
			for (int i = 0; i < noticeArray.length(); i++) {
				JSONObject noticeObj = noticeArray.getJSONObject(i);
				Timetable timetable = new Timetable();
				timetable.setSubject(noticeObj.getString("coursename"));
				timetable.setClassId(noticeObj.getString("classid"));
				timetable.setContent(noticeObj.getString("coursename"));
				timetable.setSection(noticeObj.getString("sections"));
				timetable.setWeekDay(noticeObj.getString("week"));
				timetable.setCoursecode(noticeObj.getString("coursecode"));
				this.timetableEntityList.add(0, timetable); // 服务器按升序排序
			}
			
		}
	}
}
