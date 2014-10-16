package com.stinfo.pushme.entity;

import java.io.Serializable;

public class TeacherRole implements Serializable {
	private static final long serialVersionUID = 7695603770925424909L;
	
	private String classId;
	private String className;
	private String classmaster;
	private String coursecode;
	private String coursename;
	
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public String getClassmaster() {
		return classmaster;
	}

	public void setClassmaster(String classmaster) {
		this.classmaster = classmaster;
	}

	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	
}