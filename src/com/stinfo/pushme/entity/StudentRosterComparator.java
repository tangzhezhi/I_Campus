package com.stinfo.pushme.entity;

import java.util.Comparator;

public class StudentRosterComparator implements Comparator<StudentRoster> {
	
	@Override
	public int compare(StudentRoster lhs, StudentRoster rhs) {
		String pinYin1 = lhs.getPinYin();
		String pinYin2 = rhs.getPinYin();
		return pinYin1.compareTo(pinYin2);
	}
}
