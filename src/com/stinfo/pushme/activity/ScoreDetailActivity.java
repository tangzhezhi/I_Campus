package com.stinfo.pushme.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.ScoreDetailAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.ScoreDBAdapter;
import com.stinfo.pushme.entity.ExamRound;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Score;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryScoreReq;
import com.stinfo.pushme.rest.entity.QueryScoreResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;

public class ScoreDetailActivity  extends BaseActionBarFragmentActivity {
	private String TAG = "ScoreDetailActivity";
	private  ArrayList<ExamRound> examRoundList ;
	private  ArrayList<Score> scoreList ;
	private int mIndex = 0;
	private ScoreDetailAdapter mAdapter;
	private DropDownListView lv_score_detail_list;
	private String userType;
	private String studentId;
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_score_detail_list);
		examRoundList = (ArrayList<ExamRound>) getIntent().getSerializableExtra("examRoundList");
		mIndex = getIntent().getIntExtra("index", 0);
		studentId =  getIntent().getStringExtra("studentId");
		
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setTitle(getResources().getString(R.string.view_content));
		initView();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void initView() {
		
		lv_score_detail_list =  (DropDownListView) findViewById(R.id.lv_score_detail_list);  
		if(examRoundList!=null && examRoundList.size() > 0){
			ExamRound examRound = examRoundList.get(mIndex);
			
//			ScoreDBAdapter dbAdapter = new ScoreDBAdapter();
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			try {
				loadScoreDetailFromRemote(userInfo.getUserId(),examRound.getExamRoundId());
//				dbAdapter.open();
//				scoreList = new ArrayList<Score>();
//				scoreList = dbAdapter.getAllScoreDetail(examRound.getExamRoundId(),studentId);
//				if(scoreList.size()==0){
//					loadScoreDetailFromRemote(userInfo.getUserId(),examRound.getExamRoundId());
//				}
			
//				lv_score_detail_list.setAdapter(mAdapter);
//				mAdapter.notifyDataSetChanged();
				
			} catch (Exception e) {
				Log.e(TAG, "Failed to operate database: " + e);
			} finally {
//				dbAdapter.close();
			}
		}
	}
	
	private void loadScoreDetailFromRemote(String userId,String examRoundId) {
		UserCache userCache = UserCache.getInstance();
		QueryScoreReq reqData = new QueryScoreReq();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setExamRoundId(examRoundId);
		reqData.setStudentUserId(studentId);
	
		if(userInfo instanceof Parent){
			userType = "2";
		}
		else if(userInfo instanceof Teacher){
			userType = "3";
		}
		else if(userInfo instanceof Student){
			userType = "1";
		}
		else{
			userType = "0";
		}
		reqData.setUserType(userType);
		
		
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
						MessageBox.showServerError(ScoreDetailActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	



	private void checkResponse(String response) {
		try {
			QueryScoreResp respData = new QueryScoreResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(ScoreDetailActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryScoreResp respData) {
		
		if(scoreList!=null){
			scoreList.clear();
		}
		
		lv_score_detail_list =  (DropDownListView) findViewById(R.id.lv_score_detail_list);  
		
		
		lv_score_detail_list.setHeaderDefaultText("");
		lv_score_detail_list.setHeaderPullText("");
		
//		ScoreDBAdapter dbAdapter = new ScoreDBAdapter();
		try {
//			dbAdapter.open();
//			dbAdapter.addScore(respData.getScoreList());
//			ArrayList<Score> list = new ArrayList<Score>();
			
			UserCache userCache = UserCache.getInstance();
			UserInfo userInfo = userCache.getUserInfo();
			if(userInfo instanceof Student){
				studentId = userInfo.getUservalue();
			}
			scoreList = respData.getScoreList();
			mAdapter = new ScoreDetailAdapter(this,scoreList);
//			list = dbAdapter.getAllScoreDetail(examRoundList.get(mIndex).getExamRoundId(),studentId);
			lv_score_detail_list.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
//			dbAdapter.close();
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
}
