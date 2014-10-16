package com.stinfo.pushme.entity;

import java.util.ArrayList;

/**
 * 闪光台、曝光台、爱心分享--详细实体
 * @author lenovo
 *
 */
public class DeedDetail {
	
	private String id;
	private String title;
	private String content;
	
	private String clicknum;
	private String commentsize;
	private String isdel;
	private String iselite;
	private String istop;
	private String parisesize;
	
	private ArrayList<String> pics;
	private String releasedate;
	private String releaseoperid;
	
	private String releaseuserPicPath;
	private String releaseusername;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getClicknum() {
		return clicknum;
	}
	public void setClicknum(String clicknum) {
		this.clicknum = clicknum;
	}
	public String getCommentsize() {
		return commentsize;
	}
	public void setCommentsize(String commentsize) {
		this.commentsize = commentsize;
	}
	public String getIsdel() {
		return isdel;
	}
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}
	public String getIselite() {
		return iselite;
	}
	public void setIselite(String iselite) {
		this.iselite = iselite;
	}
	public String getIstop() {
		return istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}
	public String getParisesize() {
		return parisesize;
	}
	public void setParisesize(String parisesize) {
		this.parisesize = parisesize;
	}
	
	public ArrayList<String> getPics() {
		return pics;
	}
	public void setPics(ArrayList<String> pics) {
		this.pics = pics;
	}
	public String getReleasedate() {
		return releasedate;
	}
	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}
	public String getReleaseoperid() {
		return releaseoperid;
	}
	public void setReleaseoperid(String releaseoperid) {
		this.releaseoperid = releaseoperid;
	}
	public String getReleaseuserPicPath() {
		return releaseuserPicPath;
	}
	public void setReleaseuserPicPath(String releaseuserPicPath) {
		this.releaseuserPicPath = releaseuserPicPath;
	}
	public String getReleaseusername() {
		return releaseusername;
	}
	public void setReleaseusername(String releaseusername) {
		this.releaseusername = releaseusername;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
