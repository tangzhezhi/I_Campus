package com.stinfo.pushme.rest.entity;
import org.json.JSONException;
import org.json.JSONObject;

public class AddVirtuousTeachResp {
	private int ack;

	public int getAck() {
		return ack;
	}

	public void setAck(int ack) {
		this.ack = ack;
	}

	public AddVirtuousTeachResp(String jsonStr) throws JSONException  {
		parseResponseData(jsonStr);
	}
	
	private void parseResponseData(String jsonStr) throws JSONException {
		if(jsonStr!=null && !("").equals(jsonStr)){
			jsonStr = jsonStr.toLowerCase();
		}
		JSONObject rootObj = new JSONObject(jsonStr);
		ack = rootObj.getInt("ack");
	}
	
	
}
