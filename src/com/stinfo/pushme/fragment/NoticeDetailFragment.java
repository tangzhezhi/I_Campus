package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.PostNoticeActivity;
import com.stinfo.pushme.adapter.NoticeListAdapter;
import com.stinfo.pushme.adapter.NoticeReplyListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.NoticeQueryType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.NoticeReply;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PostNoticeReplyReq;
import com.stinfo.pushme.rest.entity.QueryNoticeReplyReq;
import com.stinfo.pushme.rest.entity.QueryNoticeReplyResp;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryNoticeResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

public final class NoticeDetailFragment extends Fragment {
	private String TAG = NoticeDetailFragment.class.getName();
	private Notice mNotice = null;
	private Boolean isReply = false;
	private EditText etReply = null;
	private Button btnReply = null;
	
	private DropDownListView lvNoticeReplyDetailList;
	private NoticeReplyListAdapter mAdapter;
	private int currentPage = 0 ;
	private int counts;
	
	private View mView;
	
	private int schoolFragmentPos = 1;
	
	private ArrayList<NoticeReply> mNoticeReplayList = new ArrayList<NoticeReply>();
	
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
	
	
	public static NoticeDetailFragment newInstance(Notice notice) {
		NoticeDetailFragment newFragment = new NoticeDetailFragment();
		Bundle bundle = new Bundle();

		bundle.putSerializable("notice", notice);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	
	
	public static NoticeDetailFragment newInstance(Notice notice,Boolean isReply) {
		NoticeDetailFragment newFragment = new NoticeDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("notice", notice);
		bundle.putBoolean("replay", isReply);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mNotice = (Notice) bundle.getSerializable("notice");
			isReply = bundle.getBoolean("replay");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_notice_detail, container, false);
		TextView tvAuthor = (TextView) mView.findViewById(R.id.tv_author);
		TextView tvPublishTime = (TextView) mView.findViewById(R.id.tv_publish_time);
		TextView tvTitle = (TextView) mView.findViewById(R.id.tv_title);
		TextView tvContent = (TextView) mView.findViewById(R.id.tv_content);
		
		//颜色渐变
    	int[] colors = { Color.RED,Color.YELLOW,Color.GREEN,Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN };   
		
		etReply =  (EditText) mView.findViewById(R.id.et_reply_content);
		
		lvNoticeReplyDetailList =  (DropDownListView) mView.findViewById(R.id.lv_notice_replay_list);
		
		if (mNotice != null) {
			tvAuthor.setText(mNotice.getAuthorName());
			tvPublishTime.setText(mNotice.getCreateTime());
			tvTitle.setText(mNotice.getTitle());
			tvContent.setText(mNotice.getContent());
			
			if(isReply){
				etReply.setFocusable(true);
			}
			
			btnReply = (Button) mView.findViewById(R.id.btn_reply);
			
			btnReply.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(etReply.getText().length() < 1){
						MessageBox.showMessage(getActivity(), "请填写回复信息");
					}
					else{
						sendReply();
					}
				}
			});
			
		}
		return mView;
	}
	
	
	
	
	private void initReplyData() {
		lvNoticeReplyDetailList = (DropDownListView) mView.findViewById(R.id.lv_notice_replay_list);
		mAdapter = new NoticeReplyListAdapter(mView.getContext(), mNoticeReplayList,schoolFragmentPos);
		lvNoticeReplyDetailList.setAdapter(mAdapter);
		
		refreshNoticeReplyList();
		
		lvNoticeReplyDetailList.setHeaderDefaultText("");
		lvNoticeReplyDetailList.setFooterDefaultText("");
		
		lvNoticeReplyDetailList.setOnBottomListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					refreshNoticeReplyList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshNoticeReplyList();
				}
				else{
					lvNoticeReplyDetailList.onBottomComplete();
					lvNoticeReplyDetailList.setFooterNoMoreText("没有更多信息");
//					Toast.makeText(getActivity(), "没有更多信息", Toast.LENGTH_SHORT).show();
				}
			}
			
		});

	}
	
	
	
	/**
	 * 获取回复数据
	 */
	private void refreshNoticeReplyList() {
		UserCache userCache = UserCache.getInstance();
		QueryNoticeReplyReq reqData = new QueryNoticeReplyReq();
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setCurrentPage(this.currentPage);
		reqData.setNoticeId(mNotice.getObjectId());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkReplyResponse(response);
						lvNoticeReplyDetailList.onBottomComplete();
						lvNoticeReplyDetailList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvNoticeReplyDetailList.onBottomComplete();
						lvNoticeReplyDetailList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkReplyResponse(String response) {
		try {
			QueryNoticeReplyResp respData = new QueryNoticeReplyResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				if(this.counts < 10){
					this.currentPage = 1;
				}
				doReplySuccess(respData);
			} else if (respData.getAck() != Ack.NOT_FOUND) {
				MessageBox.showAckError(mView.getContext(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	
	

	private void doReplySuccess(QueryNoticeReplyResp respData) {
		mNoticeReplayList.addAll(respData.getNoticeReplyList());
		//将详情存入本地
//		if(mNoticeReplayList!=null && mNoticeReplayList.size() > 0){
//			DBAdapter dbAdapter = new DBAdapter();
//			try {
//				dbAdapter.open();
//				dbAdapter.addNoticeDetail(mNoticeReplayList);
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//			finally {
//				dbAdapter.close();
//			}
//				
//		}
		mAdapter.notifyDataSetChanged();
		lvNoticeReplyDetailList.onBottomComplete();
		if(currentPage>1){
			lvNoticeReplyDetailList.setSelection((currentPage-1)*10);
		}
	}

	private void sendReply(){
		UserCache userCache = UserCache.getInstance();
		PostNoticeReplyReq reqData = new PostNoticeReplyReq();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setAuthorid(userInfo.getUserId());
		reqData.setNoticeId(mNotice.getObjectId());
		reqData.setContent(etReply.getText().toString());

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
			if(response!=null && response.length() > 0){
				JSONObject rootObj = new JSONObject(response);
				int ack = rootObj.getInt("ack");
				if (ack == Ack.SUCCESS) {
					etReply.setText("");
					
					etReply.clearFocus();
					
					InputMethodManager imm = (InputMethodManager) etReply.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
			            imm.hideSoftInputFromWindow(etReply.getWindowToken(), 0);
					
					MessageBox.showMessage(getActivity(), "提交成功！");
					
					if(mNoticeReplayList!=null && mNoticeReplayList.size() > 0){
						mNoticeReplayList.clear();
					}
					
					refreshNoticeReplyList();
					
				} else if (ack != Ack.NOT_FOUND) {
					MessageBox.showAckError(getActivity(), ack);
				}
			}
		} catch (Exception e) {
			MessageBox.showParserError(getActivity());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		currentPage = 1;
		if(mNoticeReplayList!=null && mNoticeReplayList.size() > 0){
			mNoticeReplayList.clear();
		}
		initReplyData();
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
}
