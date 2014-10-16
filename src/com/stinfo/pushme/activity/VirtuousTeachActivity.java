package com.stinfo.pushme.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.CommonPagerAdapter;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.fragment.VirtuousTeachDetailListFragment;
import com.stinfo.pushme.fragment.VirtuousTeachParentFragment;
import com.stinfo.pushme.fragment.VirtuousTeachTeacherFragment;

/**
 * 德育模块
 * @author lenovo
 *
 */
public class VirtuousTeachActivity extends BaseActionBarFragmentActivity  
	implements OnPageChangeListener, OnNavigationListener  {
	private static final String TAG = "VirtuousTeachActivity";
	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragmentsList;
	private CommonPagerAdapter mAdapter;
	private int checkItem;
	private String[] mItems;
	private String userType;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public int getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(int checkItem) {
		this.checkItem = checkItem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_virtuous_teach);
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
	
	
	/**
	 * 初始化ActionBar工具条
	 */
	private void initView() {
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.virtuous_teach));
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	
	/**
	 * 初始化ViewPaper：填充Fragment
	 */
	private void initPagerView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_virtuous_teach_container);
		mFragmentsList = new ArrayList<Fragment>();
		Fragment indexFrag = new Fragment();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		if(userInfo instanceof Parent){
			Log.d(TAG, "用户是家长");
			userType = AppConstant.UserType.PARENT;
			indexFrag = VirtuousTeachParentFragment.newInstance();
		}
		else if(userInfo instanceof Teacher){
			Log.d(TAG, "用户是老师");
			userType = AppConstant.UserType.TEACHER;
			indexFrag = VirtuousTeachTeacherFragment.newInstance();
		}
		else if(userInfo instanceof Student){
			Log.d(TAG, "用户是学生");
			userType = AppConstant.UserType.STUDENT;
			Intent intent = new Intent();
		    intent.setClass(this, VirtuousTeachDetailActivity.class);  
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putString("studentId", userInfo.getUserId());
			bundle.putString("studentName", userInfo.getUserName());
			bundle.putString("userType", String.valueOf(userType));
			intent.putExtras(bundle);
			indexFrag = VirtuousTeachDetailListFragment.newInstance();
			startActivity(intent);
			this.finish();
		}
		else{
			Log.d(TAG, "用户是什么类型");
			userType = "-1";
		}
		
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
		Log.d(TAG, "点击了"+itemPosition);
        return false;  
	}
	
	
}
