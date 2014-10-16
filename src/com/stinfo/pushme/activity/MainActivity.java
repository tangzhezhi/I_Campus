package com.stinfo.pushme.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.CommonPagerAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.SchoolLeader;
import com.stinfo.pushme.common.SyncRoster;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.fragment.ContactFragment;
import com.stinfo.pushme.fragment.IndexGridFragment;
import com.stinfo.pushme.fragment.MessageFragment;
import com.stinfo.pushme.fragment.MoreFragment;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PushAccountReq;
import com.stinfo.pushme.rest.entity.SchoolClassReq;
import com.stinfo.pushme.rest.entity.SchoolClassResp;
import com.stinfo.pushme.service.ForegroundService;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.view.BadgeView;
/**
 * 主界面：此时只是将控件初始化、填充，默认展示mIndexView的界面元素
 * @author lenovo
 *
 */
public class MainActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private static final String TAG = "MainActivity";
	private ArrayList<Fragment> mFragmentsList;
	private RefreshUnreadReceiver mReceiver = null;
	private CommonPagerAdapter mAdapter;
	//ViewPager应该和Fragment一起使用:.viewpager控件的初始化，获取Fragment对象并保存在容器中，设置viewpager的适配器和监听
	private ViewPager mViewPager;

	private LinearLayout mIndexView = null;
	private LinearLayout mMessageView = null;
	private LinearLayout mContactView = null;
	private LinearLayout mMoreView = null;
	private TextView mMessageItem = null;
	private BadgeView mBadge = null;
	Message m = null;
	private ArrayList<TeacherRole> trList = null;
	private int selectedFruitIndex = 0; 
	private ProgressDialog progDialog = null;
	private Menu mMenu = null;
	
	/**
	 * 初始化界面、注册广播、初始化TabWidget控件、Fragment填充
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progDialog = new ProgressDialog(this);
		
		initPushService();
		initTabWidget();
		initPagerView();
		handleIntent(getIntent());
		registerMyReceiver();
		syncUpdateRoster() ;
	}

	private void syncUpdateRoster() {
		
		showDialog("正在同步通讯录数据，请稍等");
		
		/**
		 * 花名册同步成功后才会进去主界面
		 */
		SyncRoster syncRoster = new SyncRoster(new SyncRoster.OnTaskListener() {

			@Override
			public void onSuccess() {
				dismissDialog();
				UserCache userCache = UserCache.getInstance();
				userCache.setLogon(true);
			}

			@Override
			public void onFailure() {
				dismissDialog();
				MessageBox.showLongMessage(MainActivity.this, "同步通讯录失败!");
			}
		});

		syncRoster.execSync();
		
	}

	private void initPushService() {
		
	   Log.d(TAG,  "api_key:"+PushUtils.getMetaValue(MainActivity.this, "api_key"));
		
	   PushManager.startWork(getApplicationContext(),
	           PushConstants.LOGIN_TYPE_API_KEY,
	           PushUtils.getMetaValue(MainActivity.this, "api_key"));
		
		Intent foregroundService = new Intent(MainActivity.this,ForegroundService.class);
		startService(foregroundService);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		UserCache usercache = UserCache.getInstance();
		UserInfo userInfo = usercache.getUserInfo();
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem mitem = menu.findItem(R.id.action_class_name);
		MenuItem mitemExit = menu.findItem(R.id.action_exit);
		mitemExit.setIcon(R.drawable.exit);
		
		String teacherRole = "";
		
		if(usercache.getCurrentTeacherRole()!=null){
			if(usercache.getCurrentTeacherRole().getClassmaster()!=null && usercache.getCurrentTeacherRole().getClassmaster().equals("1")){
				teacherRole = "/班主任";
			}
		}
		mitem.setTitle(userInfo.getUserName()+teacherRole+"/"+usercache.getCurrentClass().getClassName());
		mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			this.finish();
			break;
		case R.id.action_class_name:
			this.changeClass();
			break;
		}
		return true;
	}
	
	
	/**
	 * 显示进度条对话框
	 */
	public void showDialog(String msg) {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage(msg);
		progDialog.show();
	}
	
	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
	/**
	 * 切换班级
	 */
	private void changeClass() {
	final UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		if(userInfo instanceof Teacher){
			
			final Teacher teacher = (Teacher) userInfo;
			
			if(teacher.getSchoolLeader().equals(SchoolLeader.yes)){
				getSchoolClass(teacher);
			}
			
			else{
				ArrayList<String> classname = new ArrayList<String> ();
				ArrayList<String> classid = new ArrayList<String> ();
				
				
				for(TeacherRole cs : teacher.getTeacherRoleList()){
					classname.add(cs.getClassName());
					classid.add(cs.getClassId());
				}
				
		        final String[] classnamees = classname.toArray(new String[0]); 
		        final String[] classides = classid.toArray(new String[0]); 
		        
		        
		        String tempClassid = UserCache.getInstance().getCurrentClass().getClassId();
		        
		        if(classides!=null && classides.length > 0 ){
		        	for(int q = 0 ; q < classides.length ; q++){
		        		if(tempClassid.equals(classides[q])){
		        			selectedFruitIndex = q;
		        		}
		        	}
		        }
		        
		        Dialog alertDialog = new AlertDialog.Builder(this). 
		                setTitle("更换班级"). 
		                setSingleChoiceItems(classnamees, selectedFruitIndex, new DialogInterface.OnClickListener() { 
		  
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                    	Log.d(TAG, "选择的classId"+which);
		                    	selectedFruitIndex = which;
		                    } 
		                }). 
		                setPositiveButton("确认", new DialogInterface.OnClickListener() { 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) {
		                    	showDialog("正在更换班级数据，请稍等");
		                    	String selectclassid = classides[selectedFruitIndex];
		                    	
		                    	Log.d(TAG, "选择的classId"+selectclassid);
		                    	
		                    	ClassInfo classInfo = new ClassInfo();
		                    	classInfo.setClassId(selectclassid);
		                    	classInfo.setClassName(classnamees[selectedFruitIndex]);
		                    	classInfo.setSchoolId(((Teacher) userInfo).getSchoolId());
		                    	UserCache userCache = UserCache.getInstance();
		                    	userCache.setCurrentClass(classInfo);
		                    	TeacherRole trole = teacher.getTeacherRoleList().get(selectedFruitIndex);
		                    	userCache.setCurrentTeacherRole(trole);
		                    	
		                    	/**
		                		 * 花名册同步成功后才会进去主界面
		                		 */
		                		SyncRoster syncRoster = new SyncRoster(new SyncRoster.OnTaskListener() {

		                			@Override
		                			public void onSuccess() {
		                				dismissDialog();
		                				UserCache userCache = UserCache.getInstance();
		                				userCache.setLogon(true);
		                				
		                				userCache.updateClassGroup();
		                				
		                				UserInfo userInfo = userCache.getUserInfo();
		                				
		                				String teacherRole = "";
		                				if(userCache.getCurrentTeacherRole().getClassmaster().equals("1")){
		                					teacherRole = "/班主任";
		                				}
		                				
		                				if(mMenu!=null){
			                				MenuItem mitem = mMenu.findItem(R.id.action_class_name);
			                				
			                				
			                				mitem.setTitle(userInfo.getUserName()+teacherRole+"/"+userCache.getCurrentClass().getClassName());
		                				}
		                				
		                				MessageBox.showMessage(MainActivity.this, "切换班级成功");
		                				//跳转至首页
		                				initPagerView();
		                				
		                			}

		                			@Override
		                			public void onFailure() {
		                				dismissDialog();
		                				MessageBox.showLongMessage(MainActivity.this, "同步通讯录失败!");
		                			}
		                		});

		                		syncRoster.execSync();
		                    	
		                    } 
		                }). 
		                setNegativeButton("取消", new DialogInterface.OnClickListener() { 
		 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                    	
		                    } 
		                }). 
		                create(); 
		        	alertDialog.show(); 
			}
			

		}
		else{
			MessageBox.showMessage(this, "老师用户才能切换班级");
		}
	}
	
	
	/**
	 * 获取学校班级
	 */
	private void getSchoolClass(final Teacher teacher){
		String schoolno = UserCache.getInstance().getCurrentClass().getSchoolId();
		String sessionkey = UserCache.getInstance().getSessionKey();
		SchoolClassReq reqData = new SchoolClassReq();
		reqData.setSchoolno(schoolno);
		reqData.setSessionKey(sessionkey);
		
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						
						try {
							SchoolClassResp respData = new SchoolClassResp(response);
							if (respData.getAck() == Ack.SUCCESS) {
								//登陆成功的处理
								trList = respData.getTeacherRoleList();
								teacher.setTeacherRoleList(trList);
								changeClassWhenSchoolLeader(teacher);
								
							} else {
								MessageBox.showAckError(MainActivity.this, respData.getAck());
							}
						} catch (Exception e) {
							MessageBox.showParserError(MainActivity.this);
							Log.e(TAG, "Failed to parser response data! \r\n" + e);
						}
						
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MainActivity.this);
					}
				});
		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void changeClassWhenSchoolLeader(final Teacher teacher){
		final UserInfo userInfo = UserCache.getInstance().getUserInfo();
		ArrayList<String> classname = new ArrayList<String> ();
		ArrayList<String> classid = new ArrayList<String> ();
		
		
		for(TeacherRole cs : teacher.getTeacherRoleList()){
			classname.add(cs.getClassName());
			classid.add(cs.getClassId());
		}
		
        final String[] classnamees = classname.toArray(new String[0]); 
        final String[] classides = classid.toArray(new String[0]); 
        
        
        
        Dialog alertDialog = new AlertDialog.Builder(this). 
                setTitle("更换班级"). 
                setSingleChoiceItems(classnamees, 0, new DialogInterface.OnClickListener() { 
  
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                    	Log.d(TAG, "选择的classId"+which);
                    	selectedFruitIndex = which;
                    } 
                }). 
                setPositiveButton("确认", new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) {
                    	showDialog("正在更换班级数据，请稍等");
                    	String selectclassid = classides[selectedFruitIndex];
                    	
                    	Log.d(TAG, "选择的classId"+selectclassid);
                    	
                    	ClassInfo classInfo = new ClassInfo();
                    	classInfo.setClassId(selectclassid);
                    	classInfo.setClassName(classnamees[selectedFruitIndex]);
                    	classInfo.setSchoolId(((Teacher) userInfo).getSchoolId());
                    	UserCache userCache = UserCache.getInstance();
                    	userCache.setCurrentClass(classInfo);
                    	TeacherRole trole = teacher.getTeacherRoleList().get(selectedFruitIndex);
                    	userCache.setCurrentTeacherRole(trole);
                    	
                    	/**
                		 * 花名册同步成功后才会进去主界面
                		 */
                		SyncRoster syncRoster = new SyncRoster(new SyncRoster.OnTaskListener() {

                			@Override
                			public void onSuccess() {
                				dismissDialog();
                				UserCache userCache = UserCache.getInstance();
                				userCache.setLogon(true);
                			}

                			@Override
                			public void onFailure() {
                				dismissDialog();
                				MessageBox.showLongMessage(MainActivity.this, "同步通讯录失败!");
                			}
                		});

                		syncRoster.execSync();
                    	
                    } 
                }). 
                setNegativeButton("取消", new DialogInterface.OnClickListener() { 
 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                    	
                    } 
                }). 
                create(); 
        	alertDialog.show(); 
	}
	
	
	

	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new RefreshUnreadReceiver();
			IntentFilter intentFilter = new IntentFilter();
