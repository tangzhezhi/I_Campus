package com.stinfo.pushme.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.TabsAdapter;
import com.stinfo.pushme.common.AppConstant.DeedType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.fragment.BadDeedFragment;
import com.stinfo.pushme.fragment.LoveShareFragment;
import com.stinfo.pushme.rest.RequestController;

/**
 * 爱心分享
 * @author wenle
 *
 */
public class LoveShareActivity extends BaseActionBarActivity {
	private static final String TAG = "LoveShareActivity";
	UserCache userCache = UserCache.getInstance();
	UserInfo userInfo = userCache.getUserInfo();
	private ProgressDialog prgDialog = null;
	
	//fragment页面装载适配器
	private TabsAdapter mTabsAdapter;
	//viewPager多页面滑动效果view
	private ViewPager mViewPager;
	

	public void showProgressDialog() {
		prgDialog = new ProgressDialog(LoveShareActivity.this);
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
		//手动创建viewPager,必须设置id
		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);
		
	
		/**
		 * 工具栏--导航tab模式
		 */
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.love_share));
		//将工具栏设置为导航模式
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
		
		//创建FragmentAdapter适配器，用来装载滑动fragment
		mTabsAdapter = new TabsAdapter(this,mViewPager);
		
		//设置当前页数属性,确定页面显示顺序
		Bundle args1 = new  Bundle();
		Bundle args2 = new  Bundle();
		Bundle args3 = new  Bundle();
		
		args1.putInt("currentFragment",0);
		args2.putInt("currentFragment",1);
		args3.putInt("currentFragment",2);
		
		mTabsAdapter.addTab(bar.newTab().setText("最新"),LoveShareFragment.class, args1);
		mTabsAdapter.addTab(bar.newTab().setText("热门"),LoveShareFragment.class, args2);
		mTabsAdapter.addTab(bar.newTab().setText("精华"),LoveShareFragment.class, args3);
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
		Intent intent = new Intent(LoveShareActivity.this, PostGoodORBadORShareActivity.class);
		intent.putExtra("type", DeedType.lover_share);
		LoveShareActivity.this.startActivity(intent);
	}
}