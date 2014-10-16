package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.BaseResponse;
import com.stinfo.pushme.rest.entity.PostDailyReq;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;

public class PostDailyActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "PostDailyActivity";

	private ProgressDialog prgDialog = null;
	private TextView tvClassName;
	private EditText etContent;
	private RelativeLayout layoutUserList;
	private TextView tvUserList;
//	private HashMap<String, String> mSelectMap = null;
	private ArrayList<UserInfo> mUserListSelect ;
	private String userIdSelect;
	private String userChildUserValueSelect;
	private EditText etSelectUser;
	
	public String getUserIdSelect() {
		return userIdSelect;
	}

	public void setUserIdSelect(String userIdSelect) {
		this.userIdSelect = userIdSelect;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_daily);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.daily));
		bar.setDisplayHomeAsUpEnabled(true);

		initView();
		initClassName();
	}

	private void initClassName() {
		UserCache userCache = UserCache.getInstance();
		if (userCache.getUserInfo() instanceof Teacher) {
			tvClassName = (TextView) findViewById(R.id.tv_class_name);
			tvClassName.setText(userCache.getCurrentClass().getClassName());
		}
	}

	private void initView() {
		etContent = (EditText) findViewById(R.id.et_daily_content);
		tvUserList = (TextView) findViewById(R.id.tv_user_list);
		etSelectUser  = (EditText) findViewById(R.id.et_teacher_daily_select);
		layoutUserList = (RelativeLayout) findViewById(R.id.layout_user_list);
		
		PushUtils.dealEditText(etContent, 300);
		
		layoutUserList.setOnClickListener(this);
	}

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在提交...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.submit, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_submit:
			
			if(userIdSelect==null || userIdSelect.length() < 1){
				MessageBox.showLongMessage(PostDailyActivity.this, "请选择人员，评价后提交");
				break;
			}
			onSubmit();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}

	private void onSubmit() {
		UserCache userCache = UserCache.getInstance();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		if (!(userInfo instanceof Teacher) || !("1").equals(userCache.getCurrentTeacherRole().getClassmaster())) {
			MessageBox.showLongMessage(PostDailyActivity.this, "仅允许班主任发布日常表现！");
			return;
		}

		Teacher teacher = (Teacher) userCache.getUserInfo();
		PostDailyReq reqData = new PostDailyReq();
		reqData.setUserId(teacher.getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setParentids(userIdSelect);
		reqData.setStudentids(userChildUserValueSelect);
		reqData.setContent(etContent.getText().toString());

		showProgressDialog();
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						closeProgressDialog();
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						MessageBox.showServerError(PostDailyActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			BaseResponse respData = new BaseResponse(response);
			if (respData.getAck() == Ack.SUCCESS) {
				MessageBox.showMessage(PostDailyActivity.this, "提交成功！");
				
				this.etSelectUser.setText("");
				this.etContent.setText("");
				
				this.finish();
				
			} else {
				MessageBox.showAckError(PostDailyActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(PostDailyActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_user_list:
			onSelectUser();
			break;
		}
	}
	
	private void onSelectUser() {
		Intent intent = new Intent(this, UserSelectActivity.class);
		intent.putExtra("type", "daily");
		startActivityForResult(intent, 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		mUserListSelect = (ArrayList<UserInfo>) intent.getSerializableExtra("selectList");
		
		StringBuilder sbUserName = new StringBuilder();
		StringBuilder sbUserIds = new StringBuilder();
		
		StringBuilder sbUserChildUserValues = new StringBuilder();
		
		DBAdapter dbAdapter = new DBAdapter();
		dbAdapter.open();
		
		try {
			for(UserInfo userInfo : mUserListSelect){
				sbUserName.append(userInfo.getUserName());
				sbUserName.append(",");
				sbUserIds.append(userInfo.getUservalue());
				sbUserIds.append(",");
				
				ParentRoster pr = (ParentRoster)userInfo;
				String where = String.format("userId = '%s' ", pr.getChildUserId());
				
				List<StudentRoster> sr = dbAdapter.getStudentRosterByWhere(where);
				
				if(sr!=null && sr.size() > 0){
					sbUserChildUserValues.append(sr.get(0).getUservalue());
					sbUserChildUserValues.append(",");
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		
		
		if(sbUserIds.length() > 1){
			userIdSelect = sbUserIds.substring(0, sbUserIds.length()-1);
			etSelectUser.setText(sbUserName.substring(0, sbUserName.length()-1));
			userChildUserValueSelect = sbUserChildUserValues.substring(0, sbUserChildUserValues.length()-1);
		}
		Log.d(TAG, "所选用户ID：：：：："+userIdSelect);
		
		
	}
}
