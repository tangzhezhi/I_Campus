package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Deed;
import com.stinfo.pushme.entity.DeedDetail;

public class QueryDeedDetailResp extends BaseResponse {
	
	private DeedDetail deedDetail = new DeedDetail();
	
	public DeedDetail getDeedDetail() {
		return deedDetail;
	}

	public void setDeedDetail(DeedDetail deedDetail) {
		this.deedDetail = deedDetail;
	}


	public QueryDeedDetailResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			JSONObject deedDetailObj = rootObj.getJSONArray("response").getJSONObject(0);
			
			deedDetail.setTitle(deedDetailObj.getString("title"));
			deedDetail.setContent(deedDetailObj.getString("content"));
			deedDetail.setId(deedDetailObj.getString("id"));
			deedDetail.setCommentsize(deedDetailObj.getString("commentsize"));
			deedDetail.setParisesize(deedDetailObj.getString("parisesize"));
			deedDetail.setReleasedate(deedDetailObj.getString("releasedate"));
			deedDetail.setReleaseoperid(deedDetailObj.getString("releaseoperid"));
			deedDetail.setReleaseusername(deedDetailObj.getString("releaseusername"));
			deedDetail.setReleaseuserPicPath(deedDetailObj.getString("releaseuserPicPath"));
			
			
			if(deedDetailObj.has("pics")){
				JSONArray deedDetailPicsArray= deedDetailObj.getJSONArray("pics");
				
				ArrayList<String> picList = new ArrayList<String>();
				
				for (int i = 0; i < deedDetailPicsArray.length(); i++) {
					JSONObject deedPicObj = deedDetailPicsArray.getJSONObject(i);
					picList.add(deedPicObj.getString("picpath"));
				}
				
				deedDetail.setPics(picList);
			}
		}
	}
}
