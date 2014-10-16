package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.adapter.UserListAdapter;
import com.stinfo.pushme.adapter.UserListAdapter.ViewHolder;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserComparator;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.view.IndexableListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UserSelectActivity extends BaseActionBarActivity implements OnItemClickListener,
		SearchView.OnQueryTextListener {
	private static final String TAG = "UserSelectActivity";
	private ArrayList<UserInfo> mUserList = new ArrayList<UserInfo>();
	private ArrayList<UserInfo> mUserListSelect = new ArrayList<UserInfo>();
	private HashMap<String, String> mSelectMap = new HashMap<String, String>();
	private UserListAdapter mAdapter;
	private MenuItem mSearchItem;
	private IndexableListView lvUserList;
	private boolean mSelectAll = false;
	private String type = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select);
		
		Intent intent1 = getIntent();
		
		if(intent1!=null && !intent1.getStringExtra("type").equals("")){
			type = intent1.getStringExtra("type");
		}
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.user_select));
		bar.setDisplayHomeAsUpEnabled(true);
		initData(type);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.user_select, menu);
		mSearchItem = menu.findItem(R.id.user_select_action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
		searchView.setOnQueryTextListener(this);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.action_user_select:
			onSelect();
			break;
		case R.id.action_user_select_complete:
			onUserSelectComplete();
			break;
		case R.id.action_user_select_cancel:
			onUnSelect();
			break;
		}

		return true;
	}

	private void onUserSelectComplete() {
		for(int i = 0 ; i < mUserList.size();i++){
			if(mAdapter.getIsSelected().get(i)!=null && mAdapter.getIsSelected().get(i)==true){
				mUserListSelect.add(mUserList.get(i));
			}
		}
		finish() ;
	}

	private void onSelect() {
		HashMap<Integer,Boolean> hm = new HashMap<Integer,Boolean>();
		for(int i = 0 ; i <  mUserList.size(); i++){
			hm.put(i, true);
		}
		mAdapter.setIsSelected(hm);
		mAdapter.notifyDataSetChanged();
	}
	
	private void onUnSelect() {
		HashMap<Integer,Boolean> hm = new HashMap<Integer,Boolean>();
		for(int i = 0 ; i <  mUserList.size(); i++){
			hm.put(i, false);
		}
		mAdapter.setIsSelected(hm);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void finish() {
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("selectList", mUserListSelect);
		intent.putExtras(bundle);
		if(("chatGroup").equals(type)){
			setResult(2, intent);
		}
		else {
			setResult(1, intent);
		}
		super.finish();
	}

	private void initData(String type) {
		mUserList.clear();
		lvUserList = (IndexableListView) findViewById(R.id.lv_user_list);
		initUserList(type);
		mAdapter = new UserListAdapter(this, mUserList, UserListAdapter.SELECT_MODE);
		lvUserList.setAdapter(mAdapter);
		lvUserList.setFastScrollEnabled(true);
		lvUserList.setTextFilterEnabled(true);
		lvUserList.setOnItemClickListener(this);
	}

	private void initUserList(String type) {
		DBAdapter dbAdapter = new DBAdapter();
		UserCache userCache = UserCache.getInstance();
		ClassInfo classInfo = userCache.getCurrentClass();
		
		UserInfo userInfo = userCache.getUserInfo();
		
		
		try {
			dbAdapter.open();
			mUserList.clear();
			
			if(type.equals("daily")){
				mUserList.addAll(dbAdapter.getClassParentRoster(classInfo.getClassId()));
			}
			else if(type.equals("teacherNotice")){
				mUserList.addAll(dbAdapter.getSchoolTeacherRoster(classInfo.getSchoolId()));
			}
			else if(type.equals("chatGroup")){
				if(userInfo instanceof Teacher){
					mUserList.addAll(dbAdapter.getSchoolTeacherRoster(classInfo.getSchoolId()));
					mUserList.addAll(dbAdapter.getClassParentRoster(userCache.getCurrentClass().getClassId()));
				}
				else if(userInfo instanceof Parent){
					mUserList.addAll(dbAdapter.getClassTeacherRoster(userCache.getCurrentClass().getClassId()));
					mUserList.addAll(dbAdapter.getClassParentRoster(userCache.getCurrentClass().getClassId()));
				}
			}
			
			Collections.sort(mUserList, new UserComparator());
			mAdapter.updateListData(mUserList);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		HashMap<Integer,Boolean> hm = new HashMap<Integer,Boolean>();
		switch (parent.getId()) {
		case R.id.lv_user_list:
            // 改变CheckBox的状态
			ViewHolder holder = (ViewHolder) v.getTag();
			holder.getCbSelect().toggle();
			hm = mAdapter.getIsSelected();
			if (holder.getCbSelect().isChecked() == true) {
				hm.put(pos, true);
            } 
			else {
				hm.put(pos, false);
			}
			mAdapter.setIsSelected(hm);
			mAdapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		mAdapter.getFilter().filter(arg0);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
	
	
}
