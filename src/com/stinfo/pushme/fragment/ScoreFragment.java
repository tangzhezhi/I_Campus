package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.ScoreDetailActivity;
import com.stinfo.pushme.adapter.ExamRoundListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.ExamRoundDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ExamRound;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryExamRoundReq;
import com.stinfo.pushme.rest.entity.QueryExamRoundResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

public class ScoreFragment  extends Fragment  {
	private String TAG = "ScoreFragment";
	private View mView;
    private ArrayList<ExamRound> examRoundList ;
	private ExamRoundListAdapter examRoundListAdapter;
	private ProgressDialog prgDialog = null;
	//同时实现下拉刷新及滑动到底部加载更多的ListView
	private DropDownListView lvExamRoundList;
	private String checkYear;
	private String userType;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public static Fragment newInstance() {
		ScoreFragment newFragment = new ScoreFragment();
		return newFragment;
	}
	
	private void showProgressDialog() {
		prgDialog = new ProgressDialog(getActivity());
		prgDialog.setMessage("正在获取数据中...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}
	
	public ExamRoundListAdapter getExamRoundListAdapter() {
		return examRoundListAdapter;
	}


	public void setExamRoundListAdapter(ExamRoundListAdapter examRoundListAdapter) {
		this.examRoundListAdapter = examRoundListAdapter;
	}

	public ArrayList<ExamRound> getExamRoundList() {
		return examRoundList;
	}


	public void setExamRoundList(ArrayList<ExamRound> examRoundList) {
		this.examRoundList = examRoundList;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	/**
	 * 实例化并渲染视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_score_list, container, false);
		return mView;
	}
	
	
	/**
	 * 当被其他程序打断也会重新调用进入
	 */
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	/**
	 * 初始化数据，初始化界面
	 */
	private void initData() {
		lvExamRoundList =  (DropDownListView) mView.findViewById(R.id.lv_examround_list);  
		ExamRoundDBAdapter dbAdapter = new ExamRoundDBAdapter();
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		try {
			showProgressDialog();
			loadExamRoundFromRemote();
//			dbAdapter.open();
//			examRoundList = dbAdapter.getExamRound(classInfo.getClassId(),checkYear);
//			if(examRoundList.size()==0){
//				String year = String.valueOf(DateTime.now().getYear());
//				loadExamRoundFromRemote(year);
//			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			closeProgressDialog();
//			dbAdapter.close();
		}
		
		examRoundListAdapter = new ExamRoundListAdapter(this.getActivity(),examRoundList);
		lvExamRoundList.setAdapter(examRoundListAdapter);
		examRoundListAdapter.notifyDataSetChanged();
		lvExamRoundList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int pos,
					long arg3) {
				Log.d(TAG, "点击了"+pos);
				switch (adapterView.getId()) {
				case R.id.lv_examround_list:
					Intent intent = new Intent(getActivity(),ScoreDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("examRoundList", examRoundList);
					bundle.putInt("index", pos==0?pos:pos-1);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				}
			}
		});
		
		
		/**
		 * 下拉刷新数据
		 */
		lvExamRoundList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				refreshExamRoundList();
			}
		});

	}
	
	/**
	 * 下拉动作、从服务器获取数据,解析成功后，插入本地数据库
	 */
	private void refreshExamRoundList() {

			UserCache userCache = UserCache.getInstance();
			//封装参数
			QueryExamRoundReq reqData = new QueryExamRoundReq();
			
			reqData.setUserId(userCache.getUserInfo().getUserId());
			reqData.setSessionKey(userCache.getSessionKey());
			reqData.setUserId(userCache.getUserInfo().getUserId());
			reqData.setYear(String.valueOf(DateTime.now().getYear()));
			
			UserInfo userInfo = userCache.getUserInfo();
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
			String examDate = "0";
			if (examRoundList.size() > 0) {
				examDate = examRoundList.get(0).getExamDate();
			}
			
			reqData.setExamDate(examDate);
			
			MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.v(TAG, "Response: " + response);
							checkResponse(response);
							lvExamRoundList.onDropDownComplete();
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							lvExamRoundList.onDropDownComplete();
							MessageBox.showServerError(mView.getContext());
						}
					});

			RequestController.getInstance().addToRequestQueue(req, TAG);
		
	}
	
	
	/**
	 * 向服务器发起请求，获得数据
	 * @param week
	 */
	private void loadExamRoundFromRemote(){
		UserCache userCache = UserCache.getInstance();
		
		int year = DateTime.now().getYear();
		
		int month = DateTime.now().getMonthOfYear();
		
		int aterm = 1;
		
		if(month >2 &&  month < 8){
			aterm = 2;
		}
		
		QueryExamRoundReq reqData = new QueryExamRoundReq();
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setYear(String.valueOf(year));
		reqData.setAtrem(String.valueOf(aterm));
		
//		UserInfo userInfo = userCache.getUserInfo();
//		if(userInfo instanceof Parent){
//			userType = "2";
//		}
//		else if(userInfo instanceof Teacher){
//			userType = "3";
//		}
//		else if(userInfo instanceof Student){
//			userType = "1";
//		}
//		else{
//			userType = "0";
//		}
//		reqData.setUserType(userType);
		
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
						MessageBox.showServerError(getActivity());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
}



	private void checkResponse(String response) {
		try {
			QueryExamRoundResp respData = new QueryExamRoundResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(getActivity(), respData.getAck());
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryExamRoundResp respData) {
		
		if(examRoundList!=null){
			examRoundList.clear();
		}
		
		
		lvExamRoundList =  (DropDownListView) mView.findViewById(R.id.lv_examround_list); 
//		ExamRoundDBAdapter dbAdapter = new ExamRoundDBAdapter();
		try {
			examRoundList = respData.getExamRoundList();
//			dbAdapter.open();
//			dbAdapter.addExamRound(respData.getExamRoundList());
//			ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
//			examRoundList = dbAdapter.getExamRound(classInfo.getClassId(), String.valueOf(DateTime.now().getYear()));
			examRoundListAdapter = new ExamRoundListAdapter(this.getActivity(),examRoundList);
			lvExamRoundList.setAdapter(examRoundListAdapter);
			examRoundListAdapter.notifyDataSetChanged();
			closeProgressDialog();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
//			dbAdapter.close();
		}
	}
	
}
