package com.stinfo.pushme.entity;

import java.util.Comparator;

public class UserComparator implements Comparator<UserInfo> {
	
	@Override
	public int compare(UserInfo lhs, UserInfo rhs) {
		String pinYin1 = lhs.getPinYin();
		String pinYin2 = rhs.getPinYin();
		return pinYin1.compareTo(pinYin2);
	}
}
