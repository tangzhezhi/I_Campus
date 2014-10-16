package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.NoticeType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.BaseResponse;
import com.stinfo.pushme.rest.entity.PostNoticeReq;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;

/**
 * 消息编辑框
 * @author lenovo
 *
 */
public class PostNoticeActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "PostNoticeActivity";

	private ProgressDialog prgDialog = null;
	private TextView tvClassName;
	private EditText etTitle;
	private EditText etContent;
	private EditText etSelectUser;
	private RelativeLayout layoutUserList;
	private TextView tvUserList;
	private ArrayList<UserInfo> mUserListSelect ;
	private String userIdSelect;
	
	private String receivetype="02";
	
	private int pos;
	
	private static final String[] m_virtousType = { "老师", "家长" };   //定义数组
	
	public String getReceivetype() {
		return receivetype;
	}

	public void setReceivetype(String receivetype) {
		this.receivetype = receivetype;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getUserIdSelect() {
		return userIdSelect;
	}

	public void setUserIdSelect(String userIdSelect) {
		this.userIdSelect = userIdSelect;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int i = 0;
		
		Intent intent1 = getIntent();
		
		Log.d(TAG, "获得Fragment位置参数：：："+intent1.getIntExtra("pos", 0));
		i = intent1.getIntExtra("pos", 0);
		pos = intent1.getIntExtra("pos", 0);
		
		if(i==0){
			setContentView(R.layout.activity_post_notice_school);
			initSchoolView();
		}
		else if(i==1){
			setContentView(R.layout.activity_post_notice_teacher);
			initTeacherView();
		}
		else if(i==2){
			setContentView(R.layout.activity_post_notice);
			initView();
			initClassName();
		}
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.post_notice));
		bar.setDisplayHomeAsUpEnabled(true);
	}

	private void initTeacherView() {
		layoutUserList =  (RelativeLayout)findViewById(R.id.layout_notice_teacher_user_list);
	    tvUserList = (TextView)findViewById(R.id.tv_notice_teacher_list);
	    etSelectUser  = (EditText) findViewById(R.id.et_teacher_notice_select);
		etTitle = (EditText) findViewById(R.id.et_teacher_notice_title);
		etContent = (EditText) findViewById(R.id.et_teacher_notice_content);
		
		PushUtils.dealEditText(etTitle, 30);
		
		PushUtils.dealEditText(etContent, 100);
		
		layoutUserList.setOnClickListener(this);
	}

	private void initSchoolView() {
		
		 final Spinner spinner =(Spinner)findViewById(R.id.sp_school_notice_receive_type);//下拉菜单的ID  
			ArrayAdapter<?> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_virtousType);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		    spinner.setAdapter(adapter);  
		    spinner.setSelection(0,true);
//		    spinner.performClick(); 
		    spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 

		    	@Override 
		    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
		    		 Log.d(TAG,"点击了："+ position+":::"+ spinner.getSelectedItem().toString());
		    		 receivetype = "0"+String.valueOf(position+2);
		    		 spinner.setSelection(position);
		    		 parent.setVisibility(View.VISIBLE);
		    	} 

		    	@Override 
		    	public void onNothingSelected(AdapterView<?> arg0) { 
		    		 	spinner.setSelection(0);
		    		} 
		    	}); 
		
		
		etTitle = (EditText) findViewById(R.id.et_school_notice_title);
		etContent = (EditText) findViewById(R.id.et_school_notice_content);
		
		PushUtils.dealEditText(etTitle, 30);
		
		PushUtils.dealEditText(etContent, 100);
		
		
	}


	private void initClassName() {
		UserCache userCache = UserCache.getInstance();
		
		 final Spinner spinner =(Spinner)findViewById(R.id.sp_class_notice_receive_type);//下拉菜单的ID  
			ArrayAdapter<?> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_virtousType);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		    spinner.setAdapter(adapter);  
		    spinner.setSelection(0,true);