//			intentFilter.addAction(PushUtils.ACTION_REFRESH_UNREAD);
			intentFilter.addAction(PushUtils.ACTION_INTERNAL_MESSAGE);
			registerReceiver(mReceiver, intentFilter);
		}
	}
	
	/**
	 * 初始化下面四个Tab
	 */
	private void initTabWidget() {
		mIndexView = (LinearLayout) findViewById(R.id.bottombar_index);
		mMessageView = (LinearLayout) findViewById(R.id.bottombar_message);
		mContactView = (LinearLayout) findViewById(R.id.bottombar_contact);
		mMoreView = (LinearLayout) findViewById(R.id.bottombar_more);
		
		mMessageItem = (TextView) findViewById(R.id.tab_item_message);
		mBadge = new BadgeView(this, mMessageItem);
		mBadge.setBadgeMargin(0);

		mIndexView.setOnClickListener(new TabOnClickListener(0));
		mMessageView.setOnClickListener(new TabOnClickListener(1));
		mContactView.setOnClickListener(new TabOnClickListener(2));
		mMoreView.setOnClickListener(new TabOnClickListener(3));
	}
	
	/**
	 * 初始化ViewPaper：填充Fragment
	 */
	private void initPagerView() {
		//ViewPager在这里哦--画面动起来
		mViewPager = (ViewPager) findViewById(R.id.vp_container);
		mFragmentsList = new ArrayList<Fragment>();

		Fragment indexFrag = IndexGridFragment.newInstance();
		Fragment messageFrag = MessageFragment.newInstance();
		Fragment contactFrag = ContactFragment.newInstance();
		Fragment moreFrag = MoreFragment.newInstance();

		mFragmentsList.add(indexFrag);
		mFragmentsList.add(messageFrag);
		mFragmentsList.add(contactFrag);
		mFragmentsList.add(moreFrag);

		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		setCurrentPage(0);
	}

	final class TabOnClickListener implements OnClickListener {
		private int mIndex = 0;

		public TabOnClickListener(int index) {
			mIndex = index;
		}

		@Override
		public void onClick(View v) {
			setCurrentPage(mIndex);
		}
	}
	
	/**
	 * 通过这里将界面串起来
	 * @param index
	 */
	private void setCurrentPage(int index) {
		mViewPager.setCurrentItem(index);
		switch (index) {
		case 0:
			selectIndex();
			break;
		case 1:
			selectMessage();
			break;
		case 2:
			selectContact();
			break;
		case 3:
			selectMore();
			break;
		}
	}

	private void selectIndex() {
		mIndexView.setSelected(true);
		mMessageView.setSelected(false);
		mContactView.setSelected(false);
		mMoreView.setSelected(false);
	}

	private void selectMessage() {
		mIndexView.setSelected(false);
		mMessageView.setSelected(true);
		mContactView.setSelected(false);
		mMoreView.setSelected(false);
	}

	private void selectContact() {
		mIndexView.setSelected(false);
		mMessageView.setSelected(false);
		mContactView.setSelected(true);
		mMoreView.setSelected(false);
	}

	private void selectMore() {
		mIndexView.setSelected(false);
		mMessageView.setSelected(false);
		mContactView.setSelected(false);
		mMoreView.setSelected(true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentPage(arg0);
	}


	private void handleIntent(Intent intent) {
		 String pushUserId = "";
		 String pushChannelId = "";
		
		String action = intent.getAction();
		if (PushUtils.ACTION_BIND_SUCCESS.equals(action)) {
			 pushUserId = intent.getStringExtra(PushUtils.PUSH_USERID);
			 pushChannelId =  intent.getStringExtra(PushUtils.PUSH_CHANNELID);
			onBindSuccess(pushUserId, pushChannelId);
		} else if (PushUtils.ACTION_NOTIFICATION_ENTRY.equals(action)) {
			// 重置通知栏新消息数
			MyApplication.getInstance().clearNewsCount();
		}
	}

	private void onBindSuccess(String pushUserId, String pushChannelId) {

		UserCache userCache = UserCache.getInstance();
		PushAccountReq reqData = new PushAccountReq();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setPushUserId(pushUserId);
		reqData.setPushChannelId(pushChannelId);
		reqData.setDeviceType(1);

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						setTags(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void setTags(String response) {
		Log.d(TAG, "账号绑定："+response);
	}
	
	
	
	final class RefreshUnreadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			int unreadCount = intent.getIntExtra(PushUtils.UNREAD_COUNT, 0);

			if (mBadge != null) {
				if (unreadCount > 0) {
					mBadge.setText(String.valueOf(unreadCount));
					mBadge.show();
				} else {
					mBadge.hide();
				}
			}
		}
	}
}