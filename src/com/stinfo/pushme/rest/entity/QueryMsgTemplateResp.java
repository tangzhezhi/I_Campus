package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.MsgTemplate;

public class QueryMsgTemplateResp extends BaseResponse {
	private int count = 0;
	private ArrayList<MsgTemplate> noticeList = new ArrayList<MsgTemplate>();

	public int getCount() {
		return count;
	}

	public ArrayList<MsgTemplate> getMsgTemplateList() {
		return noticeList;
	}

	public QueryMsgTemplateResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
			JSONArray noticeArray = rootObj.getJSONArray("response");
			for (int i = 0; i < noticeArray.length(); i++) {
				JSONObject noticeObj = noticeArray.getJSONObject(i);
				MsgTemplate notice = new MsgTemplate();
				notice.setAddTime(noticeObj.getString("addTime"));
				notice.setSmsContent(noticeObj.getString("smsContent"));
				notice.setGroupCode(noticeObj.getString("groupCode"));
				notice.setAddUser(noticeObj.getString("addUser"));
				notice.setSchoolNo(noticeObj.getString("schoolNo"));
				notice.setGroupName(noticeObj.getString("groupName"));
				this.noticeList.add(0, notice); // 服务器按升序排序
			}
		}
	}
}
