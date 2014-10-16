package com.stinfo.pushme.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.NoticeType;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.TimetableDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.entity.Timetable;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryTimetableReq;
import com.stinfo.pushme.rest.entity.QueryTimetableResp;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;

public class TimetableWeekAdapter extends BaseAdapter {
	private String TAG = "TimetableWeekAdapter";
	private Context context;
	private int currentWeeks;
	private String currentDays;
	private ArrayList<Timetable> mTimetableList ;
	public TimetableWeekAdapter() {
		super();
	}
	
	public TimetableWeekAdapter(Context c, ArrayList<Timetable>  timetableList) {
		super();
		mTimetableList = timetableList;
		this.context = c;
	}

	public int getCurrentWeeks() {
		return currentWeeks;
	}

	public void setCurrentWeeks(int currentWeeks) {
		this.currentWeeks = currentWeeks;
	}

	public String getCurrentDays() {
		return currentDays;
	}

	public void setCurrentDays(String currentDays) {
		this.currentDays = currentDays;
	}

	public TimetableWeekAdapter(Context context) {
		this.context = context;
	}
	
	private List<Timetable> classItems;
	
	public List<Timetable> getClassItems() {
		return classItems;
	}

	public void setClassItems(List<Timetable> classItems) {
		this.classItems = classItems;
	}

	// get the number
	@Override
	public int getCount() {
		return 8*12;
	}

	@Override
	public Object getItem(int position) {
		return mTimetableList.get(position);
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
					.inflate(R.layout.fragment_timetable_week_item, null);
			view.setTag(wrapper);
			view.setPadding(1, 1, 1, 1); // 每格的间距

		} else {
			wrapper = (ImgTextWrapper) view.getTag();
		}

		wrapper.textView = (TextView) view
				.findViewById(R.id.timetable_week_item_text);
		
		
		if(mTimetableList!=null && mTimetableList.size() > 0){
			
			if(mTimetableList.size() > position){
				final Timetable timetable = mTimetableList.get(position);
				
				if (timetable != null) {
					wrapper.textView.setText(timetable.getContent());
				}
			}

		}
		return view;
	}
	
	
	public class ImgTextWrapper {
		TextView textView;

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
		
	}

}
