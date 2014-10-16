package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.NoticeComparator;

public class QueryNoticeResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Notice> noticeList = new ArrayList<Notice>();

	public int getCount() {
		return count;
	}

	public ArrayList<Notice> getNoticeList() {
		return noticeList;
	}

	public QueryNoticeResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
//			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray noticeArray = rootObj.getJSONArray("response");
			for (int i = 0; i < noticeArray.length(); i++) {
				JSONObject noticeObj = noticeArray.getJSONObject(i);
				Notice notice = new Notice();
				notice.setObjectId(noticeObj.getString("id"));
				notice.setAuthorId(noticeObj.getString("authorid"));
				notice.setType(noticeObj.getInt("type"));
				notice.setTitle(noticeObj.getString("title"));
				notice.setContent(noticeObj.getString("content"));
				notice.setCreateTime(noticeObj.getString("createtime"));
				notice.setTotal(noticeObj.getString("total"));
				this.noticeList.add(0, notice); // 服务器按升序排序
			}
			Collections.sort(noticeList, new NoticeComparator());
		}
	}
}
