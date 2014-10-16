package com.stinfo.pushme;

import com.baidu.frontia.FrontiaApplication;


public class MyApplication extends BaseApplication {
	private static MyApplication mInstance = null;
	private int newsCount = 0;
	
	public int getNewsCount() {
		return newsCount;
	}

	public void plusNewsCount() {
		newsCount++;
	}
	
	public void clearNewsCount() {
		newsCount = 0;
	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		FrontiaApplication.initFrontiaApplication(mInstance) ;
	}
}
