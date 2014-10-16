package com.stinfo.pushme.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.UserInfoActivity;
import com.stinfo.pushme.adapter.UserListAdapter;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserComparator;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.view.IndexableListView;

public final class TeacherListFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "TeacherListFragment";
	private ArrayList<UserInfo> mUserList = new ArrayList<UserInfo>();
	private UserListAdapter mAdapter;
	private IndexableListView lvUserList;
	private View mView;

	public static TeacherListFragment newInstance() {
		TeacherListFragment newFragment = new TeacherListFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_user_list, container, false);
		initData();
		return mView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onUserFilter(String searchText) {
		mAdapter.getFilter().filter(searchText);
	}

	private void initData() {
		mUserList.clear();
		lvUserList = (IndexableListView) mView.findViewById(R.id.lv_user_list);
		mAdapter = new UserListAdapter(mView.getContext(), mUserList,UserListAdapter.NORMAL_MODE);
		lvUserList.setAdapter(mAdapter);
		lvUserList.setFastScrollEnabled(true);
		lvUserList.setTextFilterEnabled(true);
		lvUserList.setOnItemClickListener(this);
		initUserList();
	}

	private void initUserList() {
		DBAdapter dbAdapter = new DBAdapter();
		UserCache userCache = UserCache.getInstance();
		ClassInfo classInfo = userCache.getCurrentClass();

		try {
			dbAdapter.open();
			mUserList.clear();
			if (userCache.getUserInfo() instanceof Teacher) {
				mUserList.addAll(dbAdapter.getSchoolTeacherRoster(classInfo.getSchoolId()));
			} else {
				mUserList.addAll(dbAdapter.getClassTeacherRoster(classInfo.getClassId()));
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
		switch (parent.getId()) {
		case R.id.lv_user_list:
			Intent intent = new Intent(getActivity(), UserInfoActivity.class);
			Bundle bundle = new Bundle();
			UserInfo userInfo =  (UserInfo) mAdapter.getItem(pos);
			bundle.putSerializable("userInfo", userInfo);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
}
