package com.stinfo.pushme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.FeedbackReq;
import com.stinfo.pushme.util.MessageBox;

public class FeedbackActivity extends BaseActionBarActivity implements OnClickListener  {
	private static final String TAG = "FeedbackActivity";
	private LayoutInflater mInflater;
	private EditText etContent;
	private Button btnSend;
	
	
	
	public EditText getEtContent() {
		return etContent;
	}

	public void setEtContent(EditText etContent) {
		this.etContent = etContent;
	}
	
	public Button getBtnSend() {
		return btnSend;
	}

	public void setBtnSend(Button btnSend) {
		this.btnSend = btnSend;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.feedback));
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	
	private void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		etContent = (EditText) findViewById(R.id.et_feedback_content);
		btnSend = (Button) findViewById(R.id.btn_feedback_send);
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_feedback_send:
			onSend();
			break;
		}
	}

	private void onSend() {
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			MessageBox.showLongMessage(this, "请输入消息！");
			return;
		}
		sendMessage(content);
		etContent.setText("");
	}

	private void sendMessage(String message) {
		UserCache userCache = UserCache.getInstance();
		FeedbackReq reqData = new FeedbackReq();

		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setContent(message);

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to FeedbackReq request!");
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
}
