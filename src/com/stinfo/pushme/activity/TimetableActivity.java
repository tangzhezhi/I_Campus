package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.CommonPagerAdapter;
import com.stinfo.pushme.adapter.HomeworkListAdapter;
import com.stinfo.pushme.adapter.TimetableWeekAdapter;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.TimetableDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Timetable;
import com.stinfo.pushme.fragment.TimetableWeekFragment;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryTimetableReq;
import com.stinfo.pushme.rest.entity.QueryTimetableResp;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;

public class TimetableActivity extends BaseActionBarFragmentActivity  implements OnPageChangeListener, OnNavigationListener  {
	private static final String TAG = "TimetableActivity";
	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragmentsList;
	private CommonPagerAdapter mAdapter;
	private int checkItem;
//	
	public int getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(int checkItem) {
		this.checkItem = checkItem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class);
		initView();
		initPagerView();
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.class_select, menu);
		return false;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
	
	private void onSelectDay() {
		Log.d(TAG, "点击了  天  ");
	}

	private void onSelectWeek() {
		Log.d(TAG, "点击了  周 ");
	}
	
	/**
	 * 初始化ActionBar工具条
	 */
	private void initView() {
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.timetable));
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	
	/**
	 * 初始化ViewPaper：填充Fragment
	 */
	private void initPagerView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_timetable_container);
		mFragmentsList = new ArrayList<Fragment>();
		Fragment indexFrag = TimetableWeekFragment.newInstance();
		mFragmentsList.add(indexFrag);
		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(0);
	}
	
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int pageItems) {
		setCurrentPage(pageItems);
	}

	private void setCurrentPage(int pageItems) {
		
	}


	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//    	selectTimetableWeekList(itemPosition);
//    	checkItem = itemPosition;
        return false;  
	}
	
	
}
