package com.stinfo.pushme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.TabsAdapter;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.fragment.VirtuousTeachDetailListFragment;
import com.stinfo.pushme.fragment.VirtuousTeachDetailPostFragment;

/**
 * 德育--详细
 * @author lenovo
 *
 */
public class VirtuousTeachDetailActivity  extends BaseActionBarFragmentActivity {
	private String TAG = "VirtuousTeachDetailActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);
		
		/**
		 * 工具栏--导航tab模式
		 */
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.virtuous_teach));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
		
		//在导航上添加上公告页面--ViewPager
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		if(userInfo instanceof Teacher){
			mTabsAdapter.addTab(bar.newTab().setText("奖励表单"), VirtuousTeachDetailPostFragment.class, null);
		}
		mTabsAdapter.addTab(bar.newTab().setText("历史记录"), VirtuousTeachDetailListFragment.class, null);
		
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
	
	/**
	 * 菜单被分为如下三种，选项菜单（OptionsMenu）、上下文菜单（ContextMenu）和子菜单（SubMenu）
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	/**
	 * 菜单按钮选择
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

}
