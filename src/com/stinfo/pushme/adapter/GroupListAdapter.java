package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupListAdapter extends MyBaseAdatper {
	private LayoutInflater mInflater;
	private ArrayList<Group> mGroupList = null;

	public GroupListAdapter(Context context, ArrayList<Group> list) {
		super();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupList = list;
	}

	@Override
	public int getCount() {
		return mGroupList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mGroupList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_group, null);
			holder = new ViewHolder();
			holder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvGroupName.setText(mGroupList.get(pos).getGroupName());
		return convertView;
	}

	final class ViewHolder {
		public TextView tvGroupName;
	}
}