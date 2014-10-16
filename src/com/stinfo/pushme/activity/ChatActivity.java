package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.MessageGroupType;
import com.stinfo.pushme.common.AppConstant.MessageObjectType;
import com.stinfo.pushme.common.AppConstant.MessageSendStatus;
import com.stinfo.pushme.common.AppConstant.MessageType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.entity.MsgTemplateGroup;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.TeacherRoster;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PushMessageReq;
import com.stinfo.pushme.rest.entity.QueryMsgTemplateGroupReq;
import com.stinfo.pushme.rest.entity.QueryMsgTemplateGroupResp;
import com.stinfo.pushme.rest.entity.SchoolClassReq;
import com.stinfo.pushme.rest.entity.SchoolClassResp;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.util.URLChecker;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 消息交谈界面
 * @author lenovo
 *
 */
public class ChatActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "ChatActivity";
	private ArrayList<Message> mMsgList = new ArrayList<Message>();
	private LayoutInflater mInflater;
	private UserInfo mOtherUser = new UserInfo();
	private ChatListAdapter mAdapter = null;
	private InternalMessageReceiver mReceiver = null;
	private ListView lvChatList;
	private EditText etContent;
	private Button btnSend;
	private Button btnMsgTemplate;
	private Button btnAddMsgReceiver;
	private RelativeLayout layoutMsgTemplateList;
	private ArrayList<UserInfo> mUserListSelect ;
	private String userIdSelect="";
	private String userNameSelect="";
	
	private TextView tvMsgRecevierNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		initView();

		handleIntent(getIntent());
		registerMyReceiver();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
		super.onNewIntent(intent);		
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		UserInfo userInfo  = null;
		if(intent!=null){
			if(intent.getSerializableExtra("userInfo")!=null){
				userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
			}
			else if(intent.getSerializableExtra(PushUtils.MESSAGE)!=null){
				Message msg = (Message) intent.getSerializableExtra(PushUtils.MESSAGE);
				userInfo = findReceiveUser(msg.getSenderId());
			}
		}
		
		mOtherUser = userInfo ;
		
		if(userInfo instanceof TeacherRoster){
			mOtherUser.setMsgUserType(UserType.TEACHER);
		}
		else if(userInfo instanceof ParentRoster){
			mOtherUser.setMsgUserType(UserType.PARENT);
		}
		else if(userInfo instanceof StudentRoster){
			mOtherUser.setMsgUserType(UserType.STUDENT);
		}
		
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(mOtherUser.getUserName());

		initData();
		clearUnreadCount();
	}
	
	/**
	 * 更新未读消息标记为0
	 */
	private void clearUnreadCount() {
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.clearUnreadCount(mOtherUser.getUserId(), String.valueOf(MessageObjectType.PERSONAL),
					MessageGroupType.PERSONAL);
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
		//新启动MainActivity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		XGPushManager.onActivityStoped(this);
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			onSend();
			break;
		case R.id.btn_addTemplate:
			onSelectMsgTemplate();
			break;
		case R.id.btn_addMsgReceiver:
			onSelectMsgReceiver();
			break;
		}
	}
	
	
	private void onSelectMsgReceiver() {
		Log.d(TAG, "进入onSelectUser");
		Intent intent = new Intent(this, UserSelectActivity.class);
		intent.putExtra("type", "chatGroup");
		startActivityForResult(intent, 2);
	}
	

	/**
	 * 选择模板
	 */
	private void onSelectMsgTemplate() {
		final UserCache userCache = UserCache.getInstance();
		final UserInfo userInfo = userCache.getUserInfo();
		QueryMsgTemplateGroupReq reqData = new QueryMsgTemplateGroupReq();
		reqData.setUserId(userInfo.getUserId());
		reqData.setSchoolNo(userCache.getCurrentClass().getSchoolId());
		reqData.setSessionKey(userCache.getSessionKey());
		
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						
						try {
							QueryMsgTemplateGroupResp respData = new QueryMsgTemplateGroupResp(response);
							if (respData.getAck() == Ack.SUCCESS) {
								//登陆成功的处理
								List<MsgTemplateGroup> list  = respData.getMsgTemplateGroupList();
								
								ArrayList<String> groupNames = new ArrayList<String> ();
								ArrayList<String> groupCodes = new ArrayList<String> ();
								
								
								for(MsgTemplateGroup cs : list){
									groupNames.add(cs.getGroupName());
									groupCodes.add(cs.getGroupCode());
								}
								
						        final String[] groupNamesArray = groupNames.toArray(new String[0]); 
						        final String[] groupCodesArray = groupCodes.toArray(new String[0]); 
								
								  try {
									Dialog alertDialog = new AlertDialog.Builder(ChatActivity.this). 
									            setTitle("选择消息模板"). 
									            setIcon(R.drawable.actionbar_icon) 
									            .setItems(groupNamesArray, new DialogInterface.OnClickListener() { 
									                @Override 
									                public void onClick(DialogInterface dialog, int which) {
									            		Intent intent = new Intent(ChatActivity.this, MsgTemplateActivity.class);
									            		intent.setAction("com.stinfo.pushme.activity.msgtemplate");
									            		intent.putExtra("groupCode", groupCodesArray[which]);
									            		intent.putExtra("schoolNo", userCache.getCurrentClass().getSchoolId());
									            		startActivityForResult(intent, 1);
									                } 
									            }). 
									            setNegativeButton("取消", new DialogInterface.OnClickListener() { 
									                @Override 
									                public void onClick(DialogInterface dialog, int which) { 
									                } 
									            }). 
									            create(); 
									    alertDialog.show();
								} catch (Exception e) {
									e.printStackTrace();
								} 
								
							} else {
								MessageBox.showAckError(ChatActivity.this, respData.getAck());
							}
						} catch (Exception e) {
							MessageBox.showParserError(getApplicationContext());
							Log.e(TAG, "Failed to parser response data! \r\n" + e);
						}
						
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(getApplicationContext());
					}
				});
		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode==1){
			String msgcontent =  (String) intent.getStringExtra("msgtemplate");
			etContent.setText(msgcontent);
		}
		else if(resultCode==2){
			mUserListSelect = (ArrayList<UserInfo>) intent.getSerializableExtra("selectList");
			StringBuilder sbUserName = new StringBuilder();
			StringBuilder sbUserIds = new StringBuilder();
			for(UserInfo userInfo : mUserListSelect){
				sbUserName.append(userInfo.getUserName());
				sbUserName.append(",");
				sbUserIds.append(userInfo.getUservalue());
				sbUserIds.append(",");
			}
			
			
			if(sbUserIds.length() > 1){
				
				userIdSelect = sbUserIds.substring(0, sbUserIds.length()-1);
				userNameSelect = sbUserName.substring(0, sbUserName.length()-1);
				
				if(!userIdSelect.contains(mOtherUser.getUservalue())){
					userNameSelect = mOtherUser.getUserName()+"," + userNameSelect;
				}
				
				tvMsgRecevierNames.setText("已选人员："+userNameSelect);
				tvMsgRecevierNames.setVisibility(View.VISIBLE);
			}
			Log.d(TAG, "所选用户ID：：：：："+userIdSelect);
		}
		
	}
	

	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new InternalMessageReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(PushUtils.ACTION_INTERNAL_MESSAGE);
			intentFilter.setPriority(1000);
			registerReceiver(mReceiver, intentFilter);
		}
	}

	private void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		etContent = (EditText) findViewById(R.id.et_send_content);
		tvMsgRecevierNames =  (TextView) findViewById(R.id.tv_msg_recevier);
		
		
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		
		lvChatList = (ListView) findViewById(R.id.lv_chat_list);
		mAdapter = new ChatListAdapter();
		lvChatList.setAdapter(mAdapter);
		btnMsgTemplate = (Button) findViewById(R.id.btn_addTemplate);
		btnMsgTemplate.setOnClickListener(this);
		
		btnAddMsgReceiver = (Button) findViewById(R.id.btn_addMsgReceiver);
		btnAddMsgReceiver.setOnClickListener(this);
		
	}

	private void initData() {
		DBAdapter dbAdapter = new DBAdapter();
		mMsgList.clear();

		try {
			dbAdapter.open();
			mMsgList.addAll(dbAdapter.getPersonalMessage(mOtherUser.getUserId()));
			mAdapter.notifyDataSetChanged();
			lvChatList.setSelection(mAdapter.getCount() - 1);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	
	private UserInfo findReceiveUser(String userId) {
		DBAdapter dbAdapter = new DBAdapter();
		UserInfo userInfo = null;
		try {
			dbAdapter.open();
			userInfo = dbAdapter.getUserInfoByUserId(userId);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		return userInfo;
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
		
		if(userIdSelect!=null && userIdSelect.length() > 0){
			
			if(!userIdSelect.contains(mOtherUser.getUservalue())){
				userIdSelect = mOtherUser.getUservalue() +","+ userIdSelect ;
				mUserListSelect.add(0,mOtherUser);
			}
			
			
			for(UserInfo userInfo : mUserListSelect){
				Message messagetemp = message;
				messagetemp.setReceiverId(userInfo.getUservalue());
				if(userInfo instanceof TeacherRoster){
					messagetemp.setObjectType(UserType.TEACHER);
				}
				else if(userInfo instanceof ParentRoster){
					messagetemp.setObjectType(UserType.PARENT);
				}
				sendMessage(messagetemp);
				
				saveMessage(messagetemp);
			}
		}
		else{
			 message.setObjectType(mOtherUser.getMsgUserType());
			 
			sendMessage(message);
			saveMessage(message);
		}
		etContent.setText("");
	}
	
	
	
	
	

	private void sendMessage(final Message message) {
		UserCache userCache = UserCache.getInstance();
		PushMessageReq reqData = new PushMessageReq();
		
		String receiveType = "";
		
		if(userIdSelect!=null && userIdSelect.length() > 0){
			receiveType = message.getObjectType();
		}
		else{
			if(mOtherUser!=null){
				receiveType = mOtherUser.getMsgUserType();
			}
		}
		
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setUserId(message.getReceiverId());
		reqData.setType("1"); //普通消息
		reqData.setReceiveType(receiveType); 
		reqData.setOperator(userCache.getUserInfo().getUserId());
		reqData.setContent(message.getContent());

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						
						JSONObject rootObj;
						try {
							rootObj = new JSONObject(response);
							int ack = rootObj.getInt("ack");
							
							if(ack==Ack.SUCCESS){
								
								message.setSendStatus(MessageSendStatus.SUCCESS);
								mAdapter.notifyDataSetChanged();
							}
							else{
								MessageBox.showAckError(ChatActivity.this, ack);
							}
							
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
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
		message.setReceiverId(mOtherUser.getUservalue());
		message.setGroupType(MessageGroupType.PERSONAL);
		message.setContent(content);
		message.setOutFlag(1);
		message.setCreateTime(DateTimeUtil.getLongTime());
		message.setSendStatus(MessageSendStatus.SENDING);
		return message;
	}

	private void saveMessage(Message message) {
		
		DBAdapter dbAdapter = new DBAdapter();
		UserInfo userInfo = null;
		try {
			dbAdapter.open();
			Log.v(TAG, "message.getReceiverId():::::::: " + message.getReceiverId());
			userInfo = dbAdapter.getUserInfoByUservalueAndUserType(message.getReceiverId(), message.getObjectType());
			if(userInfo!=null){
				message.setReceiverId(userInfo.getUserId());
				dbAdapter.addMessage(message);
			}
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
			
			Log.d(TAG, "content: " + message.getContent());
			Log.d(TAG, "groupType: " + message.getGroupType());
			Log.d(TAG, "senderId: " + message.getSenderId());
			Log.d(TAG, "receiverId: " + message.getReceiverId());
			Log.d(TAG, "otherUser: " + mOtherUser.getUserId());
			Log.d(TAG, "userId: " + userInfo.getUserId());
			
			if (message.getGroupType() == MessageGroupType.PERSONAL
					&& message.getSenderId().equals(mOtherUser.getUserId())
					&& message.getReceiverId().equals(userInfo.getUserId())) {				
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

	final class ChatListAdapter extends MyBaseAdatper {

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

		private void bindFromData(TextFromHolder fromHolder, int pos) {
			Message message = mMsgList.get(pos);
			fromHolder.tvContent.setText(message.getContent());
			showSendTime(fromHolder.layoutTime, fromHolder.tvSendTime, pos);
			fromHolder.ivAvatar.setOnClickListener(new OnFromAvatarClick());

			try {
				if (URLChecker.isUrl(mOtherUser.getPicUrl())) {
					Log.d(TAG, "loadImage: " + mOtherUser.getPicUrl());
					ImageListener listener = ImageLoader.getImageListener(fromHolder.ivAvatar,
							R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
					ImageCacheManager.getInstance().getImageLoader().get(mOtherUser.getPicUrl(), listener);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + mOtherUser.getPicUrl());
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
			int outFlag = 0;
			Message message = mMsgList.get(pos);
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			if(userInfo.getUserId().equals(message.getSenderId())){
				outFlag = 0;
			}
			else{
				outFlag = 1;
			}
//			int outFlag = message.getOutFlag();
			return outFlag;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		final class OnFromAvatarClick implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatActivity.this, UserInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("userInfo", mOtherUser);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}

		final class OnToAvatarClick implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatActivity.this, MyAccountActivity.class);
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

				new AlertDialog.Builder(ChatActivity.this).setTitle("提示").setMessage("确认重发该消息？")
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