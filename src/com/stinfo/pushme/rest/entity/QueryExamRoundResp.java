package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.entity.ExamRound;

public class QueryExamRoundResp extends BaseResponse {
	private int count = 0;
	private ArrayList<ExamRound> examRoundList = new ArrayList<ExamRound> ();
	
	public int getCount() {
		return count;
	}
	
	public ArrayList<ExamRound> getExamRoundList() {
		return examRoundList;
	}


	public void setExamRoundList(ArrayList<ExamRound> examRoundList) {
		this.examRoundList = examRoundList;
	}


	public QueryExamRoundResp(String jsonStr) throws JSONException  {
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
				ExamRound examRound = new ExamRound();
				examRound.setClassId(noticeObj.getString("classid"));
				examRound.setExamRoundId(noticeObj.getString("id"));
				examRound.setAyear(noticeObj.getString("ayear"));
				examRound.setAtrem(noticeObj.getString("atrem"));
				examRound.setGradecode(noticeObj.getString("gradecode"));
				examRound.setSchoolno(noticeObj.getString("schoolno"));
				examRound.setExamRoundTitle(noticeObj.getString("name"));
				this.examRoundList.add(0, examRound); // 服务器按升序排序
			}
			
		}
	}
}
