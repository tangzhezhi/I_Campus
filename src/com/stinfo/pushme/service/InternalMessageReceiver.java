package com.stinfo.pushme.service;

import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.MainActivity;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.PushUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 该类为内部消息有序广播接收器优先级最低的，如果用户正在与发送消息的用户聊天，该接收器将不被调用！
 */
public class InternalMessageReceiver extends BroadcastReceiver {
	private static final String TAG = "InternalMessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, ">>> Receive intent: \r\n" + intent);
		refreshRecentMessage(context);
		
		String msgtype = "";
		String msgcontent = "";
		
		if(intent!=null && intent.getExtras()!=null){
			Bundle bundle = new Bundle();
			bundle = intent.getExtras();
			
			if(bundle.get(PushUtils.MESSAGE) instanceof Message){
				Message msg = (Message) bundle.get(PushUtils.MESSAGE) ;
				
				if(msg.getMsgType()==2){
					msgtype = "公告";
				}
				else if(msg.getMsgType()==3){
					msgtype = "家庭作业消息";
				}
				else if(msg.getMsgType()==4){
					msgtype = "日常表现消息";
				}
				else if(msg.getMsgType()==1){
					msgtype = "消息";
				}
				
				if(msg.getContent()!=null && msg.getContent().length() > 15){
					msgcontent = msg.getContent().substring(0, 15);
				}
				else{
					msgcontent = msg.getContent();
				}
				
			}
		}
		
		
//		MyApplication.getInstance().plusNewsCount();
//		String content = "您有" + MyApplication.getInstance().getNewsCount() + "条新"+msgtype+"请查看";
		String content = "您有新"+msgtype+":\r\r"+msgcontent+"...,\r\r请查看";

		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.notification_icon;
		notification.tickerText = "收到新"+msgtype+","+msgcontent+"...";
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;

		Intent contentIntent = new Intent(context, MainActivity.class);
		contentIntent.setAction(PushUtils.ACTION_NOTIFICATION_ENTRY);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

		notification.contentIntent = pendingIntent;
		notification.contentView = new RemoteViews(context.getPackageName(),
				R.layout.notification_custom_builder);
		notification.contentView.setImageViewResource(R.id.notification_icon, R.drawable.icon);
		notification.contentView.setTextViewText(R.id.notification_title, "掌上校园");
		notification.contentView.setTextViewText(R.id.notification_content, content);
		notification.contentView.setTextViewText(R.id.notification_time, DateTimeUtil.getShortTime());

		manager.notify(AppConstant.INTERNAL_MESSAGE_ID, notification);
	}

	private void refreshRecentMessage(Context context) {
		Intent intent = new Intent(PushUtils.ACTION_REFRESH_RECENT);
		context.sendOrderedBroadcast(intent, null);
	}
}
