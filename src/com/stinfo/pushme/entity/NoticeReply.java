package com.stinfo.pushme.entity;

import java.io.Serializable;

/**
 * 通知回复实体类
 * @author lenovo
 *
 */
public class NoticeReply implements Serializable {  
	
	private static final long serialVersionUID = 47084438489948998L;
	private String id;
	private String authorid;
	private String authorname;
	private String content;
	private String answerTime;
	private String noticeId;
	
	
	
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}
	
}
