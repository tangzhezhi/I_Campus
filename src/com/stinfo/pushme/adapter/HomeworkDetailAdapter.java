package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.fragment.HomeworkDetailFragment;

public final class HomeworkDetailAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Homework> mhomeworkList;

	public HomeworkDetailAdapter(FragmentManager fm, ArrayList<Homework> homeworkList) {
		super(fm);
		mhomeworkList = homeworkList;
	}
	
	@Override
	public int getCount() {
		return mhomeworkList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return HomeworkDetailFragment.newInstance(mhomeworkList.get(position));
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		return super.instantiateItem(arg0, arg1);
	}

	@Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}