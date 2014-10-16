package com.stinfo.pushme.entity;

import java.io.Serializable;

public class Timetable implements Serializable {
	private static final long serialVersionUID = -4883532639924853246L;
	
	private String timetableId;
	private String cacheUserId;
	private String subject;
	private String classId;
	private String content;
	private String createTime;
	private String section;   
	private String classDate; //日期 yyyyMMdd
	private String weekDay; //星期几
	private String coursecode;
	
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTimetableId() {
		return timetableId;
	}

	public void setTimetableId(String timetableId) {
		this.timetableId = timetableId;
	}

	public String getCacheUserId() {
		return cacheUserId;
	}

	public void setCacheUserId(String cacheUserId) {
		this.cacheUserId = cacheUserId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getClassDate() {
		return classDate;
	}

	public void setClassDate(String classDate) {
		this.classDate = classDate;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}
	
}
