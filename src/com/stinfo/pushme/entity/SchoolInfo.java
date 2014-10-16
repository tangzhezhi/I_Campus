package com.stinfo.pushme.entity;

import java.io.Serializable;

public class SchoolInfo  implements Serializable {

	private static final long serialVersionUID = -2915752528446400804L;
	
	private String schoolno;
	private String schoolname;
	public String getSchoolno() {
		return schoolno;
	}
	public void setSchoolno(String schoolno) {
		this.schoolno = schoolno;
	}
	public String getSchoolname() {
		return schoolname;
	}
	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	
}
