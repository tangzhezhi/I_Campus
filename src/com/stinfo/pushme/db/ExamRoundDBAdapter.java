package com.stinfo.pushme.db;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import com.stinfo.pushme.entity.ExamRound;

@SuppressLint("DefaultLocale")
public class ExamRoundDBAdapter extends DBAdapter {
	
	public void addExamRound(ArrayList<ExamRound> list) throws SQLException {
		for (ExamRound examRound : list) {
			ContentValues values = new ContentValues();
			values.put("cacheUserId", mUserId);
			values.put("classId", examRound.getClassId());
			values.put("examRoundId", examRound.getExamRoundId());
			values.put("examDate", examRound.getExamDate());
			values.put("totalScore", examRound.getTotalScore());
			values.put("createTime", examRound.getCreateTime());
			
			values.put("classTotalRanking", examRound.getClassTotalRanking());
			values.put("gradeTotalRanking", examRound.getGradeTotalRanking());
			values.put("classTotalAverage", examRound.getClassTotalAverage());
			values.put("gradeTotalAverage", examRound.getGradeTotalAverage());
			values.put("examRoundTitle", examRound.getExamRoundTitle());
			
			List<ExamRound> lh = this.getExamRoundExist(examRound.getClassId(),
					examRound.getExamRoundId());
			if(lh.size()==0){
				db.insertOrThrow("ExamRound", null, values);
			}
		}
	}
	
	
	private List<ExamRound> getExamRoundExist(String classId,
			String examRoundId) {
		String where = String.format(" classId = '%s' AND examRoundId = '%s'  ", 
				classId, examRoundId);
		return getExamRoundByWhere(where);
	}
	
	
	
	private ArrayList<ExamRound> getExamRoundByWhere(String where) {
		ArrayList<ExamRound> list = new ArrayList<ExamRound>();
		String orderBy = "examDate desc ";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("ExamRound", new String[] { "cacheUserId",  "classId",
				"examRoundId","examDate","totalScore","createTime", "classTotalRanking"
				,"gradeTotalRanking","classTotalAverage","gradeTotalAverage","examRoundTitle"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchExamRound(result));
			} while (result.moveToNext());
		}

		return list;
	}

	
	private ExamRound fetchExamRound(Cursor result) {
		ExamRound examRound = new ExamRound();
		examRound.setUserId(result.getString(result.getColumnIndex("cacheUserId")));
		examRound.setClassId(result.getString(result.getColumnIndex("classId")));
		examRound.setExamRoundId(result.getString(result.getColumnIndex("examRoundId")));
		examRound.setExamDate(result.getString(result.getColumnIndex("examDate")));
		examRound.setTotalScore(result.getString(result.getColumnIndex("totalScore")));
		examRound.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		examRound.setClassTotalRanking(result.getString(result.getColumnIndex("classTotalRanking")));
		examRound.setGradeTotalRanking(result.getString(result.getColumnIndex("gradeTotalRanking")));
		examRound.setClassTotalAverage(result.getString(result.getColumnIndex("classTotalAverage")));
		examRound.setGradeTotalAverage(result.getString(result.getColumnIndex("gradeTotalAverage")));
		examRound.setExamRoundTitle(result.getString(result.getColumnIndex("examRoundTitle")));
		return examRound;
	}


	public ArrayList<ExamRound> getExamRound(String classId,String year) {
		String startDate = "";
		String endDate = "";
		if(year==null || ("").equals(year)){
			year = String.valueOf(DateTime.now().getYear());
		}
		startDate = year+"0101";
		endDate  = year+"1231";
		String where = String.format("cacheUserId = '%s' AND classId = '%s' AND examDate between '%s'  and '%s' ", mUserId, classId,
				startDate,endDate);
		return getExamRoundByWhere(where);
	}



}
