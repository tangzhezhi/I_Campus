package com.stinfo.pushme.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Parent extends UserInfo implements Serializable {
	private static final long serialVersionUID = 3243588124379181970L;

	private ArrayList<Student> childList = new ArrayList<Student>();

	public ArrayList<Student> getChildList() {
		return childList;
	}

	public void setChildList(ArrayList<Student> childList) {
		this.childList = childList;
	}

	public void addOrderList(ArrayList<Student> list) {
		this.childList.addAll(list);
	}

	public void addOrderItem(Student item) {
		this.childList.add(item);
	}
}
