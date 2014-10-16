package com.stinfo.pushme.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.UserHelper;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.StringMatcher;
import com.stinfo.pushme.util.URLChecker;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;



@SuppressLint("UseSparseArrays")
public class UserListAdapter extends MyBaseAdatper implements Filterable, SectionIndexer {
	private static final String TAG = "UserListAdapter";
	private static String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static int NORMAL_MODE = 1;
	public static int SELECT_MODE = 2;

	private HashMap<String, String> mSelectMap = new HashMap<String, String>();
	
	private   HashMap<Integer, Boolean> isSelected =  new HashMap<Integer, Boolean>();
	
	public  HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public  void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}

	private LayoutInflater mInflater;
	private ArrayList<UserInfo> mFilterList;
	private ArrayList<UserInfo> mOriginalList;
	private UserFilter mUserFilter = null;
	private int mMode;
	
	
	public UserListAdapter(Context context, ArrayList<UserInfo> list) {
		super();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOriginalList = new ArrayList<UserInfo>(list);
		mFilterList = list;
	}

	public UserListAdapter(Context context, ArrayList<UserInfo> list, int mode) {
		this(context, list);
		mMode = mode;
	}

	public void updateListData(ArrayList<UserInfo> list) {
		mOriginalList = new ArrayList<UserInfo>(list);
		mFilterList = list;
		
		for(int i = 0 ; i < mFilterList.size(); i++){
			isSelected.put(i, false);
		}
		notifyDataSetChanged();
	}

	public HashMap<String, String> getSelectMap() {
		return mSelectMap;
	}

	public void clearAll() {
		mSelectMap.clear();
		notifyDataSetChanged();
	}

	public void selectAll() {
		mSelectMap.clear();
		for (UserInfo userInfo : mFilterList) {
			mSelectMap.put(userInfo.getUserId(), userInfo.getUserName());
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFilterList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mFilterList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_user, null);

			holder = new ViewHolder();
			holder.ivUserAvatar = (ImageView) convertView.findViewById(R.id.iv_user_avatar);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.tvRemark = (TextView) convertView.findViewById(R.id.tv_remark);
			holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
			
			holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_user_phone);
			holder.tvBusiness = (TextView) convertView.findViewById(R.id.tv_user_business);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mMode == NORMAL_MODE) {
			holder.cbSelect.setVisibility(View.GONE);
		} else {
			holder.cbSelect.setVisibility(View.VISIBLE);
			for(int i = 0 ; i < pos+1 ; i++){
				holder.cbSelect.setChecked(isSelected.get(i)==null?false:isSelected.get(i)); 
			}
		}

		
		UserInfo userInfo = mFilterList.get(pos);
		if (userInfo != null) {
			try {
				if (URLChecker.isUrl(userInfo.getPicUrl())) {
					Log.d(TAG, "loadImage: " + userInfo.getPicUrl());
					ImageListener listener = ImageLoader.getImageListener(holder.ivUserAvatar,
							R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
					ImageCacheManager.getInstance().getImageLoader().get(userInfo.getPicUrl(), listener);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + userInfo.getPicUrl());
			}
			
			
			holder.tvUserName.setText(userInfo.getUserName());
			
			if(userInfo.getPhone().equals("null")||userInfo.getPhone().equals(null)){
				holder.tvPhone.setText("无号码");
			}
			else{
				holder.tvPhone.setText(userInfo.getPhone());
			}
			
			if(userInfo.getBusiness().equals("1")){
//				holder.tvBusiness.setText("已订");
			}
			else{
//				holder.tvBusiness.setText("未订");
			}
			
			
//			holder.cbSelect.setChecked(isSelected.get(pos)==null?false:isSelected.get(pos)); 
			if (userInfo instanceof StudentRoster) {
				holder.tvRemark.setVisibility(View.GONE);
			} else {
				holder.tvRemark.setText(UserHelper.getRemark(userInfo));
			}
			
//			if (mMode == SELECT_MODE) {
//				boolean checked = mSelectMap.containsKey(userInfo.getUserId());
//				holder.cbSelect.setChecked(checked);
//			}
		}
		
		return convertView;
	}

	final class MyCheckedChangeListener implements OnCheckedChangeListener {
		private int mPos = 0;

		public MyCheckedChangeListener(int pos) {
			mPos = pos;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			UserInfo userInfo = mFilterList.get(mPos);
			if (isChecked) {
				if (!mSelectMap.containsKey(userInfo.getUserId())) {
					mSelectMap.put(userInfo.getUserId(), userInfo.getUserName());
				}
			} else if (mSelectMap.containsKey(userInfo.getUserId())) {
				mSelectMap.remove(userInfo.getUserId());
			}
		}
	}

	public   class ViewHolder {
		ImageView ivUserAvatar;
		TextView tvUserName;
		TextView tvRemark;
		CheckBox cbSelect;
		TextView tvPhone;
		TextView tvBusiness;
		
		public ImageView getIvUserAvatar() {
			return ivUserAvatar;
		}
		public void setIvUserAvatar(ImageView ivUserAvatar) {
			this.ivUserAvatar = ivUserAvatar;
		}
		public TextView getTvUserName() {
			return tvUserName;
		}
		public void setTvUserName(TextView tvUserName) {
			this.tvUserName = tvUserName;
		}
		public TextView getTvRemark() {
			return tvRemark;
		}
		public void setTvRemark(TextView tvRemark) {
			this.tvRemark = tvRemark;
		}
		public CheckBox getCbSelect() {
			return cbSelect;
		}
		public void setCbSelect(CheckBox cbSelect) {
			this.cbSelect = cbSelect;
		}
		public TextView getTvBusiness() {
			return tvBusiness;
		}
		public void setTvBusiness(TextView tvBusiness) {
			this.tvBusiness = tvBusiness;
		}
		public TextView getTvPhone() {
			return tvPhone;
		}
		public void setTvPhone(TextView tvPhone) {
			this.tvPhone = tvPhone;
		}
		
		
	}

	@Override
	public Filter getFilter() {
		if (mUserFilter == null) {
			mUserFilter = new UserFilter(mOriginalList, mFilterList, this);
		}
		return mUserFilter;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				UserInfo userInfo = (UserInfo) getItem(j);
				if (i == 0) {
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(userInfo.getInitial(), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(userInfo.getInitial(), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}
}