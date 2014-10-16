package com.stinfo.pushme.entity;

import java.io.Serializable;

import com.stinfo.pushme.util.PinYinUtil;

/*
 * 该类用于班级通讯录"家长"分组的显示
 */
public class ParentRoster extends UserInfo implements Serializable {
	private static final long serialVersionUID = -7519856984306049874L;

	private String childUserId = "";
	private String childName = "";
	private String childPinYin = "";
	private String schoolId = "";
	private String classId = "";
	private String className = "";
	
	private String parentValue="";
	
	public String getChildUserId() {
		return childUserId;
	}

	public void setChildUserId(String childUserId) {
		this.childUserId = childUserId;
	}

	public String getChildName() {
		return childName;
	}
	
	public void setChildName(String childName) {
		this.childName = childName.trim();
		this.childPinYin = PinYinUtil.getPinYin(this.childName);
	}
	
	public String getChildPinYin() {
		return childPinYin;
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

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	
}
