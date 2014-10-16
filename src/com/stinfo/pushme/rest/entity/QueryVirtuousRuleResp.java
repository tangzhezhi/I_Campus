package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Virtuous;

public class QueryVirtuousRuleResp extends BaseResponse {
	private ArrayList<Virtuous> VirtuousList = new ArrayList<Virtuous>();

	public ArrayList<Virtuous> getVirtuousList() {
		return VirtuousList;
	}

	public void setVirtuousList(ArrayList<Virtuous> VirtuousList) {
		this.VirtuousList = VirtuousList;
	}

	public QueryVirtuousRuleResp(String jsonStr) throws JSONException {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			JSONArray homeworkArray = rootObj.getJSONArray("response");
			for (int i = 0; i < homeworkArray.length(); i++) {
				JSONObject homeworkObj = homeworkArray.getJSONObject(i);
				Virtuous virtuous = new Virtuous();
				virtuous.setId(homeworkObj.getString("id"));
				virtuous.setMoralid(homeworkObj.getString("moralid"));
				virtuous.setName(homeworkObj.getString("name"));
				virtuous.setVal(homeworkObj.getString("val"));
				virtuous.setRemark(homeworkObj.getString("remark"));
				virtuous.setStatus(homeworkObj.getString("status"));
				virtuous.setMoralName(homeworkObj.getString("moralName"));
				this.VirtuousList.add(0, virtuous);	// 服务器按升序排序
			}
		}
	}
}
