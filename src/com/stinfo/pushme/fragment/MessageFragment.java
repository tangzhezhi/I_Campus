package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.ChatActivity;
import com.stinfo.pushme.activity.GroupChatActivity;
import com.stinfo.pushme.adapter.MessageAdapter;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.MessageGroupType;
import com.stinfo.pushme.common.AppConstant.MessageObjectType;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.MessageDBAdapter;
import com.stinfo.pushme.entity.Group;
import com.stinfo.pushme.entity.RecentUserMessage;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.util.PushUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * 消息界面
 * @author lenovo
 *
 */
public class MessageFragment extends Fragment {
	private static final String TAG = "MessageFragment";
	private ArrayList<RecentUserMessage> mUserMsgList = new ArrayList<RecentUserMessage>();
	private MessageAdapter mAdapter;
	private RefreshReceiver mReceiver = null;
	private ListView lvMsgList;
	private View mView;

	public static MessageFragment newInstance() {
		MessageFragment newFragment = new MessageFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerMyReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_message, container, false);
		initData();
		return mView;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.msg_edit, menu);
		MenuItem mitem = menu.findItem(R.id.action_msg_edit);
	
		mitem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Log.d(TAG, "点击了编辑消息按钮");
				
				return false;
			}
		});
		
	}
	
	
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
		}
	}
	
	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new RefreshReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(PushUtils.ACTION_REFRESH_RECENT);
			getActivity().registerReceiver(mReceiver, intentFilter);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initData() {
		lvMsgList = (ListView) mView.findViewById(R.id.lv_message_list);
		mAdapter = new MessageAdapter(mView.getContext(), mUserMsgList);
		lvMsgList.setAdapter(mAdapter);
//		lvMsgList.setOnItemClickListener(this);
		initUserMsgList();
	}
	
	/**
	 * 从本地数据库中读出最近的用户信息记录
	 */
	private void initUserMsgList() {
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			mUserMsgList.clear();
			mUserMsgList.addAll(dbAdapter.getRecentUserMessage());
			mAdapter.notifyDataSetChanged();
			refreshAllUnread();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

//	@Override
//	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//		switch (parent.getId()) {
//		case R.id.lv_message_list:
//			onChat(pos);
//			break;
//		}
//	}
	
	
	

	private void onChat(int pos) {
		RecentUserMessage userMsg = mUserMsgList.get(pos);
		updateUnreadCount(userMsg);
		if (userMsg.getGroupType() == MessageGroupType.PERSONAL) {
			onPersonalChat(userMsg);
		} else if (userMsg.getGroupType() == MessageGroupType.CLASS) {
			onClassChat(userMsg);
		}
	}

	private void onPersonalChat(RecentUserMessage userMsg) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userMsg.getObjectId());
		userInfo.setUserName(userMsg.getUserName());
		userInfo.setPicUrl(userMsg.getPicUrl());
		userInfo.setPhone(userMsg.getPhone());
		userInfo.setSex(userMsg.getSex());
		userInfo.setUservalue(userMsg.getUservalue());
		
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("userInfo", userInfo);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void onClassChat(RecentUserMessage userMsg) {
		String groupExt = "";
		if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.ALL))) {
			groupExt = "班级群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.TEACHER))) {
			groupExt = "老师群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.STUDENT))) {
			groupExt = "学生群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.PARENT))) {
			groupExt = "家长群";
		}
		
		String className = UserCache.getInstance().getClassName(userMsg.getObjectId());
		String groupName = className + groupExt;
		Group group = new Group(groupName, userMsg.getObjectId(), String.valueOf(userMsg.getObjectType()), userMsg.getGroupType());

		Intent intent = new Intent(getActivity(), GroupChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("group", group);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void updateUnreadCount(RecentUserMessage userMsg) {
		userMsg.setUnreadCount(0);
		mAdapter.notifyDataSetChanged();
		refreshAllUnread();
	}
	
	private void refreshAllUnread() {
		int unreadCount = 0;
		for (RecentUserMessage userMsg : mUserMsgList) {
			unreadCount += userMsg.getUnreadCount();
		}
		
		Intent intent = new Intent(PushUtils.ACTION_REFRESH_UNREAD);
		intent.putExtra(PushUtils.UNREAD_COUNT, unreadCount);
		getActivity().sendBroadcast(intent);
	}
	
	final class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent !=null){
				initUserMsgList();
			}
			
		}
	}
	
	
	
	
	
}
