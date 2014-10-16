package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.AttendanceRecord;
import com.stinfo.pushme.entity.OutOrInDoorRecord;

public class QueryAttendanceRecordResp  extends BaseResponse {
	private int count = 0;
	private ArrayList<AttendanceRecord> attendanceRecordList = new ArrayList<AttendanceRecord>();

	public int getCount() {
		return count;
	}

	public ArrayList<AttendanceRecord> getAttendanceRecordList() {
		return attendanceRecordList;
	}

	public QueryAttendanceRecordResp(String jsonStr) throws JSONException  {
		super(jsonStr);;
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			
			
			try {
				if(rootObj.get("response") instanceof JSONObject ){
					JSONObject attendanceRecordObj = rootObj.getJSONObject("response");
					AttendanceRecord attendanceRecord = new AttendanceRecord();
					attendanceRecord.setId(attendanceRecordObj.getString("id"));
					attendanceRecord.setCardNo(attendanceRecordObj.getString("waycard"));
					attendanceRecord.setUserName(attendanceRecordObj.getString("name"));
					attendanceRecord.setUserPic(attendanceRecordObj.getString("userPic"));
					
					if(attendanceRecordObj.has("wayRecords") ){
						
						if(attendanceRecordObj.get("wayRecords") instanceof JSONArray && attendanceRecordObj.get("wayRecords").toString().length() > 2 ){
							JSONArray accessTimesList = rootObj.getJSONArray("wayRecords");
							ArrayList<OutOrInDoorRecord> oiLists = new 	ArrayList<OutOrInDoorRecord>();
							for(int j = 0; j < accessTimesList.length(); j++){
								JSONObject accessTimes = accessTimesList.getJSONObject(j);
								OutOrInDoorRecord oirecord = new OutOrInDoorRecord();
								oirecord.setId(accessTimes.getString("id"));
								oirecord.setIoFlag(accessTimes.getString("state"));
								oirecord.setIoTime(accessTimes.getString("times"));
								oirecord.setCardno(accessTimes.getString("cardno"));
								oirecord.setTermid(accessTimes.getString("termid"));
								oiLists.add(oirecord);
							}
							
							attendanceRecord.setAccessTimes(oiLists);
						}
						this.attendanceRecordList.add(0, attendanceRecord);	// 服务器按升序排序
						}
				}
				else{
					JSONArray attendanceRecordArray = rootObj.getJSONArray("response");
					for (int i = 0; i < attendanceRecordArray.length(); i++) {
						JSONObject attendanceRecordObj = attendanceRecordArray.getJSONObject(i);
						AttendanceRecord attendanceRecord = new AttendanceRecord();
						attendanceRecord.setUserId(attendanceRecordObj.getString("id"));
						attendanceRecord.setCardNo(attendanceRecordObj.getString("waycard"));
						attendanceRecord.setUserName(attendanceRecordObj.getString("name"));
						attendanceRecord.setUserPic(attendanceRecordObj.getString("userPic"));
						
						if(attendanceRecordObj.has("wayRecords") ){
							if(attendanceRecordObj.get("wayRecords") instanceof JSONArray && attendanceRecordObj.get("wayRecords").toString().length() > 2){
								JSONArray accessTimesList = attendanceRecordObj.getJSONArray("wayRecords");
								ArrayList<OutOrInDoorRecord> oiLists = new 	ArrayList<OutOrInDoorRecord>();
								for(int j = 0; j < accessTimesList.length(); j++){
									JSONObject accessTimes = accessTimesList.getJSONObject(j);
									OutOrInDoorRecord oirecord = new OutOrInDoorRecord();
									oirecord.setPid(accessTimes.getString("times").substring(0, 10));
									oirecord.setIoTime(accessTimes.getString("times"));
									oirecord.setId(accessTimes.getString("id"));
									oirecord.setIoFlag(accessTimes.getString("state"));
									oirecord.setCardno(accessTimes.getString("cardno"));
									oirecord.setTermid(accessTimes.getString("termid"));
									oiLists.add(oirecord);
								}
								
								attendanceRecord.setAccessTimes(oiLists);
							}
								this.attendanceRecordList.add(0, attendanceRecord);	// 服务器按升序排序
							}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("QueryAttendanceRecordResp", "解析平安点到出错");
			}

			
			
		}
	}
	

}
