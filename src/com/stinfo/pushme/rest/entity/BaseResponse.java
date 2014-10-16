package com.stinfo.pushme.rest.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.activity.LoginActivity;
import com.stinfo.pushme.activity.MainActivity;
import com.stinfo.pushme.util.MessageBox;

public class BaseResponse {
	private int ack = 0;

    public int getAck() {
        return ack;
    }

    public BaseResponse(String jsonStr) throws JSONException {
    	parseResponseData(jsonStr);
    }

    private void parseResponseData(String jsonStr) throws JSONException {
    	JSONObject rootObj = new JSONObject(jsonStr);
        this.ack = rootObj.getInt("ack");
        Context context = MyApplication.getInstance().getApplicationContext();
        if(ack==500){
        	MessageBox.showMessage(context, "会话过期，请重新登录...");
        	Intent intent = new Intent(context, LoginActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(intent);
        }
        else if(ack==103){
        	MessageBox.showMessage(context, "未登陆，请登录...");
        	Intent intent = new Intent(context, LoginActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(intent);
        }
    }
}
