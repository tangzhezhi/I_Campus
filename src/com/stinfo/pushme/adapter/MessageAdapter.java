package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.ChatActivity;
import com.stinfo.pushme.activity.GroupChatActivity;
import com.stinfo.pushme.common.AppConstant.MessageGroupType;
import com.stinfo.pushme.common.AppConstant.MessageObjectType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.HomeworkDBAdapter;
import com.stinfo.pushme.db.MessageDBAdapter;
import com.stinfo.pushme.entity.Group;
import com.stinfo.pushme.entity.RecentUserMessage;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.PushUtils;
import com.stinfo.pushme.util.URLChecker;
import com.stinfo.pushme.view.BadgeView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 消息数据适配器
 * @author lenovo
 *
 */
public class MessageAdapter extends MyBaseAdatper {
	private static final String TAG = "MessageAdapter";
	private LayoutInflater mInflater;
	private ArrayList<RecentUserMessage> mUserMsgList;
	private Context mContext;
	private Button curDel_btn;

	public MessageAdapter(Context context, ArrayList<RecentUserMessage> list) {
		super();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		mUserMsgList = list;
	}

	@Override
	public int getCount() {
		return mUserMsgList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mUserMsgList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_message, null);

			holder = new ViewHolder();
			holder.ivUserAvatar = (ImageView) convertView.findViewById(R.id.iv_user_avatar);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_send_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.btnDel = (Button) convertView.findViewById(R.id.message_del);
			
			holder.linearLayout = (LinearLayout)convertView.findViewById(R.id.layout_message_item_label);
			
