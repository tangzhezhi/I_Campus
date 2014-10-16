package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryNoticeResp;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachResp;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.NoticeDetailActivity;
import com.stinfo.pushme.activity.VirtuousTeachDetailActivity;
import com.stinfo.pushme.adapter.NoticeListAdapter;
import com.stinfo.pushme.adapter.VirtuousTeachListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.NoticeQueryType;
import com.stinfo.pushme.common.AppConstant.NoticeType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.VirtuousTeachDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.entity.VirtuousTeach;

/**
 * 道德币奖励详细记录
 * @author lenovo
 *
 */
public final class VirtuousTeachDetailListFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "VirtuousTeachDetailListFragment";
	private  ArrayList<VirtuousTeach> virtuousTeachList  = new ArrayList<VirtuousTeach>();
	private VirtuousTeachListAdapter mAdapter;
	private DropDownListView lv_virtuousTeach_detail_list;
	private View mView;
	private String studentId;
	private String userType;
	private String studentName ;
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	
	private int currentPage = 0 ;
	private int counts;
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}
	
	public static VirtuousTeachDetailListFragment newInstance() {
		VirtuousTeachDetailListFragment newFragment = new VirtuousTeachDetailListFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_virtuous_teach_list, container, false);
		return mView;
	}
	
	/**
	 * 当被其他程序打断也会重新调用进入
	 */
	@Override
	public void onResume() {
		super.onResume();
		currentPage = 1;
		if(virtuousTeachList!=null && virtuousTeachList.size() > 0){
			virtuousTeachList.clear();
		}
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
		studentId =  getActivity().getIntent().getStringExtra("studentId");
		userType =  getActivity().getIntent().getStringExtra("userType");
		studentName =   getActivity().getIntent().getStringExtra("studentName");
		
		UserCache userCache = UserCache.getInstance();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		lv_virtuousTeach_detail_list =  (DropDownListView) getActivity().findViewById(R.id.lv_virtuous_teach_list);  
		final ClassInfo classInfo = userCache.getCurrentClass();
		
		try {
			
			loadVirtuousTeachDetailFromRemote(studentId,classInfo.getClassId(),classInfo.getSchoolId(),userType);
			
			mAdapter = new VirtuousTeachListAdapter(getActivity(),virtuousTeachList);
			lv_virtuousTeach_detail_list.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
			lv_virtuousTeach_detail_list.setSecondPositionVisible();
			lv_virtuousTeach_detail_list.onDropDownComplete();
			
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} 
		
		lv_virtuousTeach_detail_list.setOnItemClickListener(this);
		lv_virtuousTeach_detail_list.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				
				currentPage = 1;
				
				if(virtuousTeachList!=null && virtuousTeachList.size() > 0){
					virtuousTeachList.clear();
				}
				loadVirtuousTeachDetailFromRemote(studentId,classInfo.getClassId(),classInfo.getSchoolId(),userType);
				
			}
		});
		
		
		lv_virtuousTeach_detail_list.setOnBottomListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					loadVirtuousTeachDetailFromRemote(studentId,classInfo.getClassId(),classInfo.getSchoolId(),userType);
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					loadVirtuousTeachDetailFromRemote(studentId,classInfo.getClassId(),classInfo.getSchoolId(),userType);
				}
				else{
					lv_virtuousTeach_detail_list.onBottomComplete();
					lv_virtuousTeach_detail_list.setFooterNoMoreText("没有更多信息");
				}
			}
			
		});
		
		mAdapter.notifyDataSetChanged();
		lv_virtuousTeach_detail_list.setSecondPositionVisible();
		lv_virtuousTeach_detail_list.onDropDownComplete();
		
	}
	
	
	private void loadVirtuousTeachDetailFromRemote(String userId,String classId,String schoolId,String userType) {
		UserCache userCache = UserCache.getInstance();
		QueryVirtuousTeachReq reqData = new QueryVirtuousTeachReq();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		reqData.setUserId(userId);
		reqData.setSessionkey(UserCache.getInstance().getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setSchoolId(schoolId);
		reqData.setCurrentPage(this.currentPage);
		reqData.setUserType(userType);
		reqData.setStudentId(userId);
		reqData.setFlag("2");
		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lv_virtuousTeach_detail_list.onDropDownComplete();
						lv_virtuousTeach_detail_list.onBottomComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lv_virtuousTeach_detail_list.onDropDownComplete();
						lv_virtuousTeach_detail_list.onBottomComplete();
						MessageBox.showServerError(getActivity());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkResponse(String response) {
		try {
			QueryVirtuousTeachResp respData = new QueryVirtuousTeachResp(response);
			if (respData!=null) {
				this.counts = respData.getCount();
				if(this.counts < 10){
					this.currentPage = 1;
				}
				doSuccess(respData);
			} 
		} catch (Exception e) {
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryVirtuousTeachResp respData) {
		lv_virtuousTeach_detail_list =  (DropDownListView) getActivity().findViewById(R.id.lv_virtuous_teach_list);  
		try {
			
			ArrayList<VirtuousTeach> list = new ArrayList<VirtuousTeach>();
			if(respData!=null && respData.getVirtuousTeachList().size() > 0){
				list = respData.getVirtuousTeachList();
				for(VirtuousTeach vs : list){
					vs.setUserName(studentName);
				}
				
			}
			
			virtuousTeachList.addAll(list);
			
			mAdapter = new VirtuousTeachListAdapter(getActivity(),virtuousTeachList);
			lv_virtuousTeach_detail_list.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
			
			lv_virtuousTeach_detail_list.onBottomComplete();
			
			if(currentPage>1){
				lv_virtuousTeach_detail_list.setSelection((currentPage-1)*10);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		}
	}
	
	
	/**
	 * 点击记录时，跳转到详细界面
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
	}
}
