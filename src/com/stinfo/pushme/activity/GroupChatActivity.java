package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.common.AppConstant.MessageObjectType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.MessageSendStatus;
import com.stinfo.pushme.common.AppConstant.MessageType;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Group;
import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PushGroupMessageReq;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.util.URLChecker;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GroupChatActivity extends BaseActionBarActivity implements OnClickListener {
	private final static String TAG = "GroupChatActivity";
	private ArrayList<Message> mMsgList = new ArrayList<Message>();
	private HashMap<String, UserInfo> mCacheUser = new HashMap<String, UserInfo>();
	private LayoutInflater mInflater;
	private Group mGroup = null;
	private GroupChatListAdapter mAdapter;
	private InternalMessageReceiver mReceiver = null;
	private ListView lvChatList;
	private EditText etContent;
	private Button btnSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		mCacheUser.clear();

		initView();
		handleIntent(getIntent());
		registerMyReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		mGroup = (Group) intent.getSerializableExtra("group");
		ActionBar bar = getSupportActionBar();
		bar.setTitle(mGroup.getGroupName());

		initData();
		clearUnreadCount();
	}

	private void clearUnreadCount() {
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.clearUnreadCount(mGroup.getGroupId(), mGroup.getObjectType(), mGroup.getGroupType());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
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
	public void finish() {
		super.finish();
		refreshRecentMessage();
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			onSend();
			break;
		}
	}

	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new InternalMessageReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(PushUtils.ACTION_INTERNAL_MESSAGE);
			intentFilter.setPriority(999);
			registerReceiver(mReceiver, intentFilter);
		}
	}

	private void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		etContent = (EditText) findViewById(R.id.et_send_content);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);

		lvChatList = (ListView) findViewById(R.id.lv_chat_list);
		mAdapter = new GroupChatListAdapter();
		lvChatList.setAdapter(mAdapter);
	}

	private void initData() {
		DBAdapter dbAdapter = new DBAdapter();
		mMsgList.clear();

		try {
			dbAdapter.open();
			mMsgList.addAll(dbAdapter.getGroupMessage(mGroup));
			mAdapter.notifyDataSetChanged();
			lvChatList.setSelection(mAdapter.getCount() - 1);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	private void onSend() {
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			MessageBox.showLongMessage(this, "请输入消息！");
			return;
		}

		Message message = initMessage(content);
		mMsgList.add(message);
		mAdapter.notifyDataSetChanged();
		lvChatList.setSelection(mAdapter.getCount() - 1);

		sendMessage(message);
		etContent.setText("");
	}

	private void sendMessage(final Message message) {
		UserCache userCache = UserCache.getInstance();
		PushGroupMessageReq reqData = new PushGroupMessageReq();

		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setOperator(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		
		String receiveType = "";
		
		if(mGroup.getObjectType().equals(String.valueOf(MessageObjectType.ALL))){
			receiveType = UserType.ALL;
		}
		else if(mGroup.getObjectType().equals(String.valueOf(MessageObjectType.PARENT))){
			receiveType = UserType.PARENT;
		}
		else if(mGroup.getObjectType().equals(String.valueOf(MessageObjectType.STUDENT))){
			receiveType = UserType.STUDENT;
		}
		else if(mGroup.getObjectType().equals(String.valueOf(MessageObjectType.TEACHER))){
			receiveType = UserType.TEACHER;
		}
		
		reqData.setReceiveType(receiveType);
		reqData.setContent(message.getContent());

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						message.setSendStatus(MessageSendStatus.SUCCESS);
						mAdapter.notifyDataSetChanged();
						saveMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to PushMessage request!");
						message.setSendStatus(MessageSendStatus.FAILED);
						mAdapter.notifyDataSetChanged();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private Message initMessage(String content) {
		UserCache userCache = UserCache.getInstance();
		Message message = new Message();

		message.setMsgType(MessageType.NORMAL);
		message.setSenderId(userCache.getUserInfo().getUserId());
		message.setReceiverId(mGroup.getGroupId());
		message.setObjectType(mGroup.getObjectType());
		message.setGroupType(mGroup.getGroupType());
		message.setContent(content);
		message.setOutFlag(1);
		message.setCreateTime(DateTimeUtil.getLongTime());
		message.setSendStatus(MessageSendStatus.SENDING);
		return message;
	}

	private void saveMessage(Message message) {
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addMessage(message);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	final class InternalMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "[InternalMessageReceiver] Receive intent: \r\n" + intent);
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			Message message = (Message) intent.getSerializableExtra(PushUtils.MESSAGE);

			if (message.getObjectType() == mGroup.getObjectType()
					&& message.getGroupType() == mGroup.getGroupType()
					&& !message.getSenderId().equals(userInfo.getUserId())
					&& message.getReceiverId().equals(mGroup.getGroupId())) {
				mMsgList.add(message);
				mAdapter.notifyDataSetChanged();
				lvChatList.setSelection(mAdapter.getCount() - 1);
				
				clearUnreadCount();
				refreshRecentMessage();
				abortBroadcast();
			}
		}
	}
	
	private void refreshRecentMessage() {
		Intent intent = new Intent(PushUtils.ACTION_REFRESH_RECENT);
		sendBroadcast(intent);
	}

	final class GroupChatListAdapter extends MyBaseAdatper {

		@Override
		public int getCount() {
			return mMsgList.size();
		}

		@Override
		public Object getItem(int pos) {
			return mMsgList.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parentView) {
			TextFromHolder fromHolder = null;
			TextToHolder toHolder = null;
			int outFlag = getItemViewType(pos);

			if (convertView == null) {
				switch (outFlag) {
				case 0:
					fromHolder = new TextFromHolder();
					convertView = mInflater.inflate(R.layout.chatting_item_from, null);
					fromHolder.layoutTime = (LinearLayout) convertView.findViewById(R.id.layout_send_time);
					fromHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
					fromHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_chatting_send_time);
					fromHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_chatting_avatar);
					fromHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatting_content);
					bindFromData(fromHolder, pos);
					convertView.setTag(fromHolder);
					break;
				case 1:
					toHolder = new TextToHolder();
					convertView = mInflater.inflate(R.layout.chatting_item_to, null);
					toHolder.layoutTime = (LinearLayout) convertView.findViewById(R.id.layout_send_time);
					toHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_chatting_send_time);
					toHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_chatting_avatar);
					toHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatting_content);
					toHolder.ivChattingState = (ImageView) convertView.findViewById(R.id.iv_chatting_state);
					toHolder.pbUploading = (ProgressBar) convertView.findViewById(R.id.pb_uploading);
					bindToData(toHolder, pos);
					convertView.setTag(toHolder);
					break;
				}
			} else {
				switch (outFlag) {
				case 0:
					fromHolder = (TextFromHolder) convertView.getTag();
					bindFromData(fromHolder, pos);
					break;
				case 1:
					toHolder = (TextToHolder) convertView.getTag();
					bindToData(toHolder, pos);
					break;
				}
			}

			return convertView;
		}

		private UserInfo getUserInfo(String userId) {
			if (mCacheUser.containsKey(userId)) {
				return mCacheUser.get(userId);
			}

			DBAdapter dbAdapter = new DBAdapter();
			try {
				dbAdapter.open();
				UserInfo userInfo = dbAdapter.getUserInfo(userId);
				mCacheUser.put(userId, userInfo);
				return userInfo;
			} catch (Exception e) {
				Log.e(TAG, "Failed to operate database: " + e);
				return null;
			} finally {
				dbAdapter.close();
			}
		}

		private void bindFromData(TextFromHolder fromHolder, int pos) {
			Message message = mMsgList.get(pos);
			UserInfo otherUser = getUserInfo(message.getSenderId());
			fromHolder.tvContent.setText(message.getContent());
			showSendTime(fromHolder.layoutTime, fromHolder.tvSendTime, pos);

			if (otherUser != null) {
				fromHolder.tvUserName.setText(otherUser.getUserName());
				fromHolder.ivAvatar.setOnClickListener(new OnFromAvatarClick(otherUser));

				try {
					if (URLChecker.isUrl(otherUser.getPicUrl())) {
						Log.d(TAG, "loadImage: " + otherUser.getPicUrl());
						ImageListener listener = ImageLoader.getImageListener(fromHolder.ivAvatar,
								R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
						ImageCacheManager.getInstance().getImageLoader().get(otherUser.getPicUrl(), listener);
					}
				} catch (Exception e) {
					Log.e(TAG, "Failed to loadImage: " + otherUser.getPicUrl());
				}
			} else {
				fromHolder.tvUserName.setText("陌生人");
				fromHolder.ivAvatar.setClickable(false);
			}
		}

		private void bindToData(TextToHolder toHolder, int pos) {
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			Message message = mMsgList.get(pos);
			toHolder.tvContent.setText(message.getContent());
			showSendTime(toHolder.layoutTime, toHolder.tvSendTime, pos);
			toHolder.ivAvatar.setOnClickListener(new OnToAvatarClick());
			toHolder.ivChattingState.setOnClickListener(new OnChatStateClick(pos));

			try {
				if (URLChecker.isUrl(userInfo.getPicUrl())) {
					Log.d(TAG, "loadImage: " + userInfo.getPicUrl());
					ImageListener listener = ImageLoader.getImageListener(toHolder.ivAvatar,
							R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
					ImageCacheManager.getInstance().getImageLoader().get(userInfo.getPicUrl(), listener);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + userInfo.getPicUrl());
			}

			if (message.getSendStatus() == MessageSendStatus.SENDING) {
				toHolder.pbUploading.setVisibility(View.VISIBLE);
				toHolder.ivChattingState.setVisibility(View.GONE);
			} else if (message.getSendStatus() == MessageSendStatus.FAILED) {
				toHolder.pbUploading.setVisibility(View.GONE);
				toHolder.ivChattingState.setVisibility(View.VISIBLE);
			} else {
				toHolder.pbUploading.setVisibility(View.GONE);
				toHolder.ivChattingState.setVisibility(View.GONE);
			}
		}

		private void showSendTime(LinearLayout layoutTime, TextView tvSendTime, int pos) {
			Message message = mMsgList.get(pos);
			boolean visible = false;

			if (pos == 0) {
				visible = true;
			} else if (pos > 0) {
				String preDate = mMsgList.get(pos - 1).getCreateTime().substring(0, 10);
				String curDate = message.getCreateTime().substring(0, 10);
				if (!curDate.equals(preDate)) {
					visible = true;
				} else {
					visible = false;
				}
			} else {
				visible = false;
			}

			if (visible) {
				layoutTime.setVisibility(View.VISIBLE);
				tvSendTime.setText(DateTimeUtil.toMessageTime(message.getCreateTime()));
			} else {
				layoutTime.setVisibility(View.GONE);
			}
		}

		@Override
		public int getItemViewType(int pos) {
			Message message = mMsgList.get(pos);
			int outFlag = message.getOutFlag();
			return outFlag;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		final class OnFromAvatarClick implements OnClickListener {
			private UserInfo mOtherUser = null;

			public OnFromAvatarClick(UserInfo otherUser) {
				mOtherUser = otherUser;
			}

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupChatActivity.this, UserInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("userInfo", mOtherUser);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}

		final class OnToAvatarClick implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupChatActivity.this, MyAccountActivity.class);
				startActivity(intent);
			}
		}

		final class OnChatStateClick implements OnClickListener {
			private int mPos = 0;

			public OnChatStateClick(int pos) {
				mPos = pos;
			}

			@Override
			public void onClick(View v) {
				Log.d(TAG, "pos: " + mPos);
				Log.d(TAG, "msgList size: " + mMsgList.size());

				new AlertDialog.Builder(GroupChatActivity.this).setTitle("提示").setMessage("确认重发该消息？")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Message message = mMsgList.get(mPos);
								message.setSendStatus(MessageSendStatus.SENDING);
								mAdapter.notifyDataSetChanged();
								sendMessage(message);
							}
						}).setNegativeButton("取消", null).show();
			}
		}

		final class TextFromHolder {
			LinearLayout layoutTime;
			TextView tvUserName;
			TextView tvSendTime;
			ImageView ivAvatar;
			TextView tvContent;
		}

		final class TextToHolder {
			LinearLayout layoutTime;
			TextView tvSendTime;
			ImageView ivAvatar;
			TextView tvContent;
			ImageView ivChattingState;
			ProgressBar pbUploading;
		}
	}
}
