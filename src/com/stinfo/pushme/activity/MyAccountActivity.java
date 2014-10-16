package com.stinfo.pushme.activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Sex;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.AccountDBAdapter;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.URLChecker;

public class MyAccountActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "MyAccountActivity";
	private UserInfo mUserInfo = UserCache.getInstance().getUserInfo();
	private RelativeLayout layoutPhone;
	private RelativeLayout layout_myaccount_sex;
	private LinearLayout  layout_myaccount_name;
	private ImageView ivUserAvatar;
	private ImageView ivSex;
	private TextView tvUserName;
//	private TextView tvRemark;
	private TextView tvAccount;
	private TextView tvPhone;
	private TextView tvCurrentClassName;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.my_account));
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
		initData();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	private void initData() {
		UserInfo userInfo = null;
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			userInfo = dbAdapter.getUserInfoByUserId(mUserInfo.getUserId());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			dbAdapter.close();
		}
		
		
		tvUserName.setText(mUserInfo.getUserName());
		tvAccount.setText(mUserInfo.getUsercode());
		tvPhone.setText(userInfo.getPhone());
		tvCurrentClassName.setText(UserCache.getInstance().getCurrentClass().getClassName());
//		tvRemark.setText(UserHelper.getRemark(mUserInfo));

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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	private void initView() {
		layoutPhone = (RelativeLayout) findViewById(R.id.layout_myaccount_phone);
		layout_myaccount_sex =  (RelativeLayout) findViewById(R.id.layout_myaccount_sex);
		layout_myaccount_name =  (LinearLayout) findViewById(R.id.layout_myaccount_name);
		
		ivUserAvatar = (ImageView) findViewById(R.id.iv_myaccount_avatar);
		ivSex = (ImageView) findViewById(R.id.iv_myaccount_sex);
		tvUserName = (TextView) findViewById(R.id.tv_myaccount_name);
//		tvRemark = (TextView) findViewById(R.id.tv_myaccount_remark);
		tvAccount = (TextView) findViewById(R.id.tv_myaccount);
		tvPhone = (TextView) findViewById(R.id.tv_myaccount_phone);
		tvCurrentClassName = (TextView) findViewById(R.id.tv_current_classname);
		if (!(TextUtils.isEmpty(mUserInfo.getPhone()))) { 
			layoutPhone.setOnClickListener(this);
		}
		
		if (!(TextUtils.isEmpty(mUserInfo.getUserName()))) { 
			layout_myaccount_name.setOnClickListener(this);
		}
		layout_myaccount_sex.setOnClickListener(this);
		tvUserName.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_myaccount_sex:
			onClickSex();
			break;
		}
	}
	
	
	private void onClickSex(){
		final String[] sex = {"男","女"};
		Builder builder=new AlertDialog.Builder(MyAccountActivity.this);
	    builder.setTitle("请选择性别");
	    builder.setIcon(android.R.drawable.ic_dialog_info);
	    
	    builder.setSingleChoiceItems(sex, Integer.valueOf(mUserInfo.getSex()), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	mUserInfo.setSex(sex[which]);
            }
        });
	    
	    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	AccountDBAdapter adb = new AccountDBAdapter();
        		try {
        			adb.open();
        			int flag = adb.ModifyAccountSex(mUserInfo);
        			if(flag > 0){
        				
        				if (mUserInfo.getSex().equals(Sex.FEMALE) ) {
        					ivSex.setImageResource(R.drawable.ic_sex_female);
        				} else {
        					ivSex.setImageResource(R.drawable.ic_sex_male);
        				}
        				
        				MessageBox.showMessage(MyAccountActivity.this,"性别修改成功");
        			}
        			else{
        				MessageBox.showMessage(MyAccountActivity.this,"性别修改失败");
        			}
        		} catch (Exception e) {
        			Log.e("HomeworkDBAdapter", "Failed to operate database: " + e);
        		} finally {
        			adb.close();
        		}
            }
        });
	    
	    builder.setNegativeButton("取消", null);
	    AlertDialog dialog=builder.create();
	    dialog.show();
	}
	
}
