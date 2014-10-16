package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.UOptionsAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.SyncRoster;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.UserLoginReq;
import com.stinfo.pushme.rest.entity.UserLoginResp;
import com.stinfo.pushme.util.MessageBox;

/**
 * 登陆界面
 * @author lenovo
 *
 */
public class LoginActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "LoginActivity";
	private ProgressDialog prgDialog = null;
	private TextView tvForgotPwd;
	private EditText etUserId;
	private EditText etPassword;
	private Button btnLogin;
	private PopupWindow mPopupWindow;// 浮动窗口
	private LinearLayout parent;//浮动窗口依附布局
	private int pwidth;// 浮动宽口的宽度
    private UOptionsAdapter mOptionsAdapter;//适配器
    private ArrayList<String> mUserNames = new ArrayList<String>();// 保存用户名
    private Handler mHandler;// 处理消息更新UI
    
	/**
	 * Activity启动时从onCreate方法开始初始化：比如界面
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_2);
		//v7
		ActionBar bar = getSupportActionBar();
		//getResources 定位资源
		bar.setTitle(getResources().getString(R.string.login));
		initView();
	}

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在登录...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	private void initView() {
//		tvForgotPwd = (TextView) findViewById(R.id.tv_forgot_password);
		//下划线
//		tvForgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		etUserId = (EditText) findViewById(R.id.login_edit_userName);
		etPassword = (EditText) findViewById(R.id.login_edit_password);
        
		//寻找用户信息
		UserCache userCache = UserCache.getInstance();
		if (userCache.getUserInfo() != null) {
			etUserId.setText(userCache.getUserInfo().getUsercode());
		}

		btnLogin = (Button) findViewById(R.id.login_but_landing);
		btnLogin.setOnClickListener(this);
	}
	
	
	
	  /**
     * 在此方法中初始化可以获得输入框的宽度，以便于创建同样宽的浮动窗口
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 初始化UI
        initWedget();
        // 初始化浮动窗口
        initPopuWindow();
    }

    /**
     * 初始化UI控件
     */
    private void initWedget() {
        // 浮动窗口依附的布局
        this.parent = (LinearLayout) this.findViewById(R.id.username_layout);
        
		 // 获取地址输入框的宽度，用于创建浮动窗口的宽度
        int w = parent.getWidth();
        pwidth = w;
    }

    /**
     * 初始化浮动窗口
     */
    public void initPopuWindow() {
        // 浮动窗口的布局
        View loginwindow = (View) this.getLayoutInflater().inflate(
                R.layout.activity_pop_login, null);
        ListView listView = (ListView) loginwindow.findViewById(R.id.list);
        // 初始化适配器
        this.mOptionsAdapter = new UOptionsAdapter(LoginActivity.this,
                mUserNames, mHandler);
        listView.setAdapter(mOptionsAdapter);
        // 定义一个浮动窗口，并设置
        this.mPopupWindow = new PopupWindow(loginwindow, pwidth,
                LayoutParams.WRAP_CONTENT, true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

    }
	
	
	

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_but_landing:
			onLogin(v);
			break;
		}
	}
	

	private void onLogin(View v) {
		UserLoginReq reqData = new UserLoginReq();
		reqData.setUsercode(etUserId.getText().toString());
		reqData.setPassword(etPassword.getText().toString());

		showProgressDialog();
		UserCache.getInstance().setLogon(false);
		
		/**
		 * 封装为路径请求、回调得到响应结果
		 */
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						MessageBox.showServerError(LoginActivity.this);
					}
				});
		RequestController.getInstance().addToRequestQueue(req, TAG);
		
	}
	
	/**
	 * 解析返回的值
	 * @param response
	 */
	private void checkResponse(String response) {
		try {
			UserLoginResp respData = new UserLoginResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				//登陆成功的处理
				loginSuccess(respData);
			} else {
				closeProgressDialog();
				MessageBox.showAckError(LoginActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			closeProgressDialog();
			MessageBox.showParserError(LoginActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	/**
	 * 登陆成功的处理--转到主界面
	 * @param respData
	 */
	private void loginSuccess(UserLoginResp respData) {
		UserInfo userInfo = respData.getUserInfo();
		UserCache userCache = UserCache.getInstance();

		userCache.setSessionKey(respData.getSessionKey());
		// TBD: Encrypt password with MD5 or DES
		userCache.setPassword(etPassword.getText().toString());
		userCache.setUserInfo(userInfo);

		saveDefaultChild(userCache, userInfo);
		saveDefaultClass(userCache, userInfo);
		getDefaultTeacherRole(userCache,userInfo);
		/**
		 * 花名册同步成功后才会进去主界面
		 */
		SyncRoster syncRoster = new SyncRoster(new SyncRoster.OnTaskListener() {

			@Override
			public void onSuccess() {
				closeProgressDialog();
				UserCache userCache = UserCache.getInstance();
				userCache.setLogon(true);
				
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}

			@Override
			public void onFailure() {
				closeProgressDialog();
				MessageBox.showLongMessage(LoginActivity.this, "同步通讯录失败!");
			}
		});

		syncRoster.execSync();
	}

	private void saveDefaultChild(UserCache userCache, UserInfo userInfo) {
		if (userInfo instanceof Parent) {
			Parent parent = (Parent) userInfo;
			if (parent.getChildList().size() > 0) {
				Student child = parent.getChildList().get(0);
				userCache.setCurrentChild(child);
			}
		}
	}

	private void saveDefaultClass(UserCache userCache, UserInfo userInfo) {
		ClassInfo defaultClass = getDefaultClass(userInfo);
		userCache.setCurrentClass(defaultClass);
	}

	private ClassInfo getDefaultClass(UserInfo userInfo) {
		ClassInfo defaultClass = new ClassInfo();

		if (userInfo instanceof Student) {
			Student student = (Student) userInfo;
			defaultClass.setSchoolId(student.getSchoolId());
			defaultClass.setClassId(student.getClassId());
			defaultClass.setClassName(student.getClassName());

		} else if (userInfo instanceof Parent) {
			Parent parent = (Parent) userInfo;
			if (parent.getChildList().size() > 0) {
				defaultClass.setSchoolId(parent.getChildList().get(0).getSchoolId());
				defaultClass.setClassId(parent.getChildList().get(0).getClassId());
				defaultClass.setClassName(parent.getChildList().get(0).getClassName());
			}

		} else if (userInfo instanceof Teacher) {
			Teacher teacher = (Teacher) userInfo;
			defaultClass.setSchoolId(teacher.getSchoolId());
			if (teacher.getTeacherRoleList().size() > 0) {
				defaultClass.setClassId(teacher.getTeacherRoleList().get(0).getClassId());
				defaultClass.setClassName(teacher.getTeacherRoleList().get(0).getClassName());
			}
		}

		return defaultClass;
	}
	
	
	
	private TeacherRole getDefaultTeacherRole(UserCache userCache, UserInfo userInfo) {
		TeacherRole defaultTeacherRole = new TeacherRole();

		if (userInfo instanceof Teacher) {
			Teacher teacher = (Teacher) userInfo;
			if (teacher.getTeacherRoleList().size() > 0) {
				defaultTeacherRole.setClassId(teacher.getTeacherRoleList().get(0).getClassId());
				defaultTeacherRole.setClassName(teacher.getTeacherRoleList().get(0).getClassName());
				defaultTeacherRole.setClassmaster(teacher.getTeacherRoleList().get(0).getClassmaster());
			}
		}
		userCache.setCurrentTeacherRole(defaultTeacherRole);
		return defaultTeacherRole;
	}
	
	
	
	
}