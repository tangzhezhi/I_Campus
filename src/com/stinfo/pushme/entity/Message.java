package com.stinfo.pushme.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.common.AppConstant.MessageSendStatus;
import com.stinfo.pushme.util.DateTimeUtil;

public class Message implements Serializable {
	private static final long serialVersionUID = -7102656942282931838L;

	/**
	 * 消息编号
	 */
	private int msgId = 0;

	/**
	 * 消息类型: 1-普通消息；2-通知公告；3-家庭作业；4-日常表现
	 */
	private int msgType;

	/**
	 * 发送用户编号
	 */
	private String senderId;

	/**
	 * 接收用户编号(userId/schoolId/classId)
	 */
	private String receiverId;

	/**
	 * 接收对象类型：接收对象类型：  01-学生  03-家长  02-老师
	 */
	private String objectType;

	/**
	 * 组别类型：0-个人；1-学校；2-班级
	 */
	private int groupType;

	/**
	 * 消息内容
	 */
	private String content;

	/**
	 * 发送时间
	 */
	private String createTime;

	/**
	 * 送出标志：1-是；0-否
	 */
	private int outFlag;

	/**
	 * 发送状态：1-正在发送；2-发送成功；3-发送失败
	 */
	private int sendStatus;

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getOutFlag() {
		return outFlag;
	}

	public void setOutFlag(int outFlag) {
		this.outFlag = outFlag;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public static Message fromJson(String jsonStr, int outFlag) throws JSONException {
		Message msg = new Message();
		
		jsonStr = jsonStr.toLowerCase();
		
		JSONObject msgObj = new JSONObject(jsonStr);
//		JSONObject msgObj = rootObj.getJSONObject("description");
		
		
		
		msg.msgType = msgObj.getInt("msgtype");
		msg.senderId = msgObj.getString("senderid");
		msg.receiverId = msgObj.getString("receiverid");
		msg.objectType = msgObj.getString("receivetype");
//		msg.groupType = msgObj.getInt("grouptype");
		msg.content = msgObj.getString("content");
		// 采用手机本地时间
		msg.createTime = DateTimeUtil.getLongTime();
		msg.sendStatus = MessageSendStatus.SUCCESS;
		return msg;
	}
}