//		    spinner.performClick(); 
		    spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 

		    	@Override 
		    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
		    		 Log.d(TAG,"点击了："+ position+":::"+ spinner.getSelectedItem().toString());
		    		 receivetype = "0"+String.valueOf(position+2);
		    		 spinner.setSelection(position);
		    		 parent.setVisibility(View.VISIBLE);
		    	} 

		    	@Override 
		    	public void onNothingSelected(AdapterView<?> arg0) { 
		    		 	spinner.setSelection(0);
		    		} 
		    	}); 
		
		
		tvClassName = (TextView) findViewById(R.id.tv_class_name);
		if (userCache.getUserInfo() instanceof Teacher) {
			tvClassName.setText(userCache.getCurrentClass().getClassName());
		}
		
	}

	private void initView() {
		etTitle = (EditText) findViewById(R.id.et_notice_title);
		etContent = (EditText) findViewById(R.id.et_notice_content);
		
		PushUtils.dealEditText(etTitle, 30);
		
		PushUtils.dealEditText(etContent, 100);
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
			onSubmit(pos);
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_notice_teacher_user_list:
			onSelectUser();
			break;
		}
	}
	
	
	private void onSelectUser() {
		Log.d(TAG, "进入onSelectUser");
		Intent intent = new Intent(this, UserSelectActivity.class);
		intent.putExtra("type", "teacherNotice");
		startActivityForResult(intent, 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		mUserListSelect = (ArrayList<UserInfo>) intent.getSerializableExtra("selectList");
		
		StringBuilder sbUserName = new StringBuilder();
		StringBuilder sbUserIds = new StringBuilder();
		for(UserInfo userInfo : mUserListSelect){
			sbUserName.append(userInfo.getUserName());
			sbUserName.append(",");
			sbUserIds.append(userInfo.getUservalue());
			sbUserIds.append(",");
		}
		
		if(sbUserIds.length() > 1){
			userIdSelect = sbUserIds.substring(0, sbUserIds.length()-1);
			etSelectUser.setText(sbUserName.substring(0, sbUserName.length()-1));
		}
		Log.d(TAG, "所选用户ID：：：：："+userIdSelect);
		
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}

	private void onSubmit(int pos) {
		UserCache userCache = UserCache.getInstance();
		
		if (etTitle.getText().length() < 1 || etContent.getText().length() < 1 ) {
			MessageBox.showLongMessage(PostNoticeActivity.this, "请填写公告标题和内容");
			return;
		}
		
		if (!(userCache.getUserInfo() instanceof Teacher)) {
			MessageBox.showLongMessage(PostNoticeActivity.this, "仅允许教师用户发布通知公告！");
			return;
		}
		
		PostNoticeReq reqData = new PostNoticeReq();
		reqData.setObjectId(userCache.getCurrentClass().getClassId());
		reqData.setSessionKey(userCache.getSessionKey());
		
		if(pos==2){
			reqData.setType(NoticeType.CLASS);
			reqData.setUserId(userCache.getUserInfo().getUserId());
			reqData.setReceivetype(this.receivetype);
			reqData.setReceiveobject(userCache.getCurrentClass().getClassId());
		}
		else if(pos==1){
			
			UserInfo userInfo  = userCache.getUserInfo() ;
			if((userCache.getUserInfo() instanceof Teacher)){
				Teacher teacher = (Teacher) userInfo;
				
				if(!teacher.getRolecode().contains("1")){
					MessageBox.showLongMessage(PostNoticeActivity.this, "仅允许行政老师发布教师公告！");
					return;
				}
			}
			
			reqData.setType(NoticeType.TEACHER);
			reqData.setUserId(userCache.getUserInfo().getUserId());
			reqData.setReceiveobject(userIdSelect);
			reqData.setReceivetype(UserType.TEACHER);
		}
		else if(pos==0){
			UserInfo userInfo  = userCache.getUserInfo() ;
			if((userCache.getUserInfo() instanceof Teacher)){
				Teacher teacher = (Teacher) userInfo;
				
				if(userCache.getCurrentClass().getClassName()==null || userCache.getCurrentClass().getClassName().equals("0")){
					MessageBox.showLongMessage(PostNoticeActivity.this, "行政老师请先切换一个班级");
					return;
				}
				
				if(!teacher.getRolecode().contains("1")){
					MessageBox.showLongMessage(PostNoticeActivity.this, "仅允许行政老师发布学校公告！");
					return;
				}
				
			}
			
			reqData.setUserId(userCache.getUserInfo().getUserId());
			reqData.setType(NoticeType.SCHOOL);
			reqData.setReceiveobject(userCache.getCurrentClass().getSchoolId());
			reqData.setReceivetype(this.getReceivetype());
		}
		else{
			Log.d(TAG, "不是合适的公告类型");
		}
		
		reqData.setTitle(etTitle.getText().toString());
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
						MessageBox.showServerError(PostNoticeActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
		

	}

	private void checkResponse(String response) {
		try {
			BaseResponse respData = new BaseResponse(response);
			if (respData.getAck() == Ack.SUCCESS) {
				
				etTitle.setText("");
				etContent.setText("");
				mUserListSelect = null;
				MessageBox.showMessage(PostNoticeActivity.this, "提交成功！");
				this.finish();
			} else {
				MessageBox.showAckError(PostNoticeActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(PostNoticeActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
}
