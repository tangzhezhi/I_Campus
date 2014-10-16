package com.stinfo.pushme.entity;

import java.util.Comparator;

public class HomeworkComparator implements Comparator<Homework> {
	
	@Override
	public int compare(Homework lhs, Homework rhs) {
		String createtime1 = lhs.getCreateTime();
		String createtime2 = rhs.getCreateTime();
		return createtime2.compareTo(createtime1);
	}
}
