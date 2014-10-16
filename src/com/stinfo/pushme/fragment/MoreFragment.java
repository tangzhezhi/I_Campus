package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.AboutActivity;
import com.stinfo.pushme.activity.FeedbackActivity;
import com.stinfo.pushme.activity.LoginActivity;
import com.stinfo.pushme.activity.MainActivity;
import com.stinfo.pushme.activity.MyAccountActivity;
import com.stinfo.pushme.activity.SettingActivity;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.SchoolLeader;
import com.stinfo.pushme.common.SyncRoster;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.LastAppVerReq;
import com.stinfo.pushme.rest.entity.LastAppVerResp;
import com.stinfo.pushme.rest.entity.SchoolClassReq;
import com.stinfo.pushme.rest.entity.SchoolClassResp;
import com.stinfo.pushme.rest.entity.UserLoginReq;
import com.stinfo.pushme.rest.entity.UserLoginResp;
import com.stinfo.pushme.util.MessageBox;

public class MoreFragment extends Fragment implements OnClickListener {
	private final static String TAG = "MoreFragment";
	private RelativeLayout layoutMyAccount;
	private RelativeLayout layoutChangeClass;
	private RelativeLayout layoutClearCache;
	private RelativeLayout layoutFeedback;
	private RelativeLayout layoutSetting;
	private RelativeLayout layoutCheckUpdate;
	private RelativeLayout layoutAbout;
	private RelativeLayout layoutChangeAccout;
	private String mAppUrl = "";
	private View mView;
	private ProgressDialog progDialog = null;
	private int selectedFruitIndex = 0; 
	private ArrayList<TeacherRole> trList = null;
	 
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
	
