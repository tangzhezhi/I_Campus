package com.stinfo.pushme.db;

import java.util.ArrayList;

import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.entity.UserInfo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;

@SuppressLint("DefaultLocale")
public class MessageDBAdapter extends DBAdapter {
	/**
	 * 删除本地数据库中的记录
	 * @param createtime
	 * @param content
	 */
	@SuppressLint("DefaultLocale")
	public void deleteRecentMessage(String userId) {
		String where = String.format("objectId = '%s' ", userId);
		this.db.delete("RecentMessage", where, null);
	}

	
	public void deleteMessage(String userId) {
		String where = String.format("senderId = '%s' or receiverId = '%s' ", userId,userId);
		this.db.delete("Message", where, null);
	}
	
	
	private UserInfo fetchUserInfo(Cursor result) {
		UserInfo userInfo = new UserInfo();

		userInfo.setUserId(result.getString(result.getColumnIndex("userId")));
		userInfo.setUserName(result.getString(result.getColumnIndex("userName")));
		userInfo.setSex(result.getString(result.getColumnIndex("sex")));
		userInfo.setPhone(result.getString(result.getColumnIndex("phone")));
		userInfo.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		userInfo.setUservalue(result.getString(result.getColumnIndex("uservalue")));
		return userInfo;
	}

	public UserInfo getUserInfo(String name,String phone) throws SQLException {
		String where = String.format(" name = '%s' and phone ='%s'  ", name, phone);
		Cursor result = db.query("Roster", new String[] { "userId", "userName", "sex", "phone", "picUrl" },
				where, null, null, null, null, null);

		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("Not found! name: phone" + name+"::"+phone);
		}

		return fetchUserInfo(result);
	}
}
