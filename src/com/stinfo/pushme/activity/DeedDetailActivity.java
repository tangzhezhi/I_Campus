package com.stinfo.pushme.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.DeedReplyListAdapter;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.DeedType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.DeedDetail;
import com.stinfo.pushme.entity.NoticeReply;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PostDeedReplyReq;
import com.stinfo.pushme.rest.entity.QueryDeedDetailReq;
import com.stinfo.pushme.rest.entity.QueryDeedDetailResp;
import com.stinfo.pushme.rest.entity.QueryDeedReplyReq;
import com.stinfo.pushme.rest.entity.QueryDeedReplyResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.URLChecker;
import com.stinfo.pushme.view.DropDownListView;

public class DeedDetailActivity extends BaseActionBarActivity {
	private static final String TAG = "DeedDetailActivity";
	
	private int currentPage = 0 ;
	private int counts;
	private TextView tvContent;
	private TextView tvTitle;
	private TextView tvPublisher;
	private TextView tvPublishDate;
	private String behaviorid = "";
	private String plates = "";
	private DeedDetail deedDetail = new DeedDetail();
	
	private DropDownListView lvDeedReplyDetailList;
	private DeedReplyListAdapter mAdapter;
	private EditText etReply = null;
	private Button btnReply = null;
	
	private LinearLayout deedDetailLinearLayout ;
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
		return false;
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
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getIntent()!=null){
			behaviorid = getIntent().getStringExtra("behaviorid");
			plates = getIntent().getStringExtra("plates");
		}
		
		setContentView(R.layout.activity_deed_detail);
