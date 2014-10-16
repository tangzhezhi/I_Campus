package com.stinfo.pushme.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.NoticeDetailAdapter;
import com.stinfo.pushme.entity.Notice;

import java.util.ArrayList;


public class NoticeDetailActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private ArrayList<Notice> mNoticeList;
	private int mIndex = 0;
	private NoticeDetailAdapter mAdapter;
	private ViewPager mViewPager;
	private boolean isReply = false;
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_detail);
		mNoticeList = (ArrayList<Notice>) getIntent().getSerializableExtra("noticeList");
		mIndex = getIntent().getIntExtra("index", 0);
		isReply = getIntent().getBooleanExtra("reply", false);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setTitle(getResources().getString(R.string.view_content));
		initView();
	}
	
	private void initView() {
		mAdapter = new NoticeDetailAdapter(getSupportFragmentManager(), mNoticeList,isReply);
        mViewPager = (ViewPager) findViewById(R.id.vp_notice_detail);
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
