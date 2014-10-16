package com.stinfo.pushme.entity;

import java.io.Serializable;

public class ClassInfo implements Serializable {
	private static final long serialVersionUID = 7597034860148042883L;

	private String schoolId = "";
	private String classId = "";
	private String className = "";
	
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

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

	@Override
	public String toString() {
		return className;
	}
}
