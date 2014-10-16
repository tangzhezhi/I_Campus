package com.stinfo.pushme.entity;

import java.io.Serializable;

/*
 * 该类用于班级通讯录"学生"分组的显示
 */
public class StudentRoster extends UserInfo implements Serializable {
	private static final long serialVersionUID = 4364654691025862083L;

	private String schoolId;
	private String studentNo;
	private String classId;
	private String className;

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
}
