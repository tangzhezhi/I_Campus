package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Sex;
import com.stinfo.pushme.entity.StudentRoster;

public class StudentVirtuousTeachGridViewAdapter extends BaseAdapter {
	private String TAG = "TeacherVirtuousTeachGridViewAdapter";
	private Context context;
	private ArrayList<StudentRoster> studentRosterList ;
	
	
	public StudentVirtuousTeachGridViewAdapter() {
		super();
	}
	
	public StudentVirtuousTeachGridViewAdapter(Context c, ArrayList<StudentRoster>  studentRosterList) {
		super();
		this.studentRosterList = studentRosterList;
		this.context = c;
	}

	public StudentVirtuousTeachGridViewAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return studentRosterList.size();
	}

	@Override
	public Object getItem(int position) {
		return studentRosterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// create view method
	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ImgTextWrapper wrapper;
		if (view == null) {
			wrapper = new ImgTextWrapper();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater
					.inflate(R.layout.activity_virtuous_teach_teacher_detail_item, null);
			view.setTag(wrapper);
			view.setPadding(1, 1, 1, 1); // 每格的间距

		} else {
			wrapper = (ImgTextWrapper) view.getTag();
		}

		wrapper.textView = (TextView) view
				.findViewById(R.id.student_virtuous_teach_item_text);
		
		wrapper.textViewUserId = (TextView) view
				.findViewById(R.id.student_virtuous_teach_userid_item_text);
		
		wrapper.imageView =  (ImageView) view
				.findViewById(R.id.student_virtuous_teach_item_image);
		
		if(studentRosterList!=null && studentRosterList.size() > 0){
			
			if(studentRosterList.size() > position){
				final StudentRoster studentRoster = studentRosterList.get(position);
				
				if (studentRoster != null) {
					wrapper.textView.setText(studentRoster.getUserName());
					wrapper.textViewUserId.setText(studentRoster.getUservalue());
					if(studentRoster.getSex().equals(Sex.MALE)){
						wrapper.imageView.setBackgroundResource(R.drawable.ic_sex_male);
					}
					else{
						wrapper.imageView.setBackgroundResource(R.drawable.ic_sex_female);
					}
					
				}
			}

		}
		return view;
	}
	
	
	public class ImgTextWrapper {
		TextView textView;
		TextView textViewUserId;
		ImageView imageView;
		
		public TextView getTextViewUserId() {
			return textViewUserId;
		}

		public void setTextViewUserId(TextView textViewUserId) {
			this.textViewUserId = textViewUserId;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}

		public ImageView getImageView() {
			return imageView;
		}

		public void setImageView(ImageView imageView) {
			this.imageView = imageView;
		}
		
	}

}
