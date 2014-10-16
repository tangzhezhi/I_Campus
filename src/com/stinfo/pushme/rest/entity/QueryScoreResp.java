package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Score;

public class QueryScoreResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Score> scoreList = new ArrayList<Score> ();
	
	public int getCount() {
		return count;
	}
	
	public ArrayList<Score> getScoreList() {
		return scoreList;
	}


	public void setScoreList(ArrayList<Score> scoreList) {
		this.scoreList = scoreList;
	}


	public QueryScoreResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
//			this.count = rootObj.getInt("count");
//			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray noticeArray = rootObj.getJSONArray("response");
			
			for (int i = 0; i < noticeArray.length(); i++) {
				JSONObject noticeObj = noticeArray.getJSONObject(i);

				
				JSONArray coursesArray= noticeObj.getJSONArray("courses");
				for (int j = 0; j < coursesArray.length(); j++) {
					JSONObject coursesObj = coursesArray.getJSONObject(j);
					Score score = new Score();
					score.setStudentid(noticeObj.getString("studentid"));
					score.setClassid(noticeObj.getString("classid"));
					score.setSubject(coursesObj.getString("coursename"));
					score.setExamPaperScore(coursesObj.getString("degree"));
					this.scoreList.add(0, score); // 服务器按升序排序
				}
			}
			
		}
	}
}
