package com.stinfo.pushme.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.joda.time.DateTime;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.AttendanceRecordListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.SchoolLeader;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.AttendanceRecord;
import com.stinfo.pushme.entity.OutOrInDoorRecord;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryAttendanceRecordReq;
import com.stinfo.pushme.rest.entity.QueryAttendanceRecordResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

public class AttendanceActivity extends BaseActionBarActivity {
	private static final String TAG = "AttendanceActivity";
	private WheelMain wheelMain;
	private RelativeLayout rltime;
	private TextView txttime;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private DropDownListView lvAttendanceList;
	private ProgressDialog prgDialog = null;
	private AttendanceRecordListAdapter mAdapter;
	private ArrayList<AttendanceRecord> mAttendanceRecordList = new ArrayList<AttendanceRecord>();
	private int screenWidth;
	private String startDay="";
	private String endDay="";
	private String userId="";
	
	public void showProgressDialog() {
		prgDialog = new ProgressDialog(AttendanceActivity.this);
		prgDialog.setMessage("正在获取数据中...");
		prgDialog.show();
	}

	public void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_attendance);
		ActionBar bar = getSupportActionBar();
		//设置actionbar的导航模式  
		bar.setDisplayHomeAsUpEnabled(true);
		
		if(getIntent()!=null){
			userId = getIntent().getStringExtra("userId");
			endDay = getIntent().getStringExtra("attendanceDay");
			startDay = new DateTime(endDay).toString("yyyy-MM-dd").substring(0, 7)+"-01";
		}
		
		 initView();
		 
		 initData();
	}
	
	
	
	
	
	private void initView(){
		
		final ScreenInfo screenInfo = new ScreenInfo(AttendanceActivity.this);
		screenWidth = screenInfo.getWidth();
		
		lvAttendanceList = (DropDownListView)findViewById(R.id.lv_attendance_list);
		
		rltime =  (RelativeLayout)findViewById(R.id.rl_time);
	      txttime = (TextView)findViewById(R.id.txttime);
		  	Calendar calendar = Calendar.getInstance();
		  	
		  	
		  	int tempMonth = calendar.get(Calendar.MONTH) + 1;
		  	int tempDay =calendar.get(Calendar.DAY_OF_MONTH);
		  	
		  	txttime.setText(calendar.get(Calendar.YEAR) + "-" +
		  				    (tempMonth<10?"0"+tempMonth:tempMonth )+ "-" +
		  				  (tempDay<10?"0"+tempDay:tempDay ) + "");
		  	rltime.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					LayoutInflater inflater=LayoutInflater.from(AttendanceActivity.this);
					final View timepickerview=inflater.inflate(R.layout.timepicker, null);
					
					Log.d(TAG, "从日期中获得屏幕宽度:::"+screenWidth);
					
					wheelMain = new WheelMain(timepickerview);
					wheelMain.screenheight = screenInfo.getHeight();
					String time = txttime.getText().toString();
					Calendar calendar = Calendar.getInstance();
					if(JudgeDate.isDate(time, "yyyy-MM-dd")){
						try {
							calendar.setTime(dateFormat.parse(time));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					wheelMain.initDateTimePicker(year,month,day);
					new AlertDialog.Builder(AttendanceActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							txttime.setText(wheelMain.getTime());
							
							if(mAttendanceRecordList!=null && mAttendanceRecordList.size() > 0){
								mAttendanceRecordList.clear();
							}
							refreshAttendanceList();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					})
					.show();
				}
			});
	}
	
	
	private void initData(){
		
		mAdapter = new AttendanceRecordListAdapter(this, mAttendanceRecordList,screenWidth);
		lvAttendanceList.setAdapter(mAdapter);
		
		lvAttendanceList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				
				if(mAttendanceRecordList!=null && mAttendanceRecordList.size() > 0){
					mAttendanceRecordList.clear();
				}
				refreshAttendanceList();
			}
		});
	
		
		initAttendanceList() ;
	}
	
	
	private void initAttendanceList() {
		try {
			refreshAttendanceList();
			
			mAdapter.notifyDataSetChanged();
			lvAttendanceList.setSecondPositionVisible();
			lvAttendanceList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "查询平安点到打卡记录出错: " + e);
		} 
	}
	
	public void refreshAttendanceList() {
		showProgressDialog();
		UserCache userCache = UserCache.getInstance();
		QueryAttendanceRecordReq reqData = new QueryAttendanceRecordReq();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		if(userId!=null && !userId.equals("")){
			if(userId.equals(userInfo.getUserId()) && userInfo.getUserType().equals(UserType.TEACHER)){
				reqData.setTeacherid(userId);
			}
			else {
				reqData.setStuid(userId);
			}
			
			 endDay = txttime.getText().toString();
			 startDay = new DateTime(endDay).toString("yyyy-MM-dd").substring(0, 7)+"-01";
			
		}
		else{
			if(userInfo.getUserType().equals(UserType.TEACHER)){
				reqData.setClassid(userCache.getCurrentClass().getClassId());
				 startDay = txttime.getText().toString();
				 endDay = new DateTime(startDay).plusDays(1).toString("yyyy-MM-dd");
			}
			else if(userInfo.getUserType().equals(UserType.PARENT)){
				Parent parent = (Parent)userInfo;
				if(parent.getChildList()!=null && parent.getChildList().size() > 0 ){
					reqData.setStuid(parent.getChildList().get(0).getUservalue());
				}
				 endDay = txttime.getText().toString();
				 startDay = new DateTime(endDay).toString("yyyy-MM-dd").substring(0, 7)+"-01";
			}
			else if(userInfo.getSchoolLeader().equals(SchoolLeader.yes)){
				
			}
			else{
				Log.d(TAG, "找不到相应用户类型");
			}
		}
		
		reqData.setSessionKey(userCache.getSessionKey());
		
		reqData.setBeginDate(startDay);
		reqData.setEndDate(endDay);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvAttendanceList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						lvAttendanceList.onDropDownComplete();
						MessageBox.showServerError(AttendanceActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryAttendanceRecordResp respData = new QueryAttendanceRecordResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				closeProgressDialog();
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(AttendanceActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(AttendanceActivity.this);
			Log.e(TAG, "解析平台点到数据失败 \r\n" + e);
		}
	}

	private void doSuccess(QueryAttendanceRecordResp respData) {
		
		
		if( respData.getAttendanceRecordList()!=null &&  respData.getAttendanceRecordList().size() > 0){
			ArrayList<AttendanceRecord> attendanceRecordList = new ArrayList<AttendanceRecord>();	
			
			ArrayList<AttendanceRecord> attendanceRecordListSrc = respData.getAttendanceRecordList();
			
			//单个人多天
			if(attendanceRecordListSrc.size()==1){
				AttendanceRecord attendanceRecordTemp = attendanceRecordListSrc.get(0);
				
				
				int dayscounts = new DateTime(endDay).getDayOfYear() - new DateTime(startDay).getDayOfYear();
				
				ArrayList<String> days = new ArrayList<String>();
				
				for(int i = 0 ; i <= dayscounts; i++){
					days.add(new DateTime(startDay).plusDays(i).toString("yyyy-MM-dd"));
				}
				
				
				for(String  day : days){
					AttendanceRecord attendanceRecord = new AttendanceRecord();
					attendanceRecord.setId(attendanceRecordTemp.getId());
					attendanceRecord.setUserId(attendanceRecordTemp.getUserId());
					attendanceRecord.setUserName(attendanceRecordTemp.getUserName());
					attendanceRecord.setUserPic(attendanceRecordTemp.getUserPic());
					attendanceRecord.setAttendanceDay(day);
					
					ArrayList<OutOrInDoorRecord> oiList = new ArrayList<OutOrInDoorRecord>();
					
					if(attendanceRecordTemp.getAccessTimes()!=null){
						for(OutOrInDoorRecord  outOrInDoorRecord : attendanceRecordTemp.getAccessTimes()){
							if(outOrInDoorRecord.getPid().equals(day)){
								oiList.add(outOrInDoorRecord);
							}
						}
					}
					attendanceRecord.setAccessTimes(oiList);
					attendanceRecordList.add(attendanceRecord);
				}
				Collections.sort(attendanceRecordList, new AttendanceRecordComparator2());
				mAttendanceRecordList.addAll(attendanceRecordList);
			}
			else{ //多人单天
				
				for(AttendanceRecord attendanceRecord :attendanceRecordListSrc){
					attendanceRecord.setAttendanceDay(txttime.getText().toString());
				}
				
				Collections.sort(attendanceRecordListSrc, new AttendanceRecordComparator());
				mAttendanceRecordList.addAll(attendanceRecordListSrc);
			}

		}
		mAdapter.notifyDataSetChanged();
		lvAttendanceList.onDropDownComplete();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.finish();
	}
	
	
	/**
	 * 菜单按钮选择
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
	
	public class AttendanceRecordComparator implements Comparator<AttendanceRecord> {
		
		@Override
		public int compare(AttendanceRecord lhs, AttendanceRecord rhs) {
			String pinYin1 = lhs.getPinYin();
			String pinYin2 = rhs.getPinYin();
			return pinYin1.compareTo(pinYin2);
		}
	}
	
	public class AttendanceRecordComparator2 implements Comparator<AttendanceRecord> {
		
		@Override
		public int compare(AttendanceRecord lhs, AttendanceRecord rhs) {
			String attendanceDay1 = lhs.getAttendanceDay();
			String attendanceDay2 = rhs.getAttendanceDay();
			return attendanceDay2.compareTo(attendanceDay1);
		}
	}
	
}