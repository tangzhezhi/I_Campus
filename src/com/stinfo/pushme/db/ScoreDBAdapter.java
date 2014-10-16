package com.stinfo.pushme.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.Score;
import com.stinfo.pushme.entity.StudentRoster;

@SuppressLint("DefaultLocale")
public class ScoreDBAdapter extends DBAdapter {
	
	public void addScore(ArrayList<Score> list) throws SQLException {
		for (Score score : list) {
			ContentValues values = new ContentValues();
			values.put("cacheUserId", mUserId);
			values.put("userId", score.getUserId());
			values.put("examRoundId", score.getExamRoundId());
			values.put("examDate", score.getExamDate());
			values.put("subject", score.getSubject());
			values.put("createTime", score.getCreateTime());
			values.put("examPaperId", UUID.randomUUID().toString());
			values.put("examPaperTitle", score.getExamPaperTitle());
			values.put("examPaperScore", score.getExamPaperScore());
			values.put("classRanking", score.getClassRanking());
			values.put("gradeRanking", score.getGradeRanking());
			
			List<Score> lh = this.getScoreExist(score.getUserId(),
					score.getExamDate(),score.getSubject());
			if(lh.size()==0){
				db.insertOrThrow("score", null, values);
			}
		}
	}
	
	
	private List<Score> getScoreExist(String userId,
			String examDate,String subject) {
		String where = String.format(" userId = '%s' AND examDate = '%s' AND  subject='%s' ", 
				userId, examDate,subject);
		return getScoreByWhere(where);
	}
	
	private ArrayList<Score> getScoreByWhere(String where) {
		ArrayList<Score> list = new ArrayList<Score>();
		String orderBy = "examDate desc ";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Score", new String[] { "cacheUserId","userId", 
				"examRoundId","subject","examPaperId","examPaperTitle", "examDate"
				,"createTime","examPaperScore","classRanking","gradeRanking"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchScore(result));
			} while (result.moveToNext());
		}

		return list;
	}

	
	private Score fetchScore(Cursor result) {
		Score score = new Score();
		score.setUserId(result.getString(result.getColumnIndex("userId")));
		score.setCacheUserId(result.getString(result.getColumnIndex("cacheUserId")));
		score.setExamRoundId(result.getString(result.getColumnIndex("examRoundId")));
		score.setExamDate(result.getString(result.getColumnIndex("examDate")));
		score.setSubject(result.getString(result.getColumnIndex("subject")));
		score.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		score.setExamPaperId(result.getString(result.getColumnIndex("examPaperId")));
		score.setExamPaperTitle(result.getString(result.getColumnIndex("examPaperTitle")));
		score.setExamPaperScore(result.getString(result.getColumnIndex("examPaperScore")));
		score.setClassRanking(result.getString(result.getColumnIndex("classRanking")));
		score.setGradeRanking(result.getString(result.getColumnIndex("gradeRanking")));
		return score;
	}


	public ArrayList<Score> getAllScoreDetail(String examRoundId,String studentId) {
		String where = String.format(" examRoundId = '%s' AND userId= '%s' ", 
				 examRoundId,studentId
				);
		return getScoreByWhere(where);
	}
	
	
	
	public ArrayList<StudentRoster> getClassStudentRosterOfParent(String classId,String parentId) {
		
		ArrayList<StudentRoster> slist = new ArrayList<StudentRoster>();
		
		String where = String.format("userId = '%s' AND classId = '%s'", parentId, classId);
		 ArrayList<ParentRoster> parentList = getParentRosterByWhere(where);
		
		 for(ParentRoster parentRoster : parentList){
			 String sUserId = parentRoster.getChildUserId();
			 String sClassId = parentRoster.getClassId();
			 String whereStu = String.format("userId = '%s' AND classId = '%s'", sUserId, sClassId);
			 slist.addAll( getStudentRosterByWhere(whereStu));
			 
		 }
		return slist;
	}


}
