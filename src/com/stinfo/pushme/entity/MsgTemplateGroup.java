package com.stinfo.pushme.entity;

import java.io.Serializable;

/**
 * 消息模板分组
 * @author lenovo
 *
 */
public class MsgTemplateGroup implements Serializable {  
	/**
	 * 
	 */
	private static final long serialVersionUID = -7825209999545756692L;

	private String groupName;
	
	private String groupCode;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
}
