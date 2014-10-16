package com.stinfo.pushme.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.MainActivity;
import com.stinfo.pushme.common.AppConstant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class UpdateService extends Service {

	private static final String TAG = "UpdateService";
	private static final String downloadDir = "app/download/";
	private final static int TIME_OUT = 20000;

	private static final int DOWNLOAD_COMPLETE = 1;
	private static final int DOWNLOAD_FAIL = 2;

	private String appName = "";
	private String appUrl = "";

	private File updateDir = null;
	private File updateFile = null;

	private NotificationManager manager = null;
	private Notification notification = null;

	private Intent updateIntent = null;
	private PendingIntent pendingIntent = null;

	private Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				String cmd = "chmod +x " + updateFile.getPath();
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					Log.d(TAG, "Failed to chmod apk file: " + e);
				}

				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
				pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);

				notification.icon = android.R.drawable.stat_sys_download_done;
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults = Notification.DEFAULT_SOUND;

				notification.contentIntent = pendingIntent;
				notification.contentView.setImageViewResource(R.id.img_icon,
						android.R.drawable.stat_sys_download_done);
				notification.contentView.setViewVisibility(R.id.layout_prgUpdate, View.GONE);
				notification.contentView.setTextViewText(R.id.tv_update, "下载完成，点击安装！");
				manager.notify(AppConstant.UPDATE_ID, notification);
				UpdateService.this.stopSelf();
				break;

			case DOWNLOAD_FAIL:
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults = Notification.DEFAULT_SOUND;
				notification.icon = android.R.drawable.stat_sys_warning;

				notification.contentView.setImageViewResource(R.id.img_icon,
						android.R.drawable.stat_sys_warning);
				notification.contentView.setViewVisibility(R.id.layout_prgUpdate, View.GONE);
				notification.contentView.setTextViewText(R.id.tv_update, "下载失败!");
				manager.notify(AppConstant.UPDATE_ID, notification);
				UpdateService.this.stopSelf();
				break;
			}
		}
	};

	private void startUpdate(Intent intent) {
		appName = intent.getStringExtra("appName");
		appUrl = intent.getStringExtra("appUrl");

		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(), downloadDir);
		} else {
			updateDir = getFilesDir();
		}
		updateFile = new File(updateDir.getPath(), appName + ".apk");

		Log.d(TAG, "In startUpdate");
		Log.d(TAG, "updateFile: " + updateFile.getPath());
		Log.d(TAG, "appUrl: " + appUrl);

		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = android.R.drawable.stat_sys_download;
		notification.tickerText = "正在下载";
		notification.when = System.currentTimeMillis();

		updateIntent = new Intent(this, MainActivity.class);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		notification.contentIntent = pendingIntent;
		notification.contentView = new RemoteViews(getPackageName(), R.layout.notification_update);
		notification.contentView.setImageViewResource(R.id.img_icon, android.R.drawable.stat_sys_download);
		notification.contentView.setProgressBar(R.id.prg_update, 100, 0, false);
		notification.contentView.setTextViewText(R.id.tv_update, "0%");
		manager.notify(AppConstant.UPDATE_ID, notification);

		new Thread(new updateRunnable()).start();
	}

	private final class updateRunnable implements Runnable {
		private Message message = updateHandler.obtainMessage();

		public void run() {
			try {
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}

				long downloadSize = downloadUpdateFile(appUrl, updateFile);
				if (downloadSize > 0) {
					message.what = DOWNLOAD_COMPLETE;
					updateHandler.sendMessage(message);
				}
			} catch (Exception e) {
				Log.d(TAG, "Failed to download apk file: " + e);
				message.what = DOWNLOAD_FAIL;
				updateHandler.sendMessage(message);
			}
		}

		public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
			int downloadCount = 0;
			int currentSize = 0;
			long totalSize = 0;
			int updateTotalSize = 0;

			HttpURLConnection httpConnection = null;
			InputStream is = null;
			FileOutputStream fos = null;

			try {
				URL url = new URL(downloadUrl);
				httpConnection = (HttpURLConnection) url.openConnection();
				if (currentSize > 0) {
					httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
				}

				httpConnection.setConnectTimeout(TIME_OUT);
				httpConnection.setReadTimeout(TIME_OUT);

				updateTotalSize = httpConnection.getContentLength();
				if (httpConnection.getResponseCode() == 404) {
					throw new Exception("Failed to download!");
				}

				is = httpConnection.getInputStream();
				fos = new FileOutputStream(saveFile, false);
				byte buffer[] = new byte[4096];
				int readsize = 0;

				while ((readsize = is.read(buffer)) > 0) {
					fos.write(buffer, 0, readsize);
					totalSize += readsize;

					if ((downloadCount == 0)
							|| (int) ((totalSize * 100 / updateTotalSize) - 10) > downloadCount) {
						downloadCount += 10;

						notification.contentView.setProgressBar(R.id.prg_update, 100,
								(int) (totalSize * 100 / updateTotalSize), false);
						notification.contentView.setTextViewText(R.id.tv_update,
								(int) (totalSize * 100 / updateTotalSize) + "%");
						manager.notify(AppConstant.UPDATE_ID, notification);
					}
				}
			} finally {
				if (httpConnection != null) {
					httpConnection.disconnect();
				}
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			}

			return totalSize;
		}
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");

		startUpdate(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "onBind");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}
}