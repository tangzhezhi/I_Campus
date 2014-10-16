package com.stinfo.pushme.db;

import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;

public class AccountDBAdapter extends DBAdapter  {
	
	
	@SuppressLint("DefaultLocale")
	public int ModifyAccountSex(UserInfo userInfo) {
		String table = "";
		
		if(userInfo instanceof Student){
			table = "StudentRoster";
		}
		else if(userInfo instanceof Parent){
			table = "ParentRoster";
		}
		else if(userInfo instanceof Teacher){
			table = "TeacherRoster";
		}
		String where = String.format("cacheUserId = '%s' ",mUserId);
		ContentValues values = new ContentValues();
		values.put("sex", userInfo.getSex());
		return this.db.update(table, values, where, null);
	}
	
}
