package com.stinfo.pushme.entity;

import java.io.Serializable;

public class Group implements Serializable {
	private static final long serialVersionUID = -6096816215063927031L;
	
	private String groupName;
	private String groupId;
	private String objectType;
	private int groupType;
	
	public Group() {
		super();
	}
	
	public Group(String groupName, String groupId, String objectType, int groupType) {
		this.groupName = groupName;
		this.groupId = groupId;
		this.objectType = objectType;
		this.groupType = groupType;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}
}