//		mView = getWindow().getDecorView().findViewById(android.R.id.content);
		ActionBar bar = getSupportActionBar();
		if(plates.equals(DeedType.good)){
			bar.setTitle(getResources().getString(R.string.good_deed_show));
		}else if(plates.equals(DeedType.bad)){
			bar.setTitle(getResources().getString(R.string.bad_deed_show));
		}else if(plates.equals(DeedType.lover_share)){
			bar.setTitle(getResources().getString(R.string.love_share));
		}
		
		bar.setDisplayHomeAsUpEnabled(true);

		initView();
		
		initData();
		
		getDeedDetail() ;
	}
	
	
	private void initData() {
		tvTitle.setText(deedDetail.getTitle());
		tvContent.setText(deedDetail.getContent());
		tvPublisher.setText(deedDetail.getReleaseusername());
		tvPublishDate.setText(deedDetail.getReleasedate());
		
		if(deedDetail.getPics()!=null && deedDetail.getPics().size() > 0){
			
			for(int i = 0 ; i < deedDetail.getPics().size();i++ ){
				ImageView item1 = new ImageView(this);
				item1.setId(i);
				String picTemp = deedDetail.getPics().get(i);
				
				LinearLayout.LayoutParams lp = 
						new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  
				lp.setMargins(10, 10, 30, 30);
				lp.gravity = Gravity.CENTER_VERTICAL;  
				
				item1.setLayoutParams(lp);
				
				if(picTemp!=null && !picTemp.equals("")){
					if (URLChecker.isUrl(AppConstant.School_Platform_Photo_QUERY_URL+picTemp)) {
						Log.d(TAG, "loadImage: " + AppConstant.School_Platform_Photo_QUERY_URL+picTemp);
						ImageListener listener = ImageLoader.getImageListener(item1,
								R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
						ImageCacheManager.getInstance().getImageLoader().get(AppConstant.School_Platform_Photo_QUERY_URL+picTemp, listener);
					}
				}
				deedDetailLinearLayout.addView(item1);
			}
		}
	}

	private void initView() {
		deedDetailLinearLayout = (LinearLayout) findViewById(R.id.layout_detail);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvPublisher = (TextView) findViewById(R.id.tv_author);
		tvPublishDate = (TextView) findViewById(R.id.tv_publish_time);
		etReply =  (EditText)findViewById(R.id.et_reply_content);
		btnReply = (Button) findViewById(R.id.btn_reply);
		
		btnReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(etReply.getText().length() < 1){
					MessageBox.showMessage(DeedDetailActivity.this, "请填写回复信息");
				}
				else{
					sendReply();
				}
			}
		});
	}
	
	
	private void sendReply(){
		UserCache userCache = UserCache.getInstance();
		PostDeedReplyReq reqData = new PostDeedReplyReq();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setCoperid(userInfo.getUserId());
		reqData.setCusername(userInfo.getUserName());
		reqData.setBehaviorid(behaviorid);
		reqData.setReplyoperid(deedDetail.getReleaseoperid());
		reqData.setReplyusername(deedDetail.getReleaseusername());
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
						MessageBox.showServerError(DeedDetailActivity.this);
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
					
					MessageBox.showMessage(DeedDetailActivity.this, "提交成功！");
					
					if(mNoticeReplayList!=null && mNoticeReplayList.size() > 0){
						mNoticeReplayList.clear();
					}
					
					refreshDeedReplyList();
					
				} else if (ack != Ack.NOT_FOUND) {
					MessageBox.showAckError(DeedDetailActivity.this, ack);
				}
			}
		} catch (Exception e) {
			MessageBox.showParserError(DeedDetailActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	
	private void initReplyData() {
		lvDeedReplyDetailList = (DropDownListView) findViewById(R.id.lv_replay_list);
		mAdapter = new DeedReplyListAdapter(DeedDetailActivity.this, mNoticeReplayList);
		
		lvDeedReplyDetailList.setAdapter(mAdapter);
		
		refreshDeedReplyList();
		
		lvDeedReplyDetailList.setHeaderDefaultText("");
		lvDeedReplyDetailList.setFooterDefaultText("");
		
		lvDeedReplyDetailList.setOnBottomListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					refreshDeedReplyList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshDeedReplyList();
				}
				else{
					lvDeedReplyDetailList.onBottomComplete();
					lvDeedReplyDetailList.setFooterNoMoreText("没有更多信息");
				}
			}
			
		});

	}
	
	
	
	/**
	 * 获取回复数据
	 */
	private void refreshDeedReplyList() {
		UserCache userCache = UserCache.getInstance();
		QueryDeedReplyReq reqData = new QueryDeedReplyReq();
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setCurrentPage(this.currentPage);
		reqData.setBehaviorid(behaviorid);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkReplyResponse(response);
						lvDeedReplyDetailList.onBottomComplete();
						lvDeedReplyDetailList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvDeedReplyDetailList.onBottomComplete();
						lvDeedReplyDetailList.onDropDownComplete();
						MessageBox.showServerError(DeedDetailActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkReplyResponse(String response) {
		try {
			QueryDeedReplyResp respData = new QueryDeedReplyResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				if(this.counts < 10){
					this.currentPage = 1;
				}
				doReplySuccess(respData);
			} else {
				MessageBox.showAckError(DeedDetailActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(DeedDetailActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	
	

	private void doReplySuccess(QueryDeedReplyResp respData) {
		mNoticeReplayList.addAll(respData.getNoticeReplyList());
		
		
		int totalHeight = 0; 
		    for (int i = 0; i < mAdapter.getCount(); i++) { 
		        View listItem = mAdapter.getView(i, null, lvDeedReplyDetailList); 
		        listItem.measure(0, 0); 
		        totalHeight += listItem.getMeasuredHeight(); 
		    } 

	    ViewGroup.LayoutParams params = lvDeedReplyDetailList.getLayoutParams(); 
	    params.height = totalHeight + (lvDeedReplyDetailList.getDividerHeight() * (lvDeedReplyDetailList.getCount() - 1)); 
	    lvDeedReplyDetailList.setLayoutParams(params); 
		
		
		mAdapter.notifyDataSetChanged();
		lvDeedReplyDetailList.onBottomComplete();
		if(currentPage>1){
			lvDeedReplyDetailList.setSelection((currentPage-1)*10);
		}
	}
	
	
	/**
	 * 获取详情
	 */
	private void getDeedDetail() {
		UserCache userCache = UserCache.getInstance();
		QueryDeedDetailReq reqData = new QueryDeedDetailReq();
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setBehaviorid(behaviorid);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						
						try {
							QueryDeedDetailResp respData = new QueryDeedDetailResp(response);
							if (respData.getAck() == Ack.SUCCESS) {
								if( respData.getDeedDetail()!=null){
									deedDetail = respData.getDeedDetail();
									initData();
								}
								
							} else
							{
								MessageBox.showAckError(DeedDetailActivity.this, respData.getAck());
							}
						} catch (Exception e) {
							MessageBox.showParserError(DeedDetailActivity.this);
							Log.e(TAG, "Failed to parser response data! \r\n" + e);
						}
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(DeedDetailActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	

	@Override
	public void onResume() {
		super.onResume();
		
		initData();
		
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
		this.finish();
	}
	
	
	
	
}
