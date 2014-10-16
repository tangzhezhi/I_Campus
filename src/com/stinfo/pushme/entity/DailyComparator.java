package com.stinfo.pushme.entity;

import java.util.Comparator;

public class DailyComparator implements Comparator<Daily> {
	
	@Override
	public int compare(Daily lhs, Daily rhs) {
		String createtime1 = lhs.getCreateTime();
		String createtime2 = rhs.getCreateTime();
		return createtime2.compareTo(createtime1);
	}
}