	public static MoreFragment newInstance() {
		MoreFragment newFragment = new MoreFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_more, container, false);
		initView();
		return mView;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		initView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	private void initView() {
		progDialog = new ProgressDialog(getActivity());
		layoutMyAccount = (RelativeLayout) mView.findViewById(R.id.layout_my_account);
//		layoutChangeClass = (RelativeLayout) mView.findViewById(R.id.layout_change_class);
		layoutClearCache = (RelativeLayout) mView.findViewById(R.id.layout_clear_cache);
//		layoutFeedback = (RelativeLayout) mView.findViewById(R.id.layout_feedback);
//		layoutSetting = (RelativeLayout) mView.findViewById(R.id.layout_setting);
		layoutCheckUpdate = (RelativeLayout) mView.findViewById(R.id.layout_check_update);
		layoutAbout = (RelativeLayout) mView.findViewById(R.id.layout_about);
		layoutChangeAccout = (RelativeLayout) mView.findViewById(R.id.layout_change_account);

		layoutMyAccount.setOnClickListener(this);
//		layoutChangeClass.setOnClickListener(this);
		layoutClearCache.setOnClickListener(this);
//		layoutFeedback.setOnClickListener(this);
//		layoutSetting.setOnClickListener(this);
		layoutCheckUpdate.setOnClickListener(this);
		layoutAbout.setOnClickListener(this);
		layoutChangeAccout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_my_account:
			onMyAccount();
			break;
//		case R.id.layout_change_class:
//			onChangeClass();
//			break;
		case R.id.layout_clear_cache:
			onClearCache();
			break;
//		case R.id.layout_feedback:
//			onFeedback();
//			break;
//		case R.id.layout_setting:
//			onSetting();
//			break;
		case R.id.layout_check_update:
			onCheckUpdate();
			break;
		case R.id.layout_about:
			onAbout();
			break;
		case R.id.layout_change_account:
			onChangeAccount();
			break;
		}
	}

	private void onMyAccount() {
		Intent intent = new Intent(getActivity(), MyAccountActivity.class);
		startActivity(intent);
	}

	private void onChangeClass() {
		
		final UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		if(userInfo instanceof Teacher){
			
			final Teacher teacher = (Teacher) userInfo;
			Log.d(TAG, "老师角色size::::"+teacher.getTeacherRoleList().size());
			
			
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
		        
		        
		        
		        Dialog alertDialog = new AlertDialog.Builder(getActivity()). 
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
		                				
		                				Intent intent = new Intent(getActivity(), MainActivity.class);
		                				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		                			}

		                			@Override
		                			public void onFailure() {
		                				dismissDialog();
		                				MessageBox.showLongMessage(getActivity(), "同步通讯录失败!");
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
			MessageBox.showMessage(getActivity(), "老师用户才能切换班级");
		}
		
		
	}

	private void onClearCache() {
		MessageBox.showMessage(getActivity(), "正在清理缓存数据..., 请稍等");
		DBAdapter dBAdapter = new DBAdapter();
		try {
			dBAdapter.open();
			dBAdapter.updateCache();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			dBAdapter.close();
			MessageBox.showMessage(getActivity(), "缓存数据清理完成");
		}
	}

	private void onFeedback() {
		Intent intent = new Intent(getActivity(), FeedbackActivity.class);
		startActivity(intent);
	}

	private void onSetting() {
		Intent intent = new Intent(getActivity(), SettingActivity.class);
		startActivity(intent);
	}

	private void onAbout() {
		Intent intent = new Intent(getActivity(), AboutActivity.class);
		startActivity(intent);
	}

	private void onChangeAccount() {
		MessageBox.showMessage(getActivity(), "正在清理缓存数据..., 请稍等");
		DBAdapter dBAdapter = new DBAdapter();
		try {
			dBAdapter.open();
			dBAdapter.changeUser();
			
			Context mContext = MyApplication.getInstance().getApplicationContext();
			SharedPreferences mSettings = mContext.getSharedPreferences("logon.pref", 0);
			SharedPreferences.Editor mEditor = mSettings.edit();
			mEditor.putBoolean("login", false);
			mEditor.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			dBAdapter.close();
		}
		
		UserCache.getInstance().setLogon(false);
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		getActivity().finish();
	}

	private void onCheckUpdate() {
		LastAppVerReq reqData = new LastAppVerReq();
		reqData.setAppName("pushme");
		reqData.setAppType(3);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(getActivity());
					}
				});

		MessageBox.showMessage(getActivity(), "检查中..., 请稍等");
		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			LastAppVerResp respData = new LastAppVerResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				checkVersion(respData);
			} else {
				MessageBox.showAckError(getActivity(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(getActivity());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void checkVersion(LastAppVerResp respData) {
		int localVersion = 0;
		try {
			localVersion = MyApplication.getInstance().getAppVersion();
		} catch (Exception ex) {
			Log.e(TAG, "Failed to get local package version! ", ex);
			return;
		}

		mAppUrl = respData.getAppUrl();
		Log.d(TAG, "localVersion: " + localVersion);
		Log.d(TAG, "appUrl: " + mAppUrl);
		Log.d(TAG, "serverVersion: " + respData.getVersionCode());
		Log.d(TAG, "serverVersionName: " + respData.getVersionName());
		Log.d(TAG, "updateTime: " + respData.getUpdateTime());
		String promptMsg = "发现新版本 V" + respData.getVersionName() + ", 建议立即更新使用";

		if (localVersion < respData.getVersionCode()) {
			new AlertDialog.Builder(getActivity()).setTitle("软件升级").setMessage(promptMsg)
					.setPositiveButton("更新", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updatePackage();
						}
					}).setNegativeButton("取消", null).show();
		} else {
			MessageBox.showMessage(getActivity(), "已是最新版本, 无需升级");
		}
	}

	private void updatePackage() {
		Intent updateIntent = new Intent("com.stinfo.pushme.UPDATE_SERVICE");
		updateIntent.putExtra("appName", getResources().getString(R.string.app_name));
		updateIntent.putExtra("appUrl", mAppUrl);
		getActivity().startService(updateIntent);
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
								MessageBox.showAckError(getActivity(), respData.getAck());
							}
						} catch (Exception e) {
							MessageBox.showParserError(getActivity());
							Log.e(TAG, "Failed to parser response data! \r\n" + e);
						}
						
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(getActivity());
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
        
        
        
        Dialog alertDialog = new AlertDialog.Builder(getActivity()). 
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
                				
                				Intent intent = new Intent(getActivity(), MainActivity.class);
                				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                			}

                			@Override
                			public void onFailure() {
                				dismissDialog();
                				MessageBox.showLongMessage(getActivity(), "同步通讯录失败!");
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
