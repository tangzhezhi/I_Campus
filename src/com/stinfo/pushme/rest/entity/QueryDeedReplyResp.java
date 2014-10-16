package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.entity.NoticeReply;

public class QueryDeedReplyResp extends BaseResponse {
	private int count = 0;
	private ArrayList<NoticeReply> noticeList = new ArrayList<NoticeReply>();

	public int getCount() {
		return count;
	}

	public ArrayList<NoticeReply> getNoticeReplyList() {
		return noticeList;
	}

	public QueryDeedReplyResp(String jsonStr) throws JSONException  {
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
				NoticeReply notice = new NoticeReply();
				notice.setId(noticeObj.getString("id"));
				notice.setAuthorid(noticeObj.getString("coperid"));
				notice.setAuthorname(noticeObj.getString("cusername"));
				notice.setContent(noticeObj.getString("comment"));
				notice.setAnswerTime(noticeObj.getString("cdate"));
				this.noticeList.add(0, notice); // 服务器按升序排序
			}
		}
	}
}
