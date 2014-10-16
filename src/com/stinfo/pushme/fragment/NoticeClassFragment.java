package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryNoticeResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.NoticeListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.Classmaster;
import com.stinfo.pushme.common.AppConstant.NoticeQueryType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;

/**
 * 班级公告
 * @author lenovo
 *
 */
public final class NoticeClassFragment extends Fragment {
	private static final String TAG = "NoticeClassFragment";
	private ArrayList<Notice> mNoticeList = new ArrayList<Notice>();
	private NoticeListAdapter mAdapter;
	
	//同时实现下拉刷新及滑动到底部加载更多的ListView
	private DropDownListView lvNoticeList;
	private View mView;
	private int currentPage = 0 ;
	private int counts;
	
	private int classFragmentPos = 3;
	
	//未查看总数
	int noreadCount = 0;
	
	UserCache userCache = UserCache.getInstance();
	UserInfo userInfo = userCache.getUserInfo();
	
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
	
	
	
	public static NoticeClassFragment newInstance() {
		NoticeClassFragment newFragment = new NoticeClassFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		mView = inflater.inflate(R.layout.fragment_notice_list, container, false);
		return mView;
	}
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.edit, menu);
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		String classmaster = UserCache.getInstance().getCurrentTeacherRole().getClassmaster();
		MenuItem editNotice = menu.findItem(R.id.action_edit);
		if (userInfo instanceof Teacher) {
			if(classmaster!=null){
				 classmaster.equals(Classmaster.yes);
			}
			
			editNotice.setVisible(true);
		}
		else{
			editNotice.setVisible(false);
		}
	}
	
	
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
	
	/**
	 * 当被其他程序打断也会重新调用进入
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		ViewPager pager = (ViewPager)getActivity().findViewById(11);
		
		//pager.getCurrentItem();
		
		System.out.println("班级公告当前页..."+pager.getCurrentItem());
		//判断登陆用户角色
		if(userInfo instanceof Teacher){
			if(pager.getCurrentItem() == 2){
				currentPage = 1;
				if(mNoticeList!=null && mNoticeList.size() > 0){
					mNoticeList.clear();
				}
				initData();
			}
		}else{
			if(pager.getCurrentItem() == 1){
				currentPage = 1;
				if(mNoticeList!=null && mNoticeList.size() > 0){
					mNoticeList.clear();
				}
				initData();
			}
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
	
	/**
	 * 初始化数据，初始化界面
	 */
	private void initData() {
		lvNoticeList = (DropDownListView) mView.findViewById(R.id.lv_notice_list);
		mAdapter = new NoticeListAdapter(mView.getContext(), mNoticeList,classFragmentPos);
		lvNoticeList.setAdapter(mAdapter);

		lvNoticeList.setHeaderDefaultText("");
		
		refreshNoticeList();
		/**
		 * 下拉刷新数据
		 */
		lvNoticeList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				
				currentPage = 1;
				
				if(mNoticeList!=null && mNoticeList.size() > 0){
					mNoticeList.clear();
				}
				
				refreshNoticeList();
				
				
//				if(counts==0){
//					currentPage = currentPage +1;
//					refreshNoticeList();
//				}
//				else if(currentPage<(counts/10)+1){
//					currentPage = currentPage +1;
//					refreshNoticeList();
//				}
//				else{
//					lvNoticeList.setHasMore(false);
//					lvNoticeList.onDropDownComplete();
//					Toast.makeText(getActivity(), "没有更多信息", Toast.LENGTH_SHORT).show();
//				}
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
//					lvNoticeList.setHasMore(false);
					lvNoticeList.onBottomComplete();
					lvNoticeList.setFooterNoMoreText("没有更多信息");
//					Toast.makeText(getActivity(), "没有更多信息", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		
		
		initNoticeList();
	}

	private void initNoticeList() {
		try {
			//通知数据更新、刷新
			mAdapter.notifyDataSetChanged();
			lvNoticeList.setSecondPositionVisible();
			lvNoticeList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		}
	}
	
	/**
	 * 下拉动作、从服务器获取数据,解析成功后，插入本地数据库
	 */
	public void refreshNoticeList() {
		
		UserCache userCache = UserCache.getInstance();
		//封装参数
		QueryNoticeReq reqData = new QueryNoticeReq();
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setType(String.valueOf(NoticeQueryType.QUERY_CLASS));
		reqData.setReceiveobject(userCache.getCurrentClass().getClassId());
		reqData.setCurrentPage(this.currentPage);
		
		UserInfo userInfo = userCache.getUserInfo();
		if(userInfo instanceof Teacher){
			//老师
			reqData.setReceivetype(UserType.TEACHER);
		}
		else{
			//默认家长
			reqData.setReceivetype(UserType.PARENT);
		}

		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvNoticeList.onBottomComplete();
						lvNoticeList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvNoticeList.onBottomComplete();
						lvNoticeList.onDropDownComplete();
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
		mNoticeList.addAll(respData.getNoticeList());
		
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
		            intent.putExtra("classNoticeMsg", noreadCount); 
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
	
	/**
	 * 点击记录时，跳转到详细界面
	 */
//	@Override
//	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//		switch (parent.getId()) {
//		case R.id.lv_notice_list:
//			Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("noticeList", mNoticeList);
//			bundle.putInt("index", pos==0?pos:pos-1);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			break;
//		}
//	}
}
