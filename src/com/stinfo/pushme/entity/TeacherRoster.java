package com.stinfo.pushme.entity;

import java.io.Serializable;

/*
 * 该类用于班级通讯录"老师"分组的显示
 */
public class TeacherRoster extends UserInfo implements Serializable {
	private static final long serialVersionUID = -6304562226740146915L;
	
	private String teacherId;
	private String role;
	private String schoolId;
	private String classId;
	private String className;
	private String rolecode;
	private String rolename;

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

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

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}
