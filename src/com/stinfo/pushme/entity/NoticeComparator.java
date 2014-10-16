package com.stinfo.pushme.entity;

import java.util.Comparator;

public class NoticeComparator implements Comparator<Notice> {
	
	@Override
	public int compare(Notice lhs, Notice rhs) {
		String createtime1 = lhs.getCreateTime();
		String createtime2 = rhs.getCreateTime();
		return createtime2.compareTo(createtime1);
	}
}
