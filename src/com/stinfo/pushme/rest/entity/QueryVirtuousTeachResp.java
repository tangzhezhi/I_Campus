package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.NoticeComparator;
import com.stinfo.pushme.entity.VirtuousTeach;

public class QueryVirtuousTeachResp  {
	
	private class VirtuousTeachComparator implements Comparator<VirtuousTeach> {
		
		@Override
		public int compare(VirtuousTeach lhs, VirtuousTeach rhs) {
			String recorddate1 = lhs.getRecorddate();
			String recorddate2 = rhs.getRecorddate();
			return recorddate2.compareTo(recorddate1);
		}
	}
	
	
	
	private int count = 0;
	private ArrayList<VirtuousTeach> virtuousTeachList = new ArrayList<VirtuousTeach> ();
	
	
	public int getCount() {
		return count;
	}
	
	public ArrayList<VirtuousTeach> getVirtuousTeachList() {
		return virtuousTeachList;
	}


	public void setVirtuousTeachList(ArrayList<VirtuousTeach> virtuousTeachList) {
		this.virtuousTeachList = virtuousTeachList;
	}


	public QueryVirtuousTeachResp(String jsonStr) throws JSONException  {
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		jsonStr = jsonStr.toLowerCase();
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			
			if(rootObj.has("count")){
				this.count = rootObj.getInt("count");
			}
			
			if(rootObj.get("response") instanceof JSONArray){
				JSONArray noticeArray = rootObj.getJSONArray("response");
				for (int i = 0; i < noticeArray.length(); i++) {
					JSONObject noticeObj = noticeArray.getJSONObject(i);
					VirtuousTeach virtuousTeach = new VirtuousTeach();
					virtuousTeach.setClassId(noticeObj.getString("classid"));
					virtuousTeach.setCoin(noticeObj.getString("addjyb"));
					if(noticeObj.has("stuid") && !noticeObj.get("stuid").equals(null)){
						virtuousTeach.setStudentid(noticeObj.getString("stuid"));
					}
					if(noticeObj.has("studentname") && !noticeObj.get("studentname").equals(null)){
						virtuousTeach.setStudentname(noticeObj.getString("studentname"));
					}
					
					if(noticeObj.has("date") && !noticeObj.get("date").equals(null)){
						virtuousTeach.setRecorddate(noticeObj.getString("date"));
					}
					
					if(noticeObj.has("virtoustype") && !noticeObj.get("virtoustype").equals(null)){
						virtuousTeach.setVirtousType(noticeObj.getString("virtoustype"));
					}
					
					if(noticeObj.has("virtousscore") && !noticeObj.get("virtousscore").equals(null)){
						virtuousTeach.setVirtousScore(noticeObj.getString("virtousscore"));
					}
					
					if(noticeObj.has("virtousreason") && !noticeObj.get("virtousreason").equals(null)){
						virtuousTeach.setVirtousReason(noticeObj.getString("virtousreason"));
					}
					
					
					
//					virtuousTeach.setStartDate(noticeObj.getString("startdate"));
//					virtuousTeach.setEndDate(noticeObj.getString("enddate"));
					this.virtuousTeachList.add(0, virtuousTeach); // 服务器按升序排序
				}
			}
			else{
					JSONObject noticeObj = rootObj.getJSONObject("response");
					VirtuousTeach virtuousTeach = new VirtuousTeach();
					virtuousTeach.setClassId(noticeObj.getString("classid"));
					virtuousTeach.setCoin(noticeObj.getString("coin"));
					virtuousTeach.setStartDate(noticeObj.getString("startdate"));
					virtuousTeach.setEndDate(noticeObj.getString("enddate"));
					if(noticeObj.has("studentid") && !noticeObj.get("studentid").equals(null)){
						virtuousTeach.setStudentid(noticeObj.getString("studentid"));
					}
					if(noticeObj.has("studentname") && !noticeObj.get("studentname").equals(null)){
						virtuousTeach.setStudentname(noticeObj.getString("studentname"));
					}
					this.virtuousTeachList.add(0, virtuousTeach); // 服务器按升序排序
			}
			
			Collections.sort(virtuousTeachList, new VirtuousTeachComparator());
			

		}
	}
}
