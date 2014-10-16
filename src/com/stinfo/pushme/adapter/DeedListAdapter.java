package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.ChatActivity;
import com.stinfo.pushme.activity.DeedDetailActivity;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Deed;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.URLChecker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 对闪光台、曝光台、爱心分享数据进行填充、绑定，对应界面位置
 * @author lenovo
 *
 */
public class DeedListAdapter extends MyBaseAdatper {
	private String TAG = DeedListAdapter.class.getName();
	private LayoutInflater mInflater;
	private ArrayList<Deed> mDeedList;
	private LinearLayout itemBody;
	private RelativeLayout itemReply;
	private Context context;
	private String deedtype;
	
	
	public DeedListAdapter(Context context, ArrayList<Deed> deedList,String deedtype) {
		super();
		mDeedList = deedList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.deedtype = deedtype;
	}
	
	

	@Override
	public int getCount() {
		return mDeedList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mDeedList.get(pos);
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
			convertView = mInflater.inflate(R.layout.item_deed, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_create_time);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvPublish = (TextView) convertView.findViewById(R.id.tv_publish);
			holder.tvPublish_userId = (TextView) convertView.findViewById(R.id.tv_publish_userid);
			
			holder.tvCommontClickNum =  (TextView) convertView.findViewById(R.id.tv_commont_clicknum);
			holder.tvPraiseClickNum = (TextView) convertView.findViewById(R.id.tv_praise_clicknum);
			holder.ivfirstPic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.rl_reply = (RelativeLayout) convertView.findViewById(R.id.layout_deed_reply_item_1);
			holder.ll_body = (LinearLayout) convertView.findViewById(R.id.layout_deed_item_body);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Deed deed = mDeedList.get(pos);
		if (deed != null) {
			holder.tvTime.setText(deed.getReleasedate());
			holder.tvTitle.setText(deed.getTitle());
			holder.tvContent.setText(deed.getContent());
			
			if(deed.getReleaseusername()!=null){
				holder.tvPublish.setText("\r\r发布人\r\r"+deed.getReleaseusername());
			}
			else{
				holder.tvPublish.setText("\r\r发布人\r\r 行政部");
			}
			
			holder.tvPraiseClickNum.setText("("+deed.getParisesize()+")");
			
			holder.tvPublish_userId.setText(deed.getReleaseoperid());
			
			holder.tvCommontClickNum.setText("("+deed.getCommentsize()+")");
			
			UserCache userCache = UserCache.getInstance();
			
			try {
				if(deed.getFirstPic()!=null && !deed.getFirstPic().equals("")){
					holder.ivfirstPic.setVisibility(View.VISIBLE);
					if (URLChecker.isUrl(AppConstant.School_Platform_Photo_QUERY_URL+deed.getFirstPic())) {
						Log.d(TAG, "loadImage: " + AppConstant.School_Platform_Photo_QUERY_URL+deed.getFirstPic());
						ImageListener listener = ImageLoader.getImageListener(holder.ivfirstPic,
								R.drawable.album_default_photo, R.drawable.album_default_photo);
						ImageCacheManager.getInstance().getImageLoader().get(AppConstant.School_Platform_Photo_QUERY_URL+deed.getFirstPic(), listener);
					}
				}
				else{
					holder.ivfirstPic.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + AppConstant.School_Platform_Photo_QUERY_URL+deed.getFirstPic());
			}
			
			holder.ll_body.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, DeedDetailActivity.class);
					intent.putExtra("behaviorid", mDeedList.get(pos).getId());
					//模块标识，1闪光台；2曝光台；3爱心分享
					intent.putExtra("plates",deedtype);
					context.startActivity(intent);
				}
				
			});
			
			
			holder.rl_reply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					int clicknumAddOne = Integer.valueOf(mDeedList.get(pos).getClicknum())+1;
					mDeedList.get(pos).setClicknum(String.valueOf(clicknumAddOne));
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
		TextView tvPublish_userId;
		ImageView ivfirstPic;
		TextView tvPraiseClickNum;
		TextView tvCommontClickNum;
		
		RelativeLayout rl_reply;
		RelativeLayout rl_reply_2;
		RelativeLayout rl_reply_3;
		RelativeLayout rl_reply_4;
		LinearLayout ll_body;
		
	}
}