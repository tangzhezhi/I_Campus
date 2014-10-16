package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.entity.MsgTemplateGroup;

public class QueryMsgTemplateGroupResp extends BaseResponse {
	private ArrayList<MsgTemplateGroup> noticeList = new ArrayList<MsgTemplateGroup>();

	public ArrayList<MsgTemplateGroup> getMsgTemplateGroupList() {
		return noticeList;
	}

	public QueryMsgTemplateGroupResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			JSONArray noticeArray = rootObj.getJSONArray("response");
			for (int i = 0; i < noticeArray.length(); i++) {
				JSONObject noticeObj = noticeArray.getJSONObject(i);
				MsgTemplateGroup notice = new MsgTemplateGroup();
				notice.setGroupCode(noticeObj.getString("groupCode"));
				notice.setGroupName(noticeObj.getString("groupName"));
				this.noticeList.add(0, notice); // 服务器按升序排序
			}
		}
	}
}
