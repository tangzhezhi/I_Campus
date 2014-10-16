package com.stinfo.pushme.entity;

import java.io.Serializable;

/**
 * 考试轮次
 * @author lenovo
 *
 */
public class ExamRound implements Serializable {  
	private static final long serialVersionUID = -5680755381452484527L;
	
	private String schoolno;
	private String classId;
	private String examRoundId;
	private String examRoundTitle;
	private String examDate;
	private String createTime;
	private String userId;
	private String totalScore;
	private String classTotalRanking;
	private String gradeTotalRanking;
	private String classTotalAverage;
	private String gradeTotalAverage;
	
	private String ayear;
	private String atrem;
	private String gradecode;
	
	
	
	public String getExamRoundTitle() {
		return examRoundTitle;
	}

	public void setExamRoundTitle(String examRoundTitle) {
		this.examRoundTitle = examRoundTitle;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getExamRoundId() {
		return examRoundId;
	}

	public void setExamRoundId(String examRoundId) {
		this.examRoundId = examRoundId;
	}

	public String getExamDate() {
		return examDate;
	}

	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getClassTotalRanking() {
		return classTotalRanking;
	}

	public void setClassTotalRanking(String classTotalRanking) {
		this.classTotalRanking = classTotalRanking;
	}

	public String getGradeTotalRanking() {
		return gradeTotalRanking;
	}

	public void setGradeTotalRanking(String gradeTotalRanking) {
		this.gradeTotalRanking = gradeTotalRanking;
	}

	public String getClassTotalAverage() {
		return classTotalAverage;
	}

	public void setClassTotalAverage(String classTotalAverage) {
		this.classTotalAverage = classTotalAverage;
	}

	public String getGradeTotalAverage() {
		return gradeTotalAverage;
	}

	public void setGradeTotalAverage(String gradeTotalAverage) {
		this.gradeTotalAverage = gradeTotalAverage;
	}

	public String getSchoolno() {
		return schoolno;
	}

	public void setSchoolno(String schoolno) {
		this.schoolno = schoolno;
	}

	public String getAyear() {
		return ayear;
	}

	public void setAyear(String ayear) {
		this.ayear = ayear;
	}

	public String getAtrem() {
		return atrem;
	}

	public void setAtrem(String atrem) {
		this.atrem = atrem;
	}

	public String getGradecode() {
		return gradecode;
	}

	public void setGradecode(String gradecode) {
		this.gradecode = gradecode;
	}
	
	
}
