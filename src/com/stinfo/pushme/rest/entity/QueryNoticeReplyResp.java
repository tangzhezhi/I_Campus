package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.entity.NoticeReply;

public class QueryNoticeReplyResp extends BaseResponse {
	private int count = 0;
	private ArrayList<NoticeReply> noticeList = new ArrayList<NoticeReply>();

	public int getCount() {
		return count;
	}

	public ArrayList<NoticeReply> getNoticeReplyList() {
		return noticeList;
	}

	public QueryNoticeReplyResp(String jsonStr) throws JSONException  {
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
				notice.setNoticeId(noticeObj.getString("noticeId"));
				notice.setAuthorid(noticeObj.getString("authorid"));
				notice.setAuthorname(noticeObj.getString("authorname"));
				notice.setContent(noticeObj.getString("content"));
				notice.setAnswerTime(noticeObj.getString("answerTime"));
				this.noticeList.add(0, notice); // 服务器按升序排序
			}
		}
	}
}
