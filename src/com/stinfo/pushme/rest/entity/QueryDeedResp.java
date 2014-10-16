package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stinfo.pushme.entity.Deed;

public class QueryDeedResp extends BaseResponse {
	
	public class DeedComparator implements Comparator<Deed> {
		
		@Override
		public int compare(Deed lhs, Deed rhs) {
			String createtime1 = lhs.getReleasedate();
			String createtime2 = rhs.getReleasedate();
			return createtime2.compareTo(createtime1);
		}
	}
	
	
	private int count = 0;
	private ArrayList<Deed> deedList = new ArrayList<Deed>();

	public int getCount() {
		return count;
	}

	public ArrayList<Deed> getDeedList() {
		return deedList;
	}

	public QueryDeedResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
			JSONArray deedArray = rootObj.getJSONArray("response");
			for (int i = 0; i < deedArray.length(); i++) {
				JSONObject deedObj = deedArray.getJSONObject(i);
				Deed deed = new Deed();
				deed.setReleaseuserPicPath(deedObj.getString("releaseuserPicPath"));
				deed.setCommentsize(deedObj.getString("commentsize"));
				deed.setParisesize(deedObj.getString("parisesize"));
				deed.setTitle(deedObj.getString("title"));
				deed.setContent(deedObj.getString("content"));
				deed.setId(deedObj.getString("id"));
				
				deed.setPlates(deedObj.getString("plates"));
				deed.setReleaseoperid(deedObj.getString("releaseoperid"));
				deed.setReleaseusername(deedObj.getString("releaseusername"));
				deed.setReleasedate(deedObj.getString("releasedate"));
				deed.setType(deedObj.getString("type"));
				deed.setClicknum(deedObj.getString("clicknum"));
				deed.setFirstPic(deedObj.getString("firstPic"));
				
				this.deedList.add(0, deed); // 服务器按升序排序
			}
			Collections.sort(deedList, new DeedComparator());
		}
	}
}
