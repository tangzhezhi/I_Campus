package com.stinfo.pushme.entity;

import java.io.Serializable;

/**
 * 道德币
 * @author lenovo
 *
 */
public class VirtuousTeach implements Serializable {  
	private static final long serialVersionUID = -5680755381452484527L;
	
	private String id;
	private String classId;
	private String createTime;
	private String userId;
	private String coin;
	private String userType;
	private String userName;
	private String startDate;
	private String endDate;
	
	private String studentid;
	private String studentname;
	
	
	private String recorddate;
	
	private String virtousType;
	private String virtousScore;
	private String virtousReason;
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public String getStudentname() {
		return studentname;
	}
	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}
	public String getRecorddate() {
		return recorddate;
	}
	public void setRecorddate(String recorddate) {
		this.recorddate = recorddate;
	}
	public String getVirtousType() {
		return virtousType;
	}
	public void setVirtousType(String virtousType) {
		this.virtousType = virtousType;
	}
	public String getVirtousScore() {
		return virtousScore;
	}
	public void setVirtousScore(String virtousScore) {
		this.virtousScore = virtousScore;
	}
	public String getVirtousReason() {
		return virtousReason;
	}
	public void setVirtousReason(String virtousReason) {
		this.virtousReason = virtousReason;
	}
	
}