			holder.tvUserName.setVisibility(View.VISIBLE);
			holder.badge = new BadgeView(mContext, holder.ivUserAvatar);
			holder.badge.setBadgeMargin(0);
			curDel_btn = holder.btnDel;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final RecentUserMessage userMsg = mUserMsgList.get(pos);
		if (userMsg != null) {
			if (userMsg.getGroupType() == MessageGroupType.PERSONAL) {
				bindPersonalMessage(holder, userMsg);
			} else if (userMsg.getGroupType() == MessageGroupType.CLASS) {
				bindGroupMessage(holder, userMsg);
			} else if (userMsg.getGroupType() == MessageGroupType.SCHOOL) {
				bindSchoolMessage(holder, userMsg);
			}
		}
		
		
		// 为删除按钮添加监听事件，实现点击删除按钮时删除该项
		holder.btnDel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Dialog alertDialog = new AlertDialog.Builder(mContext).
                            setTitle("提示"). 
                            setMessage("您确定删除会话消息吗？"). 
                            setPositiveButton("确认", new DialogInterface.OnClickListener() { 
             
                                @Override 
                                public void onClick(DialogInterface dialog, int which) { 
                                    if (curDel_btn != null){
                                    	mUserMsgList.remove(userMsg);
                                		MessageDBAdapter dbAdapter = new MessageDBAdapter();
                                		try {
                                			dbAdapter.open();
                                			dbAdapter.deleteMessage(userMsg.getObjectId());
                                			dbAdapter.deleteRecentMessage(userMsg.getObjectId());
                                		} catch (Exception e) {
                                			Log.e(TAG, "Failed to operate database: " + e);
                                		} finally {
                                			dbAdapter.close();
                                		}
                                        
                                        notifyDataSetChanged();
                                    }
                                } 
                            }). 
                            setNegativeButton("取消", null). 
                            create(); 
                    	alertDialog.show(); 
                }
              
        });
		
		
		holder.linearLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	onChat(pos);
                }
              
        });
		
		
		return convertView;
	}

	private void bindPersonalMessage(ViewHolder holder, RecentUserMessage userMsg) {
		try {
			if (URLChecker.isUrl(userMsg.getPicUrl())) {
				Log.d(TAG, "loadImage: " + userMsg.getPicUrl());
				ImageListener listener = ImageLoader.getImageListener(holder.ivUserAvatar,
						R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
				ImageCacheManager.getInstance().getImageLoader().get(userMsg.getPicUrl(), listener);
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to loadImage: " + userMsg.getPicUrl());
		}

		holder.tvUserName.setText(userMsg.getUserName());
		holder.tvSendTime.setText(DateTimeUtil.toMessageTime(userMsg.getUpdateTime()));
		holder.tvContent.setText(userMsg.getContent());
		if (userMsg.getUnreadCount() > 0) {
			holder.badge.setText(String.valueOf(userMsg.getUnreadCount()));
			holder.badge.show();
		} else {
			holder.badge.hide();
		}
	}

	private void bindGroupMessage(ViewHolder holder, RecentUserMessage userMsg) {
		String groupExt = "";
		if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.ALL)) ) {
			groupExt = "班级群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.TEACHER))) {
			groupExt = "老师群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.STUDENT))) {
			groupExt = "学生群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.PARENT))) {
			groupExt = "家长群";
		}
		
		String className = UserCache.getInstance().getClassName(userMsg.getObjectId());
		holder.tvUserName.setText(className + groupExt);
		holder.ivUserAvatar.setImageResource(R.drawable.avatar_default_group);
		holder.tvSendTime.setText(DateTimeUtil.toMessageTime(userMsg.getUpdateTime()));
		holder.tvContent.setText(userMsg.getContent());
		if (userMsg.getUnreadCount() > 0) {
			holder.badge.setText(String.valueOf(userMsg.getUnreadCount()));
			holder.badge.show();
		} else {
			holder.badge.hide();
		}
	}

	private void bindSchoolMessage(ViewHolder holder, RecentUserMessage userMsg) {
		holder.tvUserName.setText("学校消息");
		holder.ivUserAvatar.setImageResource(R.drawable.avatar_default_group);
		holder.tvSendTime.setText(DateTimeUtil.toMessageTime(userMsg.getUpdateTime()));
		holder.tvContent.setText(userMsg.getContent());
		if (userMsg.getUnreadCount() > 0) {
			holder.badge.setText(String.valueOf(userMsg.getUnreadCount()));
			holder.badge.show();
		} else {
			holder.badge.hide();
		}
	}
	
	private void onChat(int pos) {
		RecentUserMessage userMsg = mUserMsgList.get(pos);
		updateUnreadCount(userMsg);
		if (userMsg.getGroupType() == MessageGroupType.PERSONAL) {
			onPersonalChat(userMsg);
		} else if (userMsg.getGroupType() == MessageGroupType.CLASS) {
			onClassChat(userMsg);
		}
	}
	
	private void updateUnreadCount(RecentUserMessage userMsg) {
		userMsg.setUnreadCount(0);
		notifyDataSetChanged();
		refreshAllUnread();
	}
	
	private void refreshAllUnread() {
		int unreadCount = 0;
		for (RecentUserMessage userMsg : mUserMsgList) {
			unreadCount += userMsg.getUnreadCount();
		}
		
		Intent intent = new Intent(PushUtils.ACTION_REFRESH_UNREAD);
		intent.putExtra(PushUtils.UNREAD_COUNT, unreadCount);
		mContext.sendBroadcast(intent);
	}

	private void onPersonalChat(RecentUserMessage userMsg) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userMsg.getObjectId());
		userInfo.setUserName(userMsg.getUserName());
		userInfo.setPicUrl(userMsg.getPicUrl());
		userInfo.setPhone(userMsg.getPhone());
		userInfo.setSex(userMsg.getSex());
		userInfo.setUservalue(userMsg.getUservalue());
		userInfo.setMsgUserType(userMsg.getUserType());

		Intent intent = new Intent(mContext, ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("userInfo", userInfo);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}

	private void onClassChat(RecentUserMessage userMsg) {
		String groupExt = "";
		if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.ALL))) {
			groupExt = "班级群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.TEACHER))) {
			groupExt = "老师群";
		} else if (userMsg.getObjectType().equals(String.valueOf(MessageObjectType.STUDENT))) {
			groupExt = "学生群";
		} else if (userMsg.getObjectType() .equals(String.valueOf(MessageObjectType.PARENT))) {
			groupExt = "家长群";
		}
		
		String className = UserCache.getInstance().getClassName(userMsg.getObjectId());
		String groupName = className + groupExt;
		Group group = new Group(groupName, userMsg.getObjectId(), String.valueOf(userMsg.getObjectType()), userMsg.getGroupType());

		Intent intent = new Intent(mContext, GroupChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("group", group);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}
	
	
	
	
	final class ViewHolder {
		ImageView ivUserAvatar;
		TextView tvUserName;
		TextView tvSendTime;
		TextView tvContent;
		BadgeView badge;
		Button btnDel;
		LinearLayout linearLayout;
	}
}