package com.stinfo.pushme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.HomeworkDetailAdapter;
import com.stinfo.pushme.entity.Homework;

import java.util.ArrayList;


public class HomeworkDetailActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private ArrayList<Homework> mhomeworkList;
	private int mIndex = 0;
	private HomeworkDetailAdapter mAdapter;
	private ViewPager mViewPager;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homework_detail);
		mhomeworkList = (ArrayList<Homework>) getIntent().getSerializableExtra("homeworkList");
		mIndex = getIntent().getIntExtra("index", 0);
		
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setTitle(getResources().getString(R.string.view_content));
		initView();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	private void initView() {
		mAdapter = new HomeworkDetailAdapter(getSupportFragmentManager(), mhomeworkList);
        mViewPager = (ViewPager) findViewById(R.id.vp_homework_detail);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(mIndex);
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
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
	}
}
