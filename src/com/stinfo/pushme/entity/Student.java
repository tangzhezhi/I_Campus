package com.stinfo.pushme.entity;

import java.io.Serializable;

public class Student extends UserInfo implements Serializable {
	private static final long serialVersionUID = 7364654691025862083L;
	
	private String schoolId;
	private String studentNo;
	private String classId;
	private String className;
	private String schoolname;
	
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
	
	public String getSchoolId() {
		return schoolId;
	} 	 	

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	
}
