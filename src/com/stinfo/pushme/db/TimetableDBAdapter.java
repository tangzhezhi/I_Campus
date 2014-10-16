package com.stinfo.pushme.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.stinfo.pushme.entity.Timetable;
import com.stinfo.pushme.util.DateTimeUtil;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class TimetableDBAdapter extends DBAdapter {
	
	public void addTimetable(ArrayList<Timetable> list) throws SQLException {
		
		db.delete("timetable", null, null);
		
		for (Timetable timetable : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("section", timetable.getSection());
			values.put("subject", timetable.getSubject());
			values.put("classId", timetable.getClassId());
			values.put("content", timetable.getContent());
			values.put("weekDay", timetable.getWeekDay());
			db.insertOrThrow("timetable", null, values);
		}
	}
	
	
	private List<Timetable> getTimetableExist(String classId,
			String subject, String weekday,String section) {
		String where = String.format(" classId = '%s' AND subject = '%s'  AND weekday = '%s' AND  section = '%s'  ", 
				classId, subject,weekday,section);
		return getTimetableByWhere(where);
	}
	
	
	
	private ArrayList<Timetable> getTimetableByWhere(String where) {
		ArrayList<Timetable> list = new ArrayList<Timetable>();
		String orderBy = "weekDay asc , section ASC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Timetable", new String[] { "cacheUserId",  "subject", "classId",
				"content","section","classDate","weekDay", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchTimetable(result));
			} while (result.moveToNext());
		}

		return list;
	}

	
	private Timetable fetchTimetable(Cursor result) {
		Timetable timetable = new Timetable();
		timetable.setCacheUserId(result.getString(result.getColumnIndex("cacheUserId")));
		timetable.setSection(result.getString(result.getColumnIndex("section")));
		timetable.setClassDate(result.getString(result.getColumnIndex("classDate")));
		timetable.setWeekDay(result.getString(result.getColumnIndex("weekDay")));
		timetable.setSubject(result.getString(result.getColumnIndex("subject")));
		timetable.setClassId(result.getString(result.getColumnIndex("classId")));
		timetable.setContent(result.getString(result.getColumnIndex("content")));
		timetable.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		return timetable;
	}

	public ArrayList<Timetable> getTimetableOfWeek(String classId,int week) {
			String startDate = "1";
			String endDate = "7";
			
			ArrayList<Timetable> tlist = new  ArrayList<Timetable> ();
			tlist.clear();
			
			for(int j = 1 ; j< 13 ; j++){
					String where = String.format(" classId = '%s'   AND weekday between '%s' AND  '%s'  and section = '%s' ", 
							classId, startDate,endDate,String.valueOf(j));
				ArrayList<Timetable> list = new ArrayList<Timetable>();
				list.clear();
				list = getTimetableByWhere(where);
				
				int ls = list.size();
				if(ls==0 && j==1){
					break;
				}
				else if(ls< 8){
					ArrayList<Timetable> listtemp = list;
					
					ArrayList<Timetable> templist = new ArrayList<Timetable>(7);
					for(int i = 0 ; i < 7 ; i++){
						Timetable t = new Timetable();
						t.setWeekDay(String.valueOf(i+1));
						t.setContent("");
						templist.add(i, t);
					}
					
					for(int n = 0 ; n < listtemp.size(); n++){
						for(int m = 0 ; m < templist.size(); m++){
							if(listtemp.get(n).getWeekDay().equals(templist.get(m).getWeekDay())){
								templist.remove(m);
								templist.add(m,listtemp.get(n));
							}
						}
					}
					
					Timetable  timetable = new Timetable();
					timetable.setContent(String.valueOf(j));
					templist.add(0,timetable);
					tlist.addAll(templist);
					
				}

			}
			
			return tlist;
	}

	

}
