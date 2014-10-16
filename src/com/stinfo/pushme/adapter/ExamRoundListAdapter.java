package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.ExamRound;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 对通知数据进行填充、绑定，对应界面位置
 * @author lenovo
 *
 */
public class ExamRoundListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<ExamRound> mExamRoundList = new ArrayList<ExamRound>();

	public ExamRoundListAdapter(Context context, ArrayList<ExamRound> examRoundList) {
		super();
		
		if(examRoundList!=null){
			mExamRoundList = examRoundList;
		}
		
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mExamRoundList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mExamRoundList.get(pos);
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
			convertView = mInflater.inflate(R.layout.item_examround, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_examround_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_examround_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ExamRound examRound = mExamRoundList.get(pos);
		if (examRound != null) {
			holder.tvTitle.setText(examRound.getExamRoundTitle());
			
//			UserCache userCache = UserCache.getInstance();
//			UserInfo userInfo = userCache.getUserInfo();
			
//			if(userInfo instanceof Parent){
//				holder.tvContent.setText("总分数："+examRound.getTotalScore()+
//						"\t\t 年级排名: "+examRound.getGradeTotalRanking()+
//						"\t\t 班级排名: "+examRound.getClassTotalRanking());
//			}
//			else if(userInfo instanceof Teacher){
//				holder.tvContent.setText(
//						"本班年级排名: "+examRound.getGradeTotalRanking());
//			}
//			else if(userInfo instanceof Student){
//				holder.tvContent.setText("总分数："+examRound.getTotalScore()+
//						"\t\t 年级排名: "+examRound.getGradeTotalRanking()+
//						"\t\t 班级排名: "+examRound.getClassTotalRanking());
//			}
//			else{
//				
//			}

		}

		return convertView;
	}

	public class ViewHolder {
		TextView tvTitle;
		TextView tvContent;
	}
}