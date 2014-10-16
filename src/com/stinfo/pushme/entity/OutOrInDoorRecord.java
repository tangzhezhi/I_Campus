package com.stinfo.pushme.entity;

public class OutOrInDoorRecord {
	
	private String id;
	private String pid;
	private String ioTime;
	private String ioFlag; //考勤记录状态 10 出校 20 进校
	private String cardno;
	private String termid; //设备id
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getIoTime() {
		return ioTime;
	}
	public void setIoTime(String ioTime) {
		this.ioTime = ioTime;
	}
	public String getIoFlag() {
		return ioFlag;
	}
	public void setIoFlag(String ioFlag) {
		this.ioFlag = ioFlag;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getTermid() {
		return termid;
	}
	public void setTermid(String termid) {
		this.termid = termid;
	}
	
}
