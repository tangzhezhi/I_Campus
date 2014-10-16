package com.stinfo.pushme.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.DeedListAdapter;
import com.stinfo.pushme.adapter.TabsAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.DeedSort;
import com.stinfo.pushme.common.AppConstant.DeedType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Deed;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.fragment.GoodDeedFragment;
import com.stinfo.pushme.fragment.NoticeSchoolFragment;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryDeedReq;
import com.stinfo.pushme.rest.entity.QueryDeedResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

/**
 * 闪光台
 * @author lenovo
 *
 */
public class GoodDeedShowActivity extends BaseActionBarActivity {
	private static final String TAG = "GoodDeedShowActivity";
	UserCache userCache = UserCache.getInstance();
	UserInfo userInfo = userCache.getUserInfo();
	private ProgressDialog prgDialog = null;
	
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	
	

	public void showProgressDialog() {
		prgDialog = new ProgressDialog(GoodDeedShowActivity.this);
		prgDialog.setMessage("正在获取数据中...");
		prgDialog.show();
	}

	public void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}
	
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
		bar.setTitle(getResources().getString(R.string.good_deed_show));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
		
		
		//在导航上添加上公告页面--ViewPager
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		
		
		Bundle args1 = new Bundle();
		Bundle args2 = new Bundle();
		Bundle args3 = new Bundle();
		
		args1.putInt("currentFragment", 0);
		args2.putInt("currentFragment", 1);
		args3.putInt("currentFragment", 2);
		
		mTabsAdapter.addTab(bar.newTab().setText("最新"), GoodDeedFragment.class, args1);
		mTabsAdapter.addTab(bar.newTab().setText("热门"), GoodDeedFragment.class, args2);
		mTabsAdapter.addTab(bar.newTab().setText("精华"), GoodDeedFragment.class, args3);
	}
	
	/**
	 * 菜单被分为如下三种，选项菜单（OptionsMenu）、上下文菜单（ContextMenu）和子菜单（SubMenu）
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		return true;
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
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.finish();
	}
	
	
	/**
	 * 菜单按钮选择
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEdit();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	

	public void onEdit() {
		Intent intent = new Intent(GoodDeedShowActivity.this, PostGoodORBadORShareActivity.class);
		intent.putExtra("type", DeedType.good);
		GoodDeedShowActivity.this.startActivity(intent);
	}
}