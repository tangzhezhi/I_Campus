package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.DeedListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.DeedSort;
import com.stinfo.pushme.common.AppConstant.DeedType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Deed;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryDeedReq;
import com.stinfo.pushme.rest.entity.QueryDeedResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

/**
 * 曝光台fragment
 * @author wenle
 *
 */
public  class BadDeedFragment extends Fragment  {
	private static final String TAG = "BadDeedFragment";
	//private ArrayList<Notice> mNoticeList = new ArrayList<Notice>();
	private DropDownListView lvBadDeedList;
	private DeedListAdapter mAdapter;
	private View mView;
	private ProgressDialog prgDialog = null;
	private String sortType = "id";
	private int currentFragment = 0;
	
	private int currentPage = 1;
	private int counts = 0 ;
	
	private ArrayList<Deed> mDeedList = new ArrayList<Deed>();
	
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
	
	
	public static BadDeedFragment newInstance() {
		BadDeedFragment newFragment = new BadDeedFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_deed, container, false);
		
		initView();
		
		return mView;
	}
	
	
	@Override
	public void onResume() {
		
		ViewPager pager = (ViewPager)getActivity().findViewById(11);
		
		pager.getCurrentItem();
		//增加排序参数
		if(pager!=null){
			currentFragment = pager.getCurrentItem();
			if(currentFragment==0){
				sortType = DeedSort.id;
			}
			else if(currentFragment==1){
				sortType = DeedSort.clicknum;
			}
			else {
				sortType = DeedSort.iselite;
			}
		}
		
		
		super.onResume();
		
		currentPage = 1;
		
		if(mDeedList!=null && mDeedList.size() > 0){
			mDeedList.clear();
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

	private void initView() {
		lvBadDeedList = (DropDownListView) mView.findViewById(R.id.lv_list);
	}
	
	private void initData() {
		mAdapter = new DeedListAdapter(getActivity(), mDeedList,DeedType.bad);
		lvBadDeedList.setAdapter(mAdapter);
		
		lvBadDeedList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				
				currentPage = 1;
				
				if(mDeedList!=null && mDeedList.size() > 0){
					mDeedList.clear();
				}
				refreshDeedList();
			}
		});
		
		
		
		lvBadDeedList.setOnBottomListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					refreshDeedList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshDeedList();
				}
				else{
					lvBadDeedList.onBottomComplete();
					lvBadDeedList.setFooterNoMoreText("没有更多信息");
				}
			}
			
		});
		
		initDeedList() ;
	}

	
	
	private void initDeedList() {
		try {
			refreshDeedList();
			mAdapter.notifyDataSetChanged();
			lvBadDeedList.setSecondPositionVisible();
			lvBadDeedList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} 
	}
	
	public void refreshDeedList() {
		
		UserCache userCache = UserCache.getInstance();
		QueryDeedReq reqData = new QueryDeedReq();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setCurrentPage(this.currentPage);
		reqData.setPlates(DeedType.bad);
		reqData.setSort(sortType);
		
		if(userInfo instanceof Teacher){
			Teacher teacher = (Teacher) userInfo; 
			reqData.setSchoolcode(teacher.getSchoolId());
		}
		else{
			reqData.setSchoolcode(userCache.getCurrentClass().getSchoolId());
		}

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvBadDeedList.onDropDownComplete();
						lvBadDeedList.onBottomComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvBadDeedList.onDropDownComplete();
						lvBadDeedList.onBottomComplete();
						MessageBox.showServerError(getActivity());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryDeedResp respData = new QueryDeedResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				if(this.counts < 10){
					this.currentPage = 1;
				}
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(getActivity(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(getActivity());
			Log.e(TAG, "解析闪光台/曝光台/爱心分享数据失败! \r\n" + e);
		}
	}

	private void doSuccess(QueryDeedResp respData) {
		mDeedList.addAll( respData.getDeedList());
		mAdapter.notifyDataSetChanged();
		
		lvBadDeedList.onBottomComplete();
		if(currentPage>1){
			lvBadDeedList.setSelection((currentPage-1)*10);
		}
		
	}

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {  
	        super.onSaveInstanceState(outState);  
	}  
}
