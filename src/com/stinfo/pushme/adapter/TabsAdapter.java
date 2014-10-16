package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

/**
 *  使用Fragment 来表示一页，显得更加简单和直观，
 *  Fragment  本身提供的一些特性可以让我们方便的对每一页进行管理，
 *  使用FragmentManager可以根据ID或TAG来查找Fragment ，
 *   动态添加、删除、替换，Fragment  可以管理自己的生命周期，
 *   像Activity一样提供了一些生命周期回调方法
 *   
 *   让Fragment 成为ViewPager的一页时，
 *   FragmentManager会一直保存管理创建好了的Fragment，
 *   即使当前不是显示的这一页，Fragment对象也不会被销毁，
 *   在后台默默等待重新显示。但如果Fragment不再可见时，它的视图层次会被销毁掉，
 *   下次显示时视图会重新创建。
     出于使用FragmentPagerAdapter时，
     Fragment对象会一直存留在内存中，所以当有大量的显示页时，
     就不适合用FragmentPagerAdapter 了，FragmentPagerAdapter  适用于只有少数的page情况，像选项卡。
     这个时候你可以考虑使用FragmentStatePagerAdapter   ，当使用FragmentStatePagerAdapter  时，
     如果Fragment不显示，那么Fragment对象会被销毁，
     但在回调onDestroy()方法之前会回调onSaveInstanceState(Bundle outState)方法来保存Fragment的状态，
     下次Fragment显示时通过onCreate(Bundle savedInstanceState)把存储的状态值取出来，FragmentStatePagerAdapter 
      比较适合页面比较多的情况，像一个页面的ListView   
     最后一点要注意，当使用FragmentPagerAdapter 时一定要为它的宿主ViewPager设置一个有效的ID ! 
 *   
 * @author lenovo
 *
 */
public class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener,
		ViewPager.OnPageChangeListener {

	private final Context mContext;
	private final ActionBar mActionBar;
	private final ViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	static final class TabInfo {
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(Class<?> _class, Bundle _args) {
			clss = _class;
			args = _args;
		}
	}
	
	
	public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
		mActionBar = activity.getSupportActionBar();
		mViewPager = pager;
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}

	/**
	 * 将Fragment放入ActionBar
	 * @param tab
	 * @param clss
	 * @param args
	 */
	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
		TabInfo info = new TabInfo(clss, args);
		tab.setTag(info);
		tab.setTabListener(this);

		mTabs.add(info);
		mActionBar.addTab(tab);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Object tag = tab.getTag();
		for (int i = 0; i < mTabs.size(); i++) {
			if (mTabs.get(i) == tag) {
				mViewPager.setCurrentItem(i);
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}