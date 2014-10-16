package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Daily;
import com.stinfo.pushme.entity.DailyComparator;

public class QueryDailyResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Daily> dailyList = new ArrayList<Daily>();

	public int getCount() {
		return count;
	}

	public ArrayList<Daily> getDailyList() {
		return dailyList;
	}

	public QueryDailyResp(String jsonStr) throws JSONException  {
		super(jsonStr);;
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		UserCache userCache = UserCache.getInstance();
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
//			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray dailyArray = rootObj.getJSONArray("response");
			for (int i = 0; i < dailyArray.length(); i++) {
				JSONObject dailyObj = dailyArray.getJSONObject(i);
				Daily daily = new Daily();

				daily.setAuthorId(dailyObj.getString("authorid"));
				daily.setParentid(dailyObj.getString("parentid"));
				daily.setStudentid(dailyObj.getString("studentid"));
		
				daily.setContent(dailyObj.getString("content"));
				daily.setCreateTime(dailyObj.getString("createtime"));
				this.dailyList.add(0, daily);	// 服务器按升序排序
			}
			Collections.sort(dailyList, new DailyComparator());
		}
	}
}
