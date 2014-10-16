package com.stinfo.pushme.entity;

import java.io.Serializable;

/**
 * 成绩实体类
 * @author lenovo
 *
 */
public class Score implements Serializable {  
	private static final long serialVersionUID = -5680755381452484527L;
	private String cacheUserId;
	private String examRoundId;
	private String userId;
	private String subject;
	private String examPaperId;
	private String examPaperTitle;
	private String examDate;
	private String createTime;
	private String examPaperScore;
	private String classRanking;
	private String gradeRanking;
	
	private String studentid;
	private String schoolno;
	private String classid;
	
	
	public String getCacheUserId() {
		return cacheUserId;
	}

	public void setCacheUserId(String cacheUserId) {
		this.cacheUserId = cacheUserId;
	}

	public String getExamRoundId() {
		return examRoundId;
	}

	public void setExamRoundId(String examRoundId) {
		this.examRoundId = examRoundId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(String examPaperId) {
		this.examPaperId = examPaperId;
	}

	public String getExamPaperTitle() {
		return examPaperTitle;
	}

	public void setExamPaperTitle(String examPaperTitle) {
		this.examPaperTitle = examPaperTitle;
	}

	public String getExamDate() {
		return examDate;
	}

	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	public String getExamPaperScore() {
		return examPaperScore;
	}

	public void setExamPaperScore(String examPaperScore) {
		this.examPaperScore = examPaperScore;
	}

	public String getClassRanking() {
		return classRanking;
	}

	public void setClassRanking(String classRanking) {
		this.classRanking = classRanking;
	}

	public String getGradeRanking() {
		return gradeRanking;
	}

	public void setGradeRanking(String gradeRanking) {
		this.gradeRanking = gradeRanking;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getSchoolno() {
		return schoolno;
	}

	public void setSchoolno(String schoolno) {
		this.schoolno = schoolno;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}
	
	
}
