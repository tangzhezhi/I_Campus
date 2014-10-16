package com.stinfo.pushme.activity;

import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActivity;
import com.stinfo.pushme.common.AppConfig;
import com.stinfo.pushme.common.UserCache;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends BaseActivity {
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int SPLASH_DELAY_MILLIS = 1000;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		nextActivity();
	}

	private void nextActivity() {
		boolean firstRun = AppConfig.getInstance().getFirstRun();
		if (firstRun) {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}
		AppConfig.getInstance().setFirstRun(false);
	}

	private void goHome() {
		boolean logon = UserCache.getInstance().isLogon();
		if (logon) {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		} else {
			Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		}
	}

	private void goGuide() {
		Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}
}
