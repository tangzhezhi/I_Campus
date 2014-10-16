package com.stinfo.pushme;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * 自定义异常捕获--比如闪退、崩溃之类
 * @author lenovo
 *
 */
public class MyCustomExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "MyCustomExceptionHandler";

	private Thread.UncaughtExceptionHandler mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	private Context mContext;

	public MyCustomExceptionHandler(Context context) {
		mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG, "Enter uncaughtException!");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		int appVer = BaseApplication.getInstance().getAppVersion();
		ex.printStackTrace(pw);

		StringBuilder sb = new StringBuilder();
		sb.append("[AppVer]: " + appVer + "\n");
		sb.append("[OS]: Android " + Build.VERSION.SDK_INT + "\n");
		sb.append("[Model]: " + Build.MODEL + "\n");
		sb.append("[Exception]: " + sw.toString() + "\n\n");
		Log.e(TAG, "Exception message: " + sb.toString());

		// Report runtime exception to server
		reportErrorToServer(sb.toString());
		// Process.killProcess(Process.myPid());
		mDefaultHandler.uncaughtException(thread, ex);
	}

	private void reportErrorToServer(String errorMsg) {

	}
}