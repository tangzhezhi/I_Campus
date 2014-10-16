package com.stinfo.pushme.entity;

import java.io.Serializable;

public class Course implements Serializable {
	private static final long serialVersionUID = -6132420372214601393L;
	
	private String coursecode;
	private String coursename;
	
	public String getCoursecode() {
		return coursecode;
	}
	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	
}
