package com.stinfo.pushme.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Score;

/**
 * 对通知数据进行填充、绑定，对应界面位置
 * @author lenovo
 *
 */
public class ScoreDetailAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<Score> scoreList;

	public ScoreDetailAdapter(Context context, ArrayList<Score> scoreList) {
		super();
		this.scoreList = scoreList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return scoreList.size();
	}

	@Override
	public Object getItem(int pos) {
		return scoreList.get(pos);
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
			convertView = mInflater.inflate(R.layout.item_score, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_score_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_score_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Score score = scoreList.get(pos);
		if (score != null) {
			
			
			if(score.getSubject()==null || score.getSubject().equals(null) || score.getSubject().equals("null")){
				score.setSubject("");
			}
			
			if(score.getSubject()==null || score.getSubject().equals(null) || score.getSubject().equals("null")){
				score.setSubject("");
			}
			
			if(score.getExamPaperScore()==null || score.getExamPaperScore().equals(null) || score.getExamPaperScore().equals("null")){
				score.setExamPaperScore("");
			}
			
			if(score.getGradeRanking()==null || score.getGradeRanking().equals(null) || score.getGradeRanking().equals("null")){
				score.setGradeRanking("");
			}
			
			if(score.getClassRanking()==null || score.getClassRanking().equals(null) || score.getClassRanking().equals("null")){
				score.setClassRanking("");
			}
			
			holder.tvTitle.setText(score.getSubject());
			holder.tvContent.setText("分数："+score.getExamPaperScore()
//					+
//					"\t\t 年级排名: "+score.getGradeRanking()+
//					"\t\t 班级排名: "+score.getClassRanking()
					);
		}

		return convertView;
	}

	public class ViewHolder {
		TextView tvTitle;
		TextView tvContent;
	}
}