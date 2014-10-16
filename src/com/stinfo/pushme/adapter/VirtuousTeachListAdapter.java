package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.entity.VirtuousTeach;

/**
 * 对通知数据进行填充、绑定，对应界面位置
 * @author lenovo
 *
 */
public class VirtuousTeachListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<VirtuousTeach> mVirtuousTeachList;

	public VirtuousTeachListAdapter(Context context, ArrayList<VirtuousTeach> virtuousTeachList) {
		super();
		mVirtuousTeachList = virtuousTeachList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mVirtuousTeachList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mVirtuousTeachList.get(pos);
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
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_virtuous_teach, null);
			holder = new ViewHolder();
			holder.tvCoin = (TextView) convertView.findViewById(R.id.tv_virtuous_teach_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_virtuous_teach_content);
			holder.tvUserId = (TextView) convertView.findViewById(R.id.tv_virtuous_teach_user_id);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		VirtuousTeach virtuousTeach = mVirtuousTeachList.get(pos);
		if (virtuousTeach != null) {
			
			
			String tvCreateTime = "";
			
			if(virtuousTeach.getRecorddate()!=null && virtuousTeach.getRecorddate().length() > 10){
				
				DateTime dt = new DateTime(virtuousTeach.getRecorddate().substring(0,10));
				
				if(dt.getDayOfYear()== DateTime.now().getDayOfYear()){
					tvCreateTime = "今天";
				}
				else if((DateTime.now().getDayOfYear() - dt.getDayOfYear()) < 7){
					if(dt.getDayOfWeek()==1){
						tvCreateTime = "周一";
					}
					else if(dt.getDayOfWeek()==2){
						tvCreateTime = "周二";
					}
					else if(dt.getDayOfWeek()==3){
						tvCreateTime = "周三";
					}
					else if(dt.getDayOfWeek()==4){
						tvCreateTime = "周四";
					}
					else if(dt.getDayOfWeek()==5){
						tvCreateTime = "周五";
					}
					else if(dt.getDayOfWeek()==6){
						tvCreateTime = "周六";
					}
					else if(dt.getDayOfWeek()==7){
						tvCreateTime = "周日";
					}
				}
				else{
					tvCreateTime =  virtuousTeach.getRecorddate();
				}
			}
			
			
			
			
			holder.tvCoin.setText("获得时间："+tvCreateTime);
			
			UserCache userCache = UserCache.getInstance();
			UserInfo userInfo = userCache.getUserInfo();
			
			if(virtuousTeach.getVirtousReason()==null || virtuousTeach.getVirtousReason().equals(null)){
				virtuousTeach.setVirtousReason("无");
			}
			
			holder.tvContent.setText(virtuousTeach.getUserName()+":奖励道德币："+virtuousTeach.getCoin()+"个\r\n"+"奖励理由："+virtuousTeach.getVirtousReason());

		}

		return convertView;
	}

	public class ViewHolder {
		TextView tvUserId;
		TextView tvCoin;
		TextView tvContent;
	}
}