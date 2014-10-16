package com.stinfo.pushme.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.NoticeType;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.HomeworkDBAdapter;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.util.DateTimeUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeworkListAdapter extends MyBaseAdatper {

	private HashMap<String, Integer> mapIcon = new HashMap<String, Integer>();
	private LayoutInflater mInflater;
	private ArrayList<Homework> mHomeworkList;
	private Button curDel_btn;
	
	
	public HomeworkListAdapter(Context context, ArrayList<Homework> homeworkList) {
		super();
		mHomeworkList = homeworkList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mapIcon.put("全部", R.drawable.work_qita);
		mapIcon.put("0017", R.drawable.work_yuwen);
		mapIcon.put("0002", R.drawable.work_yinyu);
		mapIcon.put("0006", R.drawable.work_lishi);
		mapIcon.put("0007", R.drawable.work_shengwu);
		mapIcon.put("0003", R.drawable.work_zhenzhi);
		mapIcon.put("0015", R.drawable.work_dili);
		mapIcon.put("0005", R.drawable.work_huaxue);
		mapIcon.put("0016", R.drawable.work_shuxue);
		mapIcon.put("0004", R.drawable.work_wuli);
		
		mapIcon.put("0018", R.drawable.work_banhui);
		mapIcon.put("0012", R.drawable.work_huodong);
		mapIcon.put("0019", R.drawable.work_kexue);
		mapIcon.put("0009", R.drawable.work_meishu);
		mapIcon.put("0022", R.drawable.work_pinshe);
		mapIcon.put("0020", R.drawable.work_pinsheng);
		mapIcon.put("0001", R.drawable.work_tiyu);
		
		mapIcon.put("0021", R.drawable.work_xiaoben);
		mapIcon.put("0013", R.drawable.work_xiezi);
		mapIcon.put("0010", R.drawable.work_xinxi);
		mapIcon.put("0008", R.drawable.work_yinyue);
		mapIcon.put("0011", R.drawable.work_zhouhui);
	}

	@Override
	public int getCount() {
		return mHomeworkList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mHomeworkList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_homework, null);

			holder = new ViewHolder();
			holder.ivSubject = (ImageView) convertView.findViewById(R.id.iv_subject);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_create_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.btnDel = (Button) convertView.findViewById(R.id.homework_del);
			curDel_btn = holder.btnDel;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Homework homework = mHomeworkList.get(pos);
		if (homework != null) {
			int resId = 0;
			
			if(mapIcon.get(homework.getSubject())==null){
				resId = R.drawable.work_qita;
			}
			else{
				resId = mapIcon.get(homework.getSubject());
			}
			
			holder.ivSubject.setImageResource(resId);
			
			String tvCreateTime = "";
			
			if(homework.getCreateTime()!=null && homework.getCreateTime().length() > 10){
				
				DateTime dt = new DateTime(homework.getCreateTime().substring(0,10));
				
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
					tvCreateTime = homework.getCreateTime();
				}
			}
			
			holder.tvTime.setText(tvCreateTime);
			holder.tvContent.setText(homework.getContent());
		}
		
		
		// 为删除按钮添加监听事件，实现点击删除按钮时删除该项
		holder.btnDel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                        if (curDel_btn != null){
                            curDel_btn.setVisibility(View.GONE);
                            mHomeworkList.remove(homework);
                            
                        	HomeworkDBAdapter dbAdapter = new HomeworkDBAdapter();
                    		try {
                    			dbAdapter.open();
                    			dbAdapter.deleteMyHomework(homework.getCreateTime(), homework.getContent());
                    		} catch (Exception e) {
                    			Log.e("HomeworkDBAdapter", "Failed to operate database: " + e);
                    		} finally {
                    			dbAdapter.close();
                    		}
                            
                            notifyDataSetChanged();
                        }
                }
              
        });
		
		holder.btnDel.setVisibility(curDel_btn.getVisibility());
		return convertView;
	}

	final class ViewHolder {
		ImageView ivSubject;
		TextView tvTime;
		TextView tvContent;
		Button btnDel;
	}
}