package com.stinfo.pushme.activity;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.common.AppConstant.Sex;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.UserHelper;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.IntentHelper;
import com.stinfo.pushme.util.URLChecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserInfoActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "UserInfoActivity";
	private UserInfo mUserInfo = null;
	private RelativeLayout layoutPhone;
	private ImageView ivUserAvatar;
	private ImageView ivSex;
	private TextView tvUserName;
	private TextView tvRemark;
	private TextView tvAccount;
	private TextView tvPhone;
	private Button btnSendMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		mUserInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");

		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.userinfo));
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
		initData();
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

	private void initView() {
		layoutPhone = (RelativeLayout) findViewById(R.id.layout_phone);
		ivUserAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
		ivSex = (ImageView) findViewById(R.id.iv_sex);
		tvUserName = (TextView) findViewById(R.id.tv_user_name);
		tvRemark = (TextView) findViewById(R.id.tv_remark);
		tvAccount = (TextView) findViewById(R.id.tv_account);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		btnSendMsg = (Button) findViewById(R.id.btn_send_msg);

		if (!(TextUtils.isEmpty(mUserInfo.getPhone()))) { 
			layoutPhone.setOnClickListener(this);
		}
		btnSendMsg.setOnClickListener(this);
	}

	private void initData() {
		tvUserName.setText(mUserInfo.getUserName());
		
		UserInfo userInfo  = UserCache.getInstance().getUserInfo();
		
		if(userInfo.getUserId().equals(mUserInfo.getUserId()) ){
			tvAccount.setText(mUserInfo.getUsercode());
		}
		
		tvPhone.setText(mUserInfo.getPhone());
		tvRemark.setText(UserHelper.getRemark(mUserInfo));

		if (URLChecker.isUrl(mUserInfo.getPicUrl())) {
			Log.d(TAG, "loadImage: " + mUserInfo.getPicUrl());
			ImageListener listener = ImageLoader.getImageListener(ivUserAvatar,
					R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
			ImageCacheManager.getInstance().getImageLoader().get(mUserInfo.getPicUrl(), listener);
		} else {
			ivUserAvatar.setImageResource(R.drawable.avatar_default_normal);
		}

		if (mUserInfo.getSex().equals(Sex.FEMALE) ) {
			ivSex.setImageResource(R.drawable.ic_sex_female);
		} else {
			ivSex.setImageResource(R.drawable.ic_sex_male);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_phone:
			onCallPhone();
			break;
		case R.id.btn_send_msg:
			onSendMessage();
			break;
		}
	}

	private void onCallPhone() {
		IntentHelper.startDialActivity(this, mUserInfo.getPhone());
	}

	private void onSendMessage() {
		Intent intent = new Intent(this, ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("userInfo", mUserInfo);
		intent.putExtras(bundle);
		startActivity(intent);
		this.finish();
	}
}
