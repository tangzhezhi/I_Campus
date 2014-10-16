package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.MsgTemplateListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.MsgTemplate;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryMsgTemplateReq;
import com.stinfo.pushme.rest.entity.QueryMsgTemplateResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

/**
 * @author lenovo
 *
 */
public final class MsgTemplateListFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "MsgTemplateListFragment";
	private ArrayList<MsgTemplate> mMsgTemplateList = new ArrayList<MsgTemplate>();
	private MsgTemplateListAdapter mAdapter;
	
	//同时实现下拉刷新及滑动到底部加载更多的ListView
	private DropDownListView lvMsgTemplateList;
	private View mView;
	private String schoolNo;
	private String groupCode;
	
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
	
	
	public static MsgTemplateListFragment newInstance() {
		MsgTemplateListFragment newFragment = new MsgTemplateListFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_notice_list, container, false);
		return mView;
	}

	/**
	 * 当被其他程序打断也会重新调用进入
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		currentPage = 0;
		
		if(mMsgTemplateList!=null && mMsgTemplateList.size() > 0){
			mMsgTemplateList.clear();
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
		Intent intent = getActivity().getIntent();
		if(intent!=null){
			schoolNo = intent.getStringExtra("schoolNo");
			groupCode = intent.getStringExtra("groupCode");
		}
		
		lvMsgTemplateList = (DropDownListView) mView.findViewById(R.id.lv_notice_list);
		mAdapter = new MsgTemplateListAdapter(mView.getContext(), mMsgTemplateList);
		lvMsgTemplateList.setAdapter(mAdapter);

		lvMsgTemplateList.setOnItemClickListener(this);
		
		refreshMsgTemplateList();
		/**
		 * 下拉刷新数据
		 */
		lvMsgTemplateList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				if(counts==0){
					currentPage = currentPage +1;
					refreshMsgTemplateList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshMsgTemplateList();
				}
				else{
					lvMsgTemplateList.setHasMore(false);
					lvMsgTemplateList.onDropDownComplete();
					Toast.makeText(getActivity(), "没有更多信息", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
		});

	}
	
	/**
	 * 下拉动作、从服务器获取数据,解析成功后，插入本地数据库
	 */
	public void refreshMsgTemplateList() {
		UserCache userCache = UserCache.getInstance();
		//封装参数
		QueryMsgTemplateReq reqData = new QueryMsgTemplateReq();
		String createTime = "0";
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setGroupCode(groupCode);
		reqData.setSchoolNo(schoolNo);
		reqData.setCurrentPage(String.valueOf(this.currentPage));

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvMsgTemplateList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvMsgTemplateList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryMsgTemplateResp respData = new QueryMsgTemplateResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(mView.getContext(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void doSuccess(QueryMsgTemplateResp respData) {
		mMsgTemplateList.addAll(0, respData.getMsgTemplateList());
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 点击记录时，跳转到详细界面
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_notice_list:
			
			  // 改变CheckBox的状态
			MsgTemplateListAdapter.ViewHolder holder = (MsgTemplateListAdapter.ViewHolder) v.getTag();
			
			Intent intent = new Intent();
			
			intent.putExtra("msgtemplate", holder.getTvContent().getText());
			getActivity().setResult(1, intent);
			getActivity().finish();
			
		}
	}
}
