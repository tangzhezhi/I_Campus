package com.stinfo.pushme.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.NoticeDetailActivity;
import com.stinfo.pushme.adapter.NoticeListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.NoticeQueryType;
import com.stinfo.pushme.common.AppConstant.SchoolLeader;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.NoticeComparator;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserComparator;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryNoticeResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

public final class NoticeSchoolFragment extends Fragment  {
	private static final String TAG = "NoticeSchoolFragment";
	private ArrayList<Notice> mNoticeList = new ArrayList<Notice>();
	private NoticeListAdapter mAdapter;
	private DropDownListView lvNoticeList;
	private View mView;
	private ProgressDialog prgDialog = null;
	
	//未查看总数
	int noreadCount = 0;
	
	private int currentPage = 0 ;
	private int counts;
	
	private int schoolFragmentPos = 1;
	
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
	
//	private void showProgressDialog() {
//		prgDialog = new ProgressDialog(getActivity());
//		prgDialog.setMessage("正在获取数据中...");
//		prgDialog.show();
//	}
//
//	private void closeProgressDialog() {
//		if ((prgDialog != null) && (prgDialog.isShowing())) {
//			prgDialog.dismiss();
//		}
//	}
	
	
	public static NoticeSchoolFragment newInstance() {
		NoticeSchoolFragment newFragment = new NoticeSchoolFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		if(savedInstanceState!=null){
			Log.d(TAG, "通知总数为"+savedInstanceState.getInt("counts"));
		}
		mView = inflater.inflate(R.layout.fragment_notice_list, container, false);
		//initData();
		return mView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.edit, menu);
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		MenuItem editNotice = menu.findItem(R.id.action_edit);
		if (userInfo instanceof Teacher && userInfo.getSchoolLeader().equals(SchoolLeader.yes)) {
			editNotice.setVisible(true);
		}
		else{
			editNotice.setVisible(false);
		}
	}
	
	//fragment可见时触发
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
            // TODO Auto-generated method stub
            if (isVisibleToUser) {
               //fragment可见时加载数据
            	currentPage = 1;
        		if(mNoticeList!=null && mNoticeList.size() > 0){
        			mNoticeList.clear();
        		}
        		if(mView != null){
        			initData();
        		}
        		
        		
            	
            	
    }
      super.setUserVisibleHint(isVisibleToUser);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		
		ViewPager pager = (ViewPager)getActivity().findViewById(11);
		
		//pager.getCurrentItem();
		
		System.out.println("学校公告当前页..."+pager.getCurrentItem());
		if(pager.getCurrentItem() == 0){
			currentPage = 1;
			if(mNoticeList!=null && mNoticeList.size() > 0){
				mNoticeList.clear();
			}
			initData();
		}
		
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

	private void initData() {
		lvNoticeList = (DropDownListView) mView.findViewById(R.id.lv_notice_list);
		mAdapter = new NoticeListAdapter(mView.getContext(), mNoticeList,schoolFragmentPos);
		lvNoticeList.setAdapter(mAdapter);
		
		refreshNoticeList();
		
		lvNoticeList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				
				currentPage = 1;
				
				if(mNoticeList!=null && mNoticeList.size() > 0){
					mNoticeList.clear();
				}
				refreshNoticeList();
			}
		});
		
		
		
		lvNoticeList.setOnBottomListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					refreshNoticeList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshNoticeList();
				}
				else{
					lvNoticeList.onBottomComplete();
					lvNoticeList.setFooterNoMoreText("没有更多信息");
				}
			}
			
		});
		
		

		initNoticeList();
	}

	private void initNoticeList() {
		try {
			mAdapter.notifyDataSetChanged();
			lvNoticeList.setSecondPositionVisible();
			lvNoticeList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} 
	}

	public void refreshNoticeList() {
		
		UserCache userCache = UserCache.getInstance();
		QueryNoticeReq reqData = new QueryNoticeReq();
		String createTime = "";
		
		UserInfo userInfo = userCache.getUserInfo();
		if(userInfo instanceof Teacher){
			//老师
			reqData.setReceivetype(UserType.TEACHER);
		}
		else{
			//默认家长
			reqData.setReceivetype(UserType.PARENT);
		}
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setType(String.valueOf(NoticeQueryType.QUERY_SCHOOL));
		
		reqData.setReceiveobject(userCache.getCurrentClass().getSchoolId());
		reqData.setCurrentPage(this.currentPage);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvNoticeList.onDropDownComplete();
						lvNoticeList.onBottomComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvNoticeList.onDropDownComplete();
						lvNoticeList.onBottomComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryNoticeResp respData = new QueryNoticeResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				if(this.counts < 10){
					this.currentPage = 1;
				}
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(mView.getContext(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void doSuccess(QueryNoticeResp respData) {
		mNoticeList.addAll( respData.getNoticeList());
		
		if(mNoticeList!=null && mNoticeList.size() > 0){
			noreadCount = 0;
			DBAdapter dbAdapter = new DBAdapter();
			try {
				dbAdapter.open();
				dbAdapter.addNotice(mNoticeList);
				for(Notice notice :mNoticeList){
					UserInfo userInfo = dbAdapter.getUserInfoByUserId(notice.getAuthorId());
					//判断已读或未读状态
					if(dbAdapter.getNoticeIsReadByWhere(notice)){
						notice.setRead(true);
					}else{
						notice.setRead(false);
						noreadCount++;
					}
					if(userInfo!=null){
						notice.setAuthorName(userInfo.getUserName());
					}
					else{
						notice.setAuthorName("无");
					}
				
				}
				
				//将未读标记放入广播
				if(noreadCount > 0){
					Intent intent = new Intent();  //Itent就是我们要发送的内容
		            intent.putExtra("schoolNoticeMsg", noreadCount); 
		            intent.setAction(PushUtils.ACTION_PUSH_OTHER_MESSAGE);
		            //fragment中的广播消息发送(activity中直接this.sendBroadcast)
		            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
		            
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				dbAdapter.close();
			}
		}
		
		mAdapter.notifyDataSetChanged();
		
		lvNoticeList.onBottomComplete();
		if(currentPage>1){
			lvNoticeList.setSelection((currentPage-1)*10);
		}
		
	}

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {  
	        super.onSaveInstanceState(outState);  
	}  
}
