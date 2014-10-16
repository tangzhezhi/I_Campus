package com.stinfo.pushme.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.DailyListAdapter;
import com.stinfo.pushme.common.AppConstant.DailyQueryType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Daily;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryDailyReq;
import com.stinfo.pushme.rest.entity.QueryDailyResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

public class DailyActivity extends BaseActionBarActivity implements OnItemClickListener {
	private static final String TAG = "DailyActivity";
	private ArrayList<Daily> mDailyList = new ArrayList<Daily>();
	private DailyListAdapter mAdapter;
	private DropDownListView lvDailyList;
	
	
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
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		currentPage = 1;
		
		if(mDailyList!=null && mDailyList.size() > 0){
			mDailyList.clear();
		}
		
		refreshDailyList();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		initView();
		initData();
	}

	private void initView() {
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.daily));
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		if (!(UserCache.getInstance().getUserInfo() instanceof Teacher)) {
			MenuItem editDaily = menu.findItem(R.id.action_edit);
			editDaily.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditDaily();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	@Override
	public void onPause() {
		RequestController.getInstance().cancelPendingRequests(TAG);
		super.onPause();
	}

	public void onEditDaily() {
		Intent intent = new Intent(this, PostDailyActivity.class);
		startActivity(intent);
	}

	private void initData() {
		lvDailyList = (DropDownListView) findViewById(R.id.lv_daily);
		mAdapter = new DailyListAdapter(this, mDailyList);
		lvDailyList.setAdapter(mAdapter);

		lvDailyList.setOnItemClickListener(this);
		lvDailyList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				
				currentPage = 1;
				if(mDailyList!=null && mDailyList.size() > 0){
					mDailyList.clear();
				}
				refreshDailyList();
			}
		});
		
		
		lvDailyList.setOnBottomListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					refreshDailyList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshDailyList();
				}
				else{
					lvDailyList.onBottomComplete();
					lvDailyList.setFooterNoMoreText("没有更多信息");
//					Toast.makeText(DailyActivity.this, "没有更多信息", Toast.LENGTH_SHORT).show();
				}
			}
			
		});

		initDailyList();
	}

	private void initDailyList() {
		try {
			mAdapter.notifyDataSetChanged();
			lvDailyList.setSecondPositionVisible();
			lvDailyList.onDropDownComplete();
			lvDailyList.onBottomComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} 
	}

	public void refreshDailyList() {
		UserCache userCache = UserCache.getInstance();
		QueryDailyReq reqData = new QueryDailyReq();
		
		reqData.setSessionKey(userCache.getSessionKey());
		
		if (userCache.getUserInfo() instanceof Teacher) {
			reqData.setUserId(userCache.getUserInfo().getUserId());
		} 
		else if (userCache.getUserInfo() instanceof Parent){
			reqData.setStudentid(userCache.getCurrentChild().getUservalue());
		}
		else if (userCache.getUserInfo() instanceof Student){
			reqData.setStudentid(userCache.getUserInfo().getUservalue());
		}
		else{
			Log.e(TAG, "不存在此类型");
		}
		reqData.setCurrentPage(this.currentPage);
		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvDailyList.onDropDownComplete();
						lvDailyList.onBottomComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvDailyList.onDropDownComplete();
						lvDailyList.onBottomComplete();
						MessageBox.showServerError(DailyActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryDailyResp respData = new QueryDailyResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				
				if(this.counts < 10){
					this.currentPage = 1;
				}
				
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(DailyActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(DailyActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void doSuccess(QueryDailyResp respData) {
		mDailyList.addAll(respData.getDailyList());
		mAdapter.notifyDataSetChanged();
		lvDailyList.onDropDownComplete();
		lvDailyList.onBottomComplete();
		if(currentPage>1){
			lvDailyList.setSelection((currentPage-1)*10);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_daily:
			Intent intent = new Intent(this, DailyDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("daily", mDailyList.get(pos==0?pos:pos-1));
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
}
