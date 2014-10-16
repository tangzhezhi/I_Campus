package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

/**
 * 新增评论
 * @author lenovo
 */
public class PostCommentReq extends BaseRequest {
	private String sessionKey = "";
	private String behaviorid = "";  // 关联字段
	private String coperid = ""; 	//评论人登陆账户id
	private String cusername=""; //评论人 登陆用户名
	private String comment = ""; // 评论内容
	private String replyoperid=""; // 被回复人 登陆账户id
	private String replyusername = ""; // 被回复人 登陆用户名
	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	public String getBehaviorid() {
		return behaviorid;
	}

	public void setBehaviorid(String behaviorid) {
		this.behaviorid = behaviorid;
	}

	public String getCoperid() {
		return coperid;
	}

	public void setCoperid(String coperid) {
		this.coperid = coperid;
	}

	public String getCusername() {
		return cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReplyoperid() {
		return replyoperid;
	}

	public void setReplyoperid(String replyoperid) {
		this.replyoperid = replyoperid;
	}

	public String getReplyusername() {
		return replyusername;
	}

	public void setReplyusername(String replyusername) {
		this.replyusername = replyusername;
	}

	@Override
	public String getPath() {
		return AppConstant.School_Platform_BASE_URL + "behavior/setComment";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("behaviorid", this.behaviorid);
		paramsHashMap.put("coperid", this.coperid);
		paramsHashMap.put("sessionkey", this.sessionKey);
		paramsHashMap.put("cusername", this.cusername);
		paramsHashMap.put("comment", this.comment);
		paramsHashMap.put("replyoperid", this.replyoperid);
		paramsHashMap.put("replyusername", this.replyusername);
		return paramsHashMap;
	}
}
