package com.stinfo.pushme.util;

import com.stinfo.pushme.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentSwitcher {

	public static void switchFragment(FragmentManager fragMgr, Fragment from, Fragment to) {
		if (from != to) {
			/*
			FragmentTransaction transaction = fragMgr.beginTransaction().setCustomAnimations(
					android.R.anim.fade_in, R.anim.abc_fade_out);
			*/
			FragmentTransaction transaction = fragMgr.beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(from).add(R.id.fragment_container, to).commit();
			} else {
				transaction.hide(from).show(to).commit();
			}
		}
	}
}
