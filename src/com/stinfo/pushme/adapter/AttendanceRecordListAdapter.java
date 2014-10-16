package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.AttendanceActivity;
import com.stinfo.pushme.activity.NoticeActivity;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.InOrOutDoorFlag;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.entity.AttendanceRecord;
import com.stinfo.pushme.entity.OutOrInDoorRecord;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.URLChecker;


public class AttendanceRecordListAdapter extends MyBaseAdatper {
	private String TAG = AttendanceRecordListAdapter.class.getName();
	private LayoutInflater mInflater;
	private ArrayList<AttendanceRecord> mNoticeList;
	private Context context ;
	private int screenWidth;
	
	public AttendanceRecordListAdapter(Context context, ArrayList<AttendanceRecord> noticeList,int screenWidth) {
		super();
		mNoticeList = noticeList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.screenWidth = screenWidth;
	}

	@Override
	public int getCount() {
		return mNoticeList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mNoticeList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
	
	
	@Override
	public int getItemViewType(int pos) {
		int outFlag = 0;
		if((pos+1)%2==0){
			outFlag = 1;
		}
		return outFlag;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_attendance, null);

			holder = new ViewHolder();
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
			holder.imUserPic = (ImageView) convertView.findViewById(R.id.iv_userpic);
			holder.imTimeLine = (ImageView) convertView.findViewById(R.id.iv_timeline);
			holder.tvDay =  (TextView) convertView.findViewById(R.id.tv_day);
			
			holder.llayout = (LinearLayout) convertView.findViewById(R.id.layout_attendance_item_ll);
			
			convertView.setTag(holder);
			
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AttendanceRecord attendanceRecord = mNoticeList.get(pos);
		if (attendanceRecord != null) {
			holder.tvUserName.setText(attendanceRecord.getUserName());
			
			String tvCreateTime = "";
		if(attendanceRecord.getAttendanceDay()!=null && attendanceRecord.getAttendanceDay().length() > 6){
				if(attendanceRecord.getAttendanceDay().length()<10){
					
					int monthTemp = Integer.valueOf(attendanceRecord.getAttendanceDay().split("-")[1]) ;
					
					int dayTemp = Integer.valueOf(attendanceRecord.getAttendanceDay().split("-")[2]) ;
					String syearT = attendanceRecord.getAttendanceDay().substring(0, 4);
					String smonthT = (monthTemp<10?"0"+monthTemp:monthTemp+"");
					String sdayT = (dayTemp<10?"0"+dayTemp:dayTemp+"");
					
					attendanceRecord.setAttendanceDay(syearT+"-"+smonthT+"-"+sdayT);
				}
			
			
				DateTime dt = new DateTime(attendanceRecord.getAttendanceDay().substring(0,10));
				
				if(dt.getDayOfYear()== DateTime.now().getDayOfYear()){
					tvCreateTime = "今天";
				}
				else{
					tvCreateTime = attendanceRecord.getAttendanceDay();
				}
			}
			
			holder.tvDay.setText(tvCreateTime);
			
			UserCache userCache = UserCache.getInstance();
			UserInfo userInfo = userCache.getUserInfo();
			if(userInfo.getUserType().equals(UserType.TEACHER)){
				holder.llayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, AttendanceActivity.class);
						
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
						intent.putExtra("userName", mNoticeList.get(pos).getUserName());
						intent.putExtra("userId", mNoticeList.get(pos).getUserId());
						intent.putExtra("attendanceDay", mNoticeList.get(pos).getAttendanceDay());
						
						context.startActivity(intent);
					}
				});
			}
			
			if(attendanceRecord.getUserPic()!=null && !attendanceRecord.getUserPic().equals("")){
				holder.imUserPic.setVisibility(View.VISIBLE);
				if (URLChecker.isUrl(AppConstant.School_Platform_Photo_QUERY_URL+attendanceRecord.getUserPic())) {
					Log.d(TAG, "loadImage: " + AppConstant.School_Platform_Photo_QUERY_URL+attendanceRecord.getUserPic());
					ImageListener listener = ImageLoader.getImageListener(holder.imUserPic,
							R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
					ImageCacheManager.getInstance().getImageLoader().get(AppConstant.School_Platform_Photo_QUERY_URL+attendanceRecord.getUserPic(), listener);
				}
			}
			else{
				holder.imUserPic.setVisibility(View.GONE);
			}
			
			
			
			ArrayList<OutOrInDoorRecord> accessTimes = attendanceRecord.getAccessTimes();
			
			
			  Bitmap baseBitmap =  Bitmap.createBitmap(screenWidth-80,
					  100, Bitmap.Config.ARGB_8888);
			  Canvas canvas  = new Canvas(baseBitmap);
			  canvas.drawColor(Color.TRANSPARENT);
			  	
			  // 初始化一个画笔，笔触宽度为5，颜色为红色
				Paint   paintLine = new Paint();
				paintLine.setStrokeWidth(5);
				paintLine.setAntiAlias(true);
				paintLine.setColor(Color.GRAY);	
			  
			  
				 // 进校点画笔
				Paint   paint = new Paint();
		        paint.setStrokeWidth(10);
		        paint.setAntiAlias(true);
		        paint.setColor(Color.GREEN);	
				
				 //离校点画笔
				Paint   paintPointDown = new Paint();
				paintPointDown.setStrokeWidth(10);
				paintPointDown.setAntiAlias(true);
				paintPointDown.setColor(Color.RED);	
		        
		        
				 // 初始化一个画笔，笔触宽度为5，颜色为红色
				Paint   paintText = new Paint();
				paintText.setTextSize(14);
				paintText.setStrokeWidth(5);
				paintText.setAntiAlias(true);
				paintText.setStyle(Paint.Style.FILL);  
		        paintText.setColor(Color.BLACK);	
			
		     canvas.drawLine(50, 50, screenWidth-50, 50, paintLine);
		        
		     if(accessTimes!=null && accessTimes.size() > 0){
					for(int i = 0 ; i < accessTimes.size();i++){
						
						String tempTime = accessTimes.get(i).getIoTime();
						
						String point = tempTime.substring(11, 13);
						
						String xminPoint = tempTime.substring(14, 16);
						
						String xsecPoint = tempTime.substring(17, 19);
						
						float x = 100+ Float.valueOf(point)*15+Float.valueOf(xminPoint)/6+Float.valueOf(xsecPoint)/60;
						
						if(accessTimes.get(i).getIoFlag().equals(InOrOutDoorFlag.inDoor)){
							canvas.drawPoint(x,50, paint);
							canvas.drawText(tempTime.substring(11, 19), x-50, 65, paintText);
						}
						else{
							canvas.drawPoint(x,50, paintPointDown);
							canvas.drawText(tempTime.substring(11, 19), x-50, 35, paintText);
						}
					}
					canvas.drawText("6:00", 51, 35, paintText);
					canvas.drawText("24:00", screenWidth-60, 35, paintText);
		     }
		     else{
		    	 canvas.drawText("无出入记录", 2, 35, paintText);
		     }
		  
			Log.d(TAG, "屏幕宽度："+screenWidth);
			
		  // 把图片展示到ImageView中
		  holder.imTimeLine.setImageBitmap(baseBitmap);
			
		}
		return convertView;
		
	}
	
	
	
	
	
	
	private final class ViewHolder {
		public LinearLayout llayout;
		public TextView tvUserName;
		public TextView tvDay;
		public ImageView imUserPic;
		public ImageView imTimeLine;
	}
}
