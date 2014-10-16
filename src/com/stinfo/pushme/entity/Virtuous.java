package com.stinfo.pushme.entity;

import java.io.Serializable;

public class Virtuous implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4740126425746391966L;
	
	private String id;
	private String moralid;
	
	private String name;
	private String val;
	private String remark;
	private String status;
	private String moralName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoralid() {
		return moralid;
	}
	public void setMoralid(String moralid) {
		this.moralid = moralid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMoralName() {
		return moralName;
	}
	public void setMoralName(String moralName) {
		this.moralName = moralName;
	}

}
