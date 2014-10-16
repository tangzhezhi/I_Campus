package com.stinfo.pushme.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.MainActivity.RefreshUnreadReceiver;
import com.stinfo.pushme.adapter.TabsAdapter;
import com.stinfo.pushme.common.AppConstant.SchoolLeader;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.fragment.NoticeClassFragment;
import com.stinfo.pushme.fragment.NoticeSchoolFragment;
import com.stinfo.pushme.fragment.NoticeTeacherFragment;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.view.BadgeView;

/**
 * 通知公告--ActionBar 持有ViewPager进行展示
 * @author lenovo
 *
 */
public class NoticeActivity extends BaseActionBarActivity {
	private static final String TAG = "NoticeActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	UserCache userCache = UserCache.getInstance();
	UserInfo userInfo = userCache.getUserInfo();
	private ProgressDialog prgDialog = null;
	
	LayoutInflater mInflater = null;
	//学校公告数字标签view
	View convertSchoolView = null;
	//教师公告数字标签view
	View convertTeacherView = null;
	//班级公告数字标签view
	View convertClassView = null;
	
	//公告信息未读广播
	private NoticeUnreadReceiver mReceiver = null;
	//学校公告数字标签
	BadgeView badgeSchool = null;
	//教师公告数字标签
	BadgeView badgeTeacher = null;
	//班级公告数字标签
	BadgeView badgeClass = null;
	
	public void showProgressDialog() {
		prgDialog = new ProgressDialog(NoticeActivity.this);
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
		if(userInfo instanceof Teacher){
			System.out.println("设置了缓存界面数...");
			mViewPager.setOffscreenPageLimit(2);   //缓存界面数
		}
		setContentView(mViewPager);
		
		//广播初始化
		registerMyReceiver();
		/**
		 * 工具栏--导航tab模式
		 */
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.notice));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
		
		
		
		//在导航上添加上公告页面--ViewPager
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertSchoolView = mInflater.inflate(R.layout.layout_notice_bartitle, null);
		TextView tvSchoolTitle = (TextView) convertSchoolView.findViewById(R.id.tv_bageview);
		tvSchoolTitle.setText("学校公告");
		
		badgeSchool = new BadgeView(this,tvSchoolTitle);
		badgeSchool.setBadgeMargin(0);
		mTabsAdapter.addTab(bar.newTab().setCustomView(convertSchoolView), NoticeSchoolFragment.class, null);
		
		if(userInfo instanceof Teacher){
			convertTeacherView = mInflater.inflate(R.layout.layout_notice_teacher_bartitle, null);
			TextView tvTeacherTitle = (TextView) convertTeacherView.findViewById(R.id.tv_teacher_bageview);
			tvTeacherTitle.setText("教师公告");
			badgeTeacher = new BadgeView(this,tvTeacherTitle);
			badgeTeacher.setBadgeMargin(0);
			mTabsAdapter.addTab(bar.newTab().setCustomView(convertTeacherView), NoticeTeacherFragment.class, null);
		}
		
		convertClassView = mInflater.inflate(R.layout.layout_notice_class_bartitle, null);
		TextView tvClassTitle = (TextView) convertClassView.findViewById(R.id.tv_class_bageview);
		tvClassTitle.setText("班级公告");
		badgeClass = new BadgeView(this,tvClassTitle);
		badgeClass.setBadgeMargin(0);
		mTabsAdapter.addTab(bar.newTab().setCustomView(convertClassView), NoticeClassFragment.class, null);
	}
	
	/**
	 * 菜单被分为如下三种，选项菜单（OptionsMenu）、上下文菜单（ContextMenu）和子菜单（SubMenu）
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.edit, menu);
//		
//		UserInfo userInfo = UserCache.getInstance().getUserInfo();
//		MenuItem editNotice = menu.findItem(R.id.action_edit);
//		
//		if (mViewPager.getCurrentItem()!=2&&userInfo.getSchoolLeader().equals(SchoolLeader.yes)) {
//			editNotice.setVisible(true);
//		}
//		else{
//			editNotice.setVisible(false);
//		}
		return false;
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
	 * 菜单按钮选择
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditNotice();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	

	public void onEditNotice() {
		if(mViewPager!=null){
			Log.d(TAG, "当前Fragment是: " + mViewPager.getCurrentItem());
			
			if(mViewPager.getCurrentItem()==2){
				UserInfo userInfo = UserCache.getInstance().getUserInfo();
				
				if(userInfo.getSchoolLeader().equals(SchoolLeader.yes) && UserCache.getInstance().getCurrentClass().getClassName().equals("")){
					MessageBox.showMessage(NoticeActivity.this, "行政老师请选择一个班级后操作");
					return ;
				}
			}
			
			Intent intent = new Intent(NoticeActivity.this, PostNoticeActivity.class);
			intent.putExtra("pos", mViewPager.getCurrentItem());
			NoticeActivity.this.startActivity(intent);
		}
	}
	
	//广播注册
	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new NoticeUnreadReceiver();
			IntentFilter intentFilter = new IntentFilter();
			//广播频率
			intentFilter.addAction(PushUtils.ACTION_PUSH_OTHER_MESSAGE);
			//activity和fragment之间广播通信,必须LocalBroadcastManager.getInstance(this)
			LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
		}
	}
	
	//接收未读数据
	final class NoticeUnreadReceiver extends BroadcastReceiver {
		
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			int unreadSchoolCount = intent.getIntExtra("schoolNoticeMsg", 0);
			int unreadTeacherCount = intent.getIntExtra("teacherNoticeMsg", 0);
			int unreadClassCount = intent.getIntExtra("classNoticeMsg", 0);
			if ( badgeSchool != null) {
				if (unreadSchoolCount > 0) {
					badgeSchool.setText(String.valueOf(unreadSchoolCount));
					badgeSchool.show();
				} else {
					badgeSchool.hide();
				}
			}
			
			if ( badgeTeacher != null) {
				if (unreadTeacherCount > 0) {
					badgeTeacher.setText(String.valueOf(unreadTeacherCount));
					badgeTeacher.show();
				} else {
					badgeTeacher.hide();
				}
			}
			
			if ( badgeClass != null) {
				
				if (unreadClassCount > 0) {
					badgeClass.setText(String.valueOf(unreadClassCount));
					badgeClass.show();
				} else {
					badgeClass.hide();
				}
			}
		}
	}
}