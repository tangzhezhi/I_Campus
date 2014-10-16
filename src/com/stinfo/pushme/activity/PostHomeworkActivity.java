package com.stinfo.pushme.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.BaseResponse;
import com.stinfo.pushme.rest.entity.PostHomeworkReq;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;

public class PostHomeworkActivity extends BaseActionBarActivity {
	private static final String TAG = "PostHomeworkActivity";

	private ProgressDialog prgDialog = null;
	private String mSubject = "";
	private String[] mItems;
	private String[] mItemsValue;
	private Spinner spSubject;
	private TextView tvClassName;
	private EditText etContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_homework);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.post_homework));
		bar.setDisplayHomeAsUpEnabled(true);
		
		mItems =  getIntent().getStringArrayExtra("coursename");
		mItemsValue = getIntent().getStringArrayExtra("coursecode");
		
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
		etContent = (EditText) findViewById(R.id.et_homework_content);
		spSubject = (Spinner) findViewById(R.id.sp_subject);
		
		PushUtils.dealEditText(etContent, 300);
		
//		mItems = getResources().getStringArray(R.array.subject_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSubject.setAdapter(adapter);
		spSubject.setOnItemSelectedListener(new SpinnerSelectedListener());
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			mSubject = mItemsValue[arg2];
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
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
		if (!(userCache.getUserInfo() instanceof Teacher)) {
			MessageBox.showLongMessage(PostHomeworkActivity.this, "仅允许教师用户发布家庭作业！");
			return;
		}

		Teacher teacher = (Teacher) userCache.getUserInfo();
		PostHomeworkReq reqData = new PostHomeworkReq();
		reqData.setUserId(teacher.getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setSubject(mSubject);
		reqData.setClassId(userCache.getCurrentClass().getClassId());
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
						MessageBox.showServerError(PostHomeworkActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			BaseResponse respData = new BaseResponse(response);
			if (respData.getAck() == Ack.SUCCESS) {
				MessageBox.showMessage(PostHomeworkActivity.this, "提交成功！");
				
				etContent.setText("");
				this.finish();
			} else {
				MessageBox.showAckError(PostHomeworkActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(PostHomeworkActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
}
