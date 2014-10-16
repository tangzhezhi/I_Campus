package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.ChatActivity;
import com.stinfo.pushme.activity.NoticeDetailActivity;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.util.DateTimeUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 对通知数据进行填充、绑定，对应界面位置
 * @author lenovo
 *
 */
public class NoticeListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<Notice> mNoticeList;
	private LinearLayout itemBody;
	private RelativeLayout itemReply;
	private Context context;
	private int currentFragmentPos;
	
	public NoticeListAdapter(Context context, ArrayList<Notice> noticeList) {
		super();
		mNoticeList = noticeList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}
	
	public NoticeListAdapter(Context context, ArrayList<Notice> noticeList,int currentFragmentPos) {
		super();
		mNoticeList = noticeList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.currentFragmentPos = currentFragmentPos;
	}
	
	

	@Override
	public int getCount() {
		return mNoticeList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mNoticeList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
	
	/**
	 * 显示记录，绑定值，最后返回视图
	 * 通过convertView+ViewHolder来实现，
	 * ViewHolder就是一个静态类，
	 * 使用 ViewHolder 的关键好处是缓存了显示数据的视图（View），加快了 UI 的响应速度。
	 */
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_notice, null);
			holder = new ViewHolder();
			holder.tvIsRead = (TextView) convertView.findViewById(R.id.tv_isRead);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_create_time);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvPublish = (TextView) convertView.findViewById(R.id.tv_publish);
			holder.tvPublish_userId = (TextView) convertView.findViewById(R.id.tv_publish_userid);
			holder.tvTotal = (TextView) convertView.findViewById(R.id.tv_user_reply_total);
			holder.rl_reply = (RelativeLayout) convertView.findViewById(R.id.layout_notice_reply_item_1);
			holder.rl_reply_2 = (RelativeLayout) convertView.findViewById(R.id.layout_notice_reply_item_2);
			holder.ll_body = (LinearLayout) convertView.findViewById(R.id.layout_notice_item_body);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Notice notice = mNoticeList.get(pos);
		if (notice != null) {
			
			holder.tvTime.setText(notice.getCreateTime());
			if(notice.isRead()){
				holder.tvTitle.setText(notice.getTitle());
				holder.tvIsRead.setVisibility(View.GONE);
			}else{
				holder.tvTitle.setText(notice.getTitle());
				holder.tvIsRead.setVisibility(View.VISIBLE);
			}
			
			holder.tvContent.setText(notice.getContent());
			
			if(notice.getTotal()!=null ){
				holder.tvTotal.setText("("+notice.getTotal()+")");
			}
			
			if(notice.getAuthorName()!=null){
				holder.tvPublish.setText("\r\r发布人\r\r"+notice.getAuthorName());
			}
			else{
				holder.tvPublish.setText("\r\r发布人\r\r 行政部");
			}
			
			holder.tvPublish_userId.setText(notice.getAuthorId());
			
			UserCache userCache = UserCache.getInstance();
			if(currentFragmentPos==1 && userCache.getUserInfo() instanceof Parent){
				holder.rl_reply.setVisibility(View.GONE);
				holder.rl_reply_2.setVisibility(View.GONE);
			}
			
			
			holder.ll_body.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					//更新isRead
					DBAdapter dbAdapter = new DBAdapter();
					try {
						dbAdapter.open();
						dbAdapter.updateNoticeIsReadByWhere(notice);
					}catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						dbAdapter.close();
					}
					
					Intent intent = new Intent(context, NoticeDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("noticeList", mNoticeList);
					bundle.putInt("index", pos);
					intent.putExtras(bundle);
					context.startActivity(intent);
					
					
				}
				
			});
			
//			holder.rl_reply_2.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					
//					UserInfo userInfo = new UserInfo();
//					
//					DBAdapter dbAdapter = new DBAdapter();
//					try {
//						dbAdapter.open();
//						userInfo = dbAdapter.getUserInfoByUserId(notice.getAuthorId());
//					} catch (Exception e) {
//						Log.e("NoticeListAdapter", "Failed to operate database: " + e);
//					} finally {
//						dbAdapter.close();
//					}
//					
//					Intent intent = new Intent(context, ChatActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("userInfo", userInfo);
//					intent.putExtras(bundle);
//					context.startActivity(intent);
//				}
//				
//			});
			
			
			
			holder.rl_reply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					//更新isRead
					DBAdapter dbAdapter = new DBAdapter();
					try {
						dbAdapter.open();
						dbAdapter.updateNoticeIsReadByWhere(notice);
					}catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						dbAdapter.close();
					}
					
					Intent intent = new Intent(context, NoticeDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("noticeList", mNoticeList);
					bundle.putInt("index", pos==0?pos:pos-1);
					bundle.putBoolean("reply", true);
					intent.putExtras(bundle);
					context.startActivity(intent);
					
					
				}
				
			});
			
			
			
		}

		return convertView;
	}

	final class ViewHolder {
		TextView tvTime;
		TextView tvTitle;
		TextView tvContent;
		TextView tvPublish;
		TextView tvTotal;
		TextView tvPublish_userId;
		TextView tvIsRead;
		RelativeLayout rl_reply;
		RelativeLayout rl_reply_2;
		LinearLayout ll_body;
		
	}
}