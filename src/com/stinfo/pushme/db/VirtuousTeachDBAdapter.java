package com.stinfo.pushme.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.stinfo.pushme.entity.Score;
import com.stinfo.pushme.entity.VirtuousTeach;
import com.stinfo.pushme.util.DateTimeUtil;

@SuppressLint("DefaultLocale")
public class VirtuousTeachDBAdapter extends DBAdapter {
	
	public void addVirtuousTeach(ArrayList<VirtuousTeach> list) throws SQLException {
		for (VirtuousTeach virtuousTeach : list) {
			ContentValues values = new ContentValues();
			values.put("id", UUID.randomUUID().toString());
			values.put("userId", virtuousTeach.getUserId());
			values.put("userType", virtuousTeach.getUserType());
			values.put("coin", virtuousTeach.getCoin());
			values.put("classId", virtuousTeach.getClassId());
			values.put("startDate", virtuousTeach.getStartDate());
			values.put("endDate", virtuousTeach.getEndDate());
			values.put("createTime", DateTimeUtil.getCompactTime());
			
			List<VirtuousTeach> lh = this.getVirtuousTeachExist(virtuousTeach.getUserId(),
					virtuousTeach.getCoin(),virtuousTeach.getStartDate(),virtuousTeach.getEndDate());
			if(lh.size()==0){
				db.insertOrThrow("virtuousTeach", null, values);
			}
		}
	}
	
	
	private List<VirtuousTeach> getVirtuousTeachExist(String userId,
			String coin,String startDate,String endDate) {
		String where = String.format(" userId = '%s' AND coin = '%s' AND  startDate='%s' and endDate='%s'  ", 
				userId, coin,startDate,endDate);
		return getVirtuousTeachByWhere(where);
	}
	
	private ArrayList<VirtuousTeach> getVirtuousTeachByWhere(String where) {
		ArrayList<VirtuousTeach> list = new ArrayList<VirtuousTeach>();
		String orderBy = "createTime desc ";
		String limit = String.valueOf(20);

		Cursor result = db.query("VirtuousTeach", new String[] { "userId",
				"userType","coin","classId","startDate","endDate","createTime"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchVirtuousTeach(result));
			} while (result.moveToNext());
		}

		return list;
	}

	
	public ArrayList<VirtuousTeach> getAllVirtuousTeach(String userId,String classId) {
		String where = String.format(" userId = '%s' AND classId= '%s' ", 
				userId,classId
				);
		return getVirtuousTeachByWhere(where);
	}
	
	
	private VirtuousTeach fetchVirtuousTeach(Cursor result) {
		VirtuousTeach virtuousTeach = new VirtuousTeach();
		virtuousTeach.setUserId(result.getString(result.getColumnIndex("userId")));
		virtuousTeach.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		virtuousTeach.setUserType(result.getString(result.getColumnIndex("userType")));
		virtuousTeach.setCoin(result.getString(result.getColumnIndex("coin")));
		virtuousTeach.setClassId(result.getString(result.getColumnIndex("classId")));
		virtuousTeach.setStartDate(result.getString(result.getColumnIndex("startDate")));
		virtuousTeach.setEndDate(result.getString(result.getColumnIndex("endDate")));
		return virtuousTeach;
	}

}
