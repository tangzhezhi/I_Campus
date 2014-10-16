package com.stinfo.pushme.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.MainActivity;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.LastAppVerReq;
import com.stinfo.pushme.rest.entity.LastAppVerResp;
import com.stinfo.pushme.util.PushUtils;

public class ForegroundService extends Service {
	  private static final String TAG = ForegroundService.class.getName();
	  private Notification notification;
	  Message m = null;
	  private String mAppUrl = "";
	  private AlertDialog ad = null;
	  private ExecutorService service= Executors.newFixedThreadPool(2);  
	  private	Timer timer = null;
	    @Override
	    public void onCreate() {
	        super.onCreate();
	      //自定义通知栏样式
	        RemoteViews mRemoteViews = new RemoteViews(this.getPackageName(),R.layout.notification_show);
	        mRemoteViews.setTextViewText(R.id.notify_title_info, "   掌上校园");
	        // 声明一个通知，并对其进行属性设置
	        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(ForegroundService.this)
	        .setSmallIcon(R.drawable.actionbar_icon)
	        .setContent(mRemoteViews)
	        ;
//	        .setContentTitle("掌上校园")
//	        .setContentText("");
	        
	        
	        // 声明一个Intent，用于设置点击通知后开启的Activity
	        Intent resuliIntent=new Intent(ForegroundService.this, MainActivity.class);
	        PendingIntent resultPendingIntent=PendingIntent.getActivity(ForegroundService.this, 0, resuliIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	        mBuilder.setContentIntent(resultPendingIntent);
	        notification=mBuilder.build();
	        // 把当前服务设定为前台服务，并指定显示的通知。
	        startForeground(1,notification);
	        
	        try {
				timer = new Timer();
				
				timer.schedule(task,0, 3600000);
				timer.schedule(queryUpdateVersion,0, 3600000);
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "前台服务注册信鸽服务出错"+e);
			}
	        
	    }
	    
	    TimerTask task = new TimerTask(){    
	    	public void run(){
	    		Message message = new Message(); 
	    		message.what = 1; 
	    		handler.sendMessage(message); 
	    	}    
	    };
	    
	    TimerTask queryUpdateVersion = new TimerTask(){    
	    	public void run(){
	    		Message message = new Message(); 
	    		message.what = 1; 
	    		handlerQueryVersionUpdate.sendMessage(message); 
	    	}    
	    };
	    
	    @Override  
	    public int onStartCommand(Intent intent, int flags, int startId) {  
	        super.onStartCommand(intent, flags, startId);  
	        Log.d(TAG, "onStartCommand");  
	        return START_STICKY;  
	    }   
	    
	    
	    @Override
	    public void onDestroy() {        
	        super.onDestroy();
	        // 在服务销毁的时候，使当前服务推出前台，并销毁显示的通知
	        stopForeground(false);
	    }
	  
	  
	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }
	    
	    
	    Handler handler = new Handler() {  
	        public void handleMessage(Message msg) {   
	             switch (msg.what) {   
	                  case 1:   
	                	  service.submit(new RegisterXinGeThread());
	                  break;   
	             }   
	        }   
	   };  
	   
	   
	    Handler handlerQueryVersionUpdate = new Handler() {  
	        public void handleMessage(Message msg) {   
	             switch (msg.what) {   
	                  case 1:   
	                	  service.submit(new QueryVersionUpdateThread());
	                  break;   
	             }   
	        }   
	   };  
	   
	   
	   
	   public class RegisterXinGeThread implements Runnable {

	       @Override
	       public void run() {
	    	   try {
            	Log.d("ForegroundService", "向百度推送进行注册。。。。");
            	
                if (!PushUtils.hasBind(getApplicationContext())) {
                    PushManager.startWork(getApplicationContext(),
                            PushConstants.LOGIN_TYPE_API_KEY,
                            PushUtils.getMetaValue(ForegroundService.this, "api_key"));
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
	       }
	   }
	   
	   
	   
	   public class QueryVersionUpdateThread implements Runnable {
		   
		   private void onCheckUpdate() {
				LastAppVerReq reqData = new LastAppVerReq();
				reqData.setAppName("pushme");
				reqData.setAppType(3);

				MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								Log.v(TAG, "Response: " + response);
								checkResponse(response);
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.d(TAG, "查询版本更新异常："+error.getMessage());
							}
						});

				
				Log.d(TAG, "检查中..., 请稍等");
				
				RequestController.getInstance().addToRequestQueue(req, TAG);
			}

			private void checkResponse(String response) {
				try {
					LastAppVerResp respData = new LastAppVerResp(response);
					if (respData.getAck() == Ack.SUCCESS) {
						checkVersion(respData);
					} else {
						Log.d(TAG, "查询版本更新异常："+respData.getAck());
					}
				} catch (Exception e) {
					Log.e(TAG, "Failed to parser response data! \r\n" + e);
				}
			}

			private void checkVersion(LastAppVerResp respData) {
				int localVersion = 0;
				try {
					localVersion = MyApplication.getInstance().getAppVersion();
				} catch (Exception ex) {
					Log.e(TAG, "Failed to get local package version! ", ex);
					return;
				}

				mAppUrl = respData.getAppUrl();
				Log.d(TAG, "localVersion: " + localVersion);
				Log.d(TAG, "appUrl: " + mAppUrl);
				Log.d(TAG, "serverVersion: " + respData.getVersionCode());
				Log.d(TAG, "serverVersionName: " + respData.getVersionName());
				Log.d(TAG, "updateTime: " + respData.getUpdateTime());
				String promptMsg = "发现新版本 V" + respData.getVersionName() + ", 建议立即更新使用";

				if (localVersion < respData.getVersionCode()) {
					
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(ForegroundService.this);
					alertDialog.setTitle("软件升级");
					alertDialog.setMessage(promptMsg);
					alertDialog.setPositiveButton("更新",
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)
								{
									updatePackage();
								}
							});
			
					alertDialog.setNegativeButton("取消", null);
					
					ad=alertDialog.create();  
			        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  
			        ad.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失                               
			        ad.show();
				} 

			}

		   
			private void updatePackage() {
				
				if(ad!=null && ad.isShowing()){
					Intent updateIntent = new Intent("com.stinfo.pushme.UPDATE_SERVICE");
					updateIntent.putExtra("appName", getResources().getString(R.string.app_name));
					updateIntent.putExtra("appUrl", mAppUrl);
					ForegroundService.this.startService(updateIntent);
					
					ad.closeOptionsMenu();
					ad.dismiss();
				}
				
			}
		   
	       @Override
	       public void run() {
	    	   try {
            	Log.d("ForegroundService", "查询版本更新");
            	onCheckUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
	       }
	   }
	   
	   
	   
}
