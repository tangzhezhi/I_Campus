package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GuidePagerAdapter extends PagerAdapter {
	private ArrayList<View> mViews;

	public GuidePagerAdapter(ArrayList<View> views) {
		mViews = views;
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(mViews.get(position), 0);
		return mViews.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return (view == arg1);
	}

	@Override
	public void destroyItem(View view, int position, Object arg2) {
		((ViewPager) view).removeView(mViews.get(position));
	}
}