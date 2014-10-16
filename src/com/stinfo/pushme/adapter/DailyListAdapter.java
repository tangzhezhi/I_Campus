package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Daily;
import com.stinfo.pushme.util.DateTimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DailyListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<Daily> mDailyList;

	public DailyListAdapter(Context context, ArrayList<Daily> dailyList) {
		super();
		mDailyList = dailyList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mDailyList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mDailyList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_daily, null);

			holder = new ViewHolder();
			holder.ivUserAvatar = (ImageView) convertView.findViewById(R.id.iv_user_avatar);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.tvCreateTime = (TextView) convertView.findViewById(R.id.tv_create_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Daily daily = mDailyList.get(pos);
		if (daily != null) {
			holder.tvUserName.setText(daily.getReceiverName());
			holder.tvCreateTime.setText(daily.getCreateTime());
			holder.tvContent.setText(daily.getContent());
		}

		return convertView;
	}

	final class ViewHolder {
		ImageView ivUserAvatar;
		TextView tvUserName;
		TextView tvCreateTime;
		TextView tvContent;
	}
}