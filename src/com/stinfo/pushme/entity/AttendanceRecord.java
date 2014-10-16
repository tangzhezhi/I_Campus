package com.stinfo.pushme.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.stinfo.pushme.util.PinYinUtil;

public class AttendanceRecord  implements Serializable {
	private static final long serialVersionUID = 2353219003803979291L;
	private String id;
	private String userId;
	private String userName;
	private String cardNo;
	private ArrayList<OutOrInDoorRecord> accessTimes;
	private String userPic;
	private String attendanceDay;
	
	private String initial = "0";
	private String pinYin = "";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ArrayList<OutOrInDoorRecord> getAccessTimes() {
		return accessTimes;
	}
	public void setAccessTimes(ArrayList<OutOrInDoorRecord> accessTimes) {
		this.accessTimes = accessTimes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName.trim();
		this.pinYin = PinYinUtil.getPinYin(this.userName);
		if (TextUtils.isEmpty(this.pinYin)) {
			this.initial = "0";
		} else {
			this.initial = String.valueOf(this.pinYin.charAt(0));
		}
	}
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public String getAttendanceDay() {
		return attendanceDay;
	}
	public void setAttendanceDay(String attendanceDay) {
		this.attendanceDay = attendanceDay;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	
}
