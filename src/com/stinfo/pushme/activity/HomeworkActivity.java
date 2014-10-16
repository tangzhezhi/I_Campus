package com.stinfo.pushme.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.HomeworkListAdapter;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.HomeworkQueryType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Course;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryCourseReq;
import com.stinfo.pushme.rest.entity.QueryCourseResp;
import com.stinfo.pushme.rest.entity.QueryHomeworkReq;
import com.stinfo.pushme.rest.entity.QueryHomeworkResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;
import com.stinfo.pushme.view.DropDownListView.SildeDeleteListener;

public class HomeworkActivity extends BaseActionBarActivity implements OnNavigationListener,
		OnItemClickListener  {
	private static final String TAG = "HomeworkActivity";
	private ArrayList<Homework> mHomeworkList = new ArrayList<Homework>();
	private HomeworkListAdapter mAdapter;
	private String[] mItems = null;
	private String[] mItemsValue = null;
	
	private ArrayList<Course> mCourseList = new ArrayList<Course>();
	
	private DropDownListView lvHomeworkList;
	private int checkItem;
	
	public int getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(int checkItem) {
		this.checkItem = checkItem;
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
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homework);
		
		mItems =  getIntent().getStringArrayExtra("coursename");
		mItemsValue = getIntent().getStringArrayExtra("coursecode");
		
		initView();
		initData();
		inttDelBtn();
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
		
		if(mHomeworkList!=null && mHomeworkList.size() > 0){
			mHomeworkList.clear();
		}
		
		refreshHomeworkList();
		
	}
	
	private void initView() {
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.homework));
		bar.setDisplayHomeAsUpEnabled(true);
		
//		mItems = getResources().getStringArray(R.array.subject_list);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(adapter, this);
	}
	
	
	private void inttDelBtn() {
		lvHomeworkList.setFilpperDeleteListener(new SildeDeleteListener() {
            public void filpperOnclick(float xPosition, float yPosition) {
                    if (lvHomeworkList.getChildCount() == 0)
                            return;
                    // 根据坐标获得滑动删除的item的index
                    final int index = lvHomeworkList.pointToPosition((int) xPosition,
                                    (int) yPosition);
            }

            public void filpperDelete(float xPosition, float yPosition) {
                    // listview中要有item，否则返回
                    if (lvHomeworkList.getChildCount() == 0)
                            return;
                    // 根据坐标获得滑动删除的item的index
                    final int index = lvHomeworkList.pointToPosition((int) xPosition,
                                    (int) yPosition);
                    // 一下两步是获得该条目在屏幕显示中的相对位置，直接根据index删除会空指針异常。因为listview中的child只有当前在屏幕中显示的才不会为空
                    int firstVisiblePostion = lvHomeworkList.getFirstVisiblePosition();
                    View view = lvHomeworkList.getChildAt(index - firstVisiblePostion);
                    view.findViewById(R.id.homework_del).setVisibility(View.VISIBLE);
                    
            }
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		if (!(UserCache.getInstance().getUserInfo() instanceof Teacher)) {
			MenuItem editHomework = menu.findItem(R.id.action_edit);
			editHomework.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditHomework();
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

	public void onEditHomework() {
		Intent intent = new Intent(this, PostHomeworkActivity.class);
		intent.putExtra("coursename", mItems);   
		intent.putExtra("coursecode", mItemsValue);
		startActivity(intent);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    	Log.v(TAG, "我点击了导航条选项:类型是: " + mItems[itemPosition]);
    	checkItem = itemPosition;
    	
    	if(checkItem!=0){
    		selectHomeworkSubjectList(itemPosition);
    	}
        return false;  
	}

	private void initData() {
		
		this.counts = 0;
		this.currentPage = 1;
		
		lvHomeworkList = (DropDownListView) findViewById(R.id.lv_homework);
		mAdapter = new HomeworkListAdapter(this, mHomeworkList);
		lvHomeworkList.setAdapter(mAdapter);
		
		lvHomeworkList.setOnItemClickListener(this);
		lvHomeworkList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
//				if(counts==0){
//					currentPage = currentPage +1;
//					refreshHomeworkList();
//				}
//				else if(currentPage<(counts/10)+1){
//					currentPage = currentPage +1;
//					refreshHomeworkList();
//				}
//				else{
//					lvHomeworkList.setHasMore(false);
//					lvHomeworkList.onDropDownComplete();
//				}
				
				currentPage = 1;
				if(mHomeworkList!=null && mHomeworkList.size() > 0){
					mHomeworkList.clear();
				}
				refreshHomeworkList();
				
			}
		});
		
		
		lvHomeworkList.setOnBottomListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(counts==0){
					currentPage = currentPage +1;
					refreshHomeworkList();
				}
				else if(currentPage<(counts/10)+1){
					currentPage = currentPage +1;
					refreshHomeworkList();
				}
				else{
					lvHomeworkList.onBottomComplete();
					lvHomeworkList.setFooterNoMoreText("没有更多信息");
				}
			}
			
		});

		initHomeworkList();
	}

	private void initHomeworkList() {
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		try {
			mAdapter.notifyDataSetChanged();
			lvHomeworkList.setSecondPositionVisible();
			lvHomeworkList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} 
	}
	
	
	/**
	 * 选择了科目类型
	 * @param itemid
	 */
	private void selectHomeworkSubjectList(int itemid) {
		mHomeworkList.clear();
		try {
			this.counts = 0;
			this.currentPage = 1;
			refreshHomeworkList();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		}
	}

	public void refreshHomeworkList() {
		UserCache userCache = UserCache.getInstance();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		QueryHomeworkReq reqData = new QueryHomeworkReq();
		String createTime = "0";
		String subject = "";
		
		if(userInfo instanceof Teacher){
			reqData.setUserId(userCache.getUserInfo().getUserId());
		}
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassid(userCache.getCurrentClass().getClassId());
		reqData.setCurrentPage(this.currentPage);
		
		if(checkItem!=0){
			subject = mItemsValue[checkItem];
		}
		
		reqData.setSubject(subject);
		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvHomeworkList.onDropDownComplete();
						lvHomeworkList.onBottomComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvHomeworkList.onDropDownComplete();
						lvHomeworkList.onBottomComplete();
						MessageBox.showServerError(HomeworkActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryHomeworkResp respData = new QueryHomeworkResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				this.counts = respData.getCount();
				
				if(this.counts < 10){
					this.currentPage = 1;
				}
				
				doSuccess(respData);
			} else if (respData.getAck() != Ack.SUCCESS) {
				MessageBox.showAckError(HomeworkActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(HomeworkActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void doSuccess(QueryHomeworkResp respData) {
		mHomeworkList.addAll(respData.getHomeworkList());
		
		if(mItems!=null && mItems.length > 0){
//			bundle.putStringArray("courseName", mItems);
//			bundle.putStringArray("courseCode", mItemsValue);
			
			for(Homework homework :mHomeworkList){
				
				for(int i = 0 ; i < mItemsValue.length; i++){
					if(homework.getSubject().equals(mItemsValue[i])){
						homework.setSubjectName(mItems[i]);
					}
				}
			}
		}
		
		mAdapter.notifyDataSetChanged();
		
		lvHomeworkList.onDropDownComplete();
		lvHomeworkList.onBottomComplete();
		if(currentPage>1){
			lvHomeworkList.setSelection((currentPage-1)*10);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_homework:
			Intent intent = new Intent(this, HomeworkDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("homeworkList", mHomeworkList);
			bundle.putInt("index", pos==0?pos:pos-1);
			
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}

	
}
