package com.stinfo.pushme.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

public class RolePaser {
	public static String jsonToString(String role) {
		if (TextUtils.isEmpty(role)) {
			return "";
		}
		
		try {
			StringBuffer normal = new StringBuffer();
			JSONArray jsonArray = new JSONArray(role);

			for (int i = 0; i < jsonArray.length(); i++) {
				normal.append(jsonArray.getString(i));
				normal.append(" ");
			}
			return normal.toString();
		} catch (JSONException e) {
			return "";
		}
	}

	public static ArrayList<String> jsonToArray(String role) {
		ArrayList<String> roleList = new ArrayList<String>();
		if (TextUtils.isEmpty(role)) {
			return roleList;
		}
		
		try {
			JSONArray jsonArray = new JSONArray(role);
			for (int i = 0; i < jsonArray.length(); i++) {
				roleList.add(jsonArray.getString(i));
			}
			return roleList;
		} catch (JSONException e) {
			return roleList;
		}
	}
}
