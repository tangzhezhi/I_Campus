package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.AttendanceActivity;
import com.stinfo.pushme.activity.BadDeedShowActivity;
import com.stinfo.pushme.activity.DailyActivity;
import com.stinfo.pushme.activity.GoodDeedShowActivity;
import com.stinfo.pushme.activity.HomeworkActivity;
import com.stinfo.pushme.activity.LoveShareActivity;
import com.stinfo.pushme.activity.NoticeActivity;
import com.stinfo.pushme.activity.ScoreActivity;
import com.stinfo.pushme.activity.TimetableActivity;
import com.stinfo.pushme.activity.VirtuousTeachActivity;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.entity.Course;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryCourseReq;
import com.stinfo.pushme.rest.entity.QueryCourseResp;
import com.stinfo.pushme.util.LineGridView;
import com.stinfo.pushme.util.MessageBox;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public class IndexGridFragment extends Fragment  {
	private ProgressDialog prgDialog = null;
	private String TAG = "IndexGridFragment";
	private View mView;
    private LineGridView gridView;  
	private ImageAdapter imageAdapter;
	private ArrayList<Course> courseList = null;
	
	public ImageAdapter getImageAdapter() {
		return imageAdapter;
	}

	public void setImageAdapter(ImageAdapter imageAdapter) {
		this.imageAdapter = imageAdapter;
	}
	
	private void showProgressDialog() {
		prgDialog = new ProgressDialog(getActivity());
		prgDialog.setMessage("正在获取课程信息...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}
	

	public static IndexGridFragment newInstance() {
		IndexGridFragment newFragment = new IndexGridFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	/**
	 * 实例化并渲染视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_index, container, false);
		gridView = (LineGridView) mView.findViewById(R.id.index_grid);  
		imageAdapter = new ImageAdapter(this.getActivity());
		gridView.setAdapter(imageAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				Log.d(TAG, "点击了"+pos);
				ImgTextWrapper itw = (ImgTextWrapper) v.getTag();
				Log.d(TAG, "点击了名称：："+itw.textView.getText());
				switch (pos) {
				case 0:
					onNoticeClick();
					break;
				case 1:
					onHomeWorkClick();
					break;
				case 2:
					onDailyClick();
					break;
				case 3:
					onTimetableClick();
					break;
				case 4:
					onScoreClick();
					break;
				case 5:
					onVirtuousTeachClick();
					break;
				case 6:
					onGoodDeedClick();
					break;	
				case 7:
					onBadDeedClick();
					break;
				case 8:
					onLoveShareClick();
					break;
				case 9:
					onAttendanceClick();
					break;
				}
			}
		});
		return mView;
	}
	
	private void onNoticeClick() {
		Intent intent = new Intent(getActivity(), NoticeActivity.class);
		startActivity(intent);
	}

	private void onDailyClick() {
		Intent intent = new Intent(getActivity(), DailyActivity.class);
		startActivity(intent);
	}

	private void onHomeWorkClick() {
		String type = "HomeWork";
		getCourse(type);
	}
	
	
	private void onGoodDeedClick() {
		Intent intent = new Intent(getActivity(), GoodDeedShowActivity.class);
		startActivity(intent);
	}
	
	private void onBadDeedClick() {
		Intent intent = new Intent(getActivity(), BadDeedShowActivity.class);
		startActivity(intent);
	}
	
	private void onLoveShareClick() {
		Intent intent = new Intent(getActivity(), LoveShareActivity.class);
		startActivity(intent);
	}
	
	public void getCourse(final String type) {
		QueryCourseReq reqData = new QueryCourseReq();
		showProgressDialog();
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							QueryCourseResp respData = new QueryCourseResp(response);
							if (respData.getAck() == Ack.SUCCESS) {
								courseList = respData.getCourseList();
								
								Intent intent = null;
								
								if(type.equals("HomeWork")){
									 intent = new Intent(getActivity(), HomeworkActivity.class);
								}
								else{
									Log.d(TAG, "不用查询课程");
								}
								
								if(courseList!=null){
									closeProgressDialog();
									ArrayList<String> cname = new ArrayList<String> ();
									ArrayList<String> ccode = new ArrayList<String> ();
									for(Course cs : courseList){
										cname.add(cs.getCoursename());
										ccode.add(cs.getCoursecode());
									}
									
									intent.putExtra("coursename", cname.toArray(new String[0]));   
									intent.putExtra("coursecode", ccode.toArray(new String[0]));
									startActivity(intent);
								}
								
								
							} else if (respData.getAck() != Ack.SUCCESS) {
								MessageBox.showAckError(getActivity(), respData.getAck());
							}
						} catch (Exception e) {
							MessageBox.showParserError(getActivity());
							Log.e(TAG, "Failed to parser response data! \r\n" + e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	

	private void onTimetableClick() {
		Intent intent = new Intent(getActivity(), TimetableActivity.class);
		startActivity(intent);
	}

	private void onScoreClick() {
		Intent intent = new Intent(getActivity(), ScoreActivity.class);
		startActivity(intent);
	}
	
	private void onVirtuousTeachClick() {
		Intent intent = new Intent(getActivity(), VirtuousTeachActivity.class);
		startActivity(intent);
	}
	
	private void onAttendanceClick() {
		Intent intent = new Intent(getActivity(), AttendanceActivity.class);
		startActivity(intent);
	}
	
	 class ImageAdapter extends BaseAdapter {  
		 private Context context;  
		   
		 public ImageAdapter(Context context) {  
		  this.context=context;  
		 }  
		   
//		// 定义图标数组  
//		    private int[] imageRes = {R.drawable.index_notice,R.drawable.index_homework,R.drawable.index_dialy,  
//		            R.drawable.index_timetable,R.drawable.index_score,R.drawable.index_attendance
//		            ,R.drawable.index_virtuous};  
//		    
//		    //定义标题数组  
//		    private String [] itemName = {getString(R.string.notice),getString(R.string.homework),
//		    		getString(R.string.daily),  getString(R.string.timetable),
//		    		getString(R.string.score),getString(R.string.attendance)
//		    		,getString(R.string.virtuous_teach)
//		         };  
		 
			// 定义图标数组  
		    private int[] imageRes = {R.drawable.index_notice,R.drawable.index_homework,R.drawable.index_dialy,  
		            R.drawable.index_timetable,R.drawable.index_score
		            ,R.drawable.index_virtuous
		            ,R.drawable.good_deed
		            ,R.drawable.bad_deed
		            ,R.drawable.love_share
		            ,R.drawable.safe_attendance
		            };  
		    
		    //定义标题数组  
		    private String [] itemName = {getString(R.string.notice),getString(R.string.homework),
		    		getString(R.string.daily),  getString(R.string.timetable),
		    		getString(R.string.score)
		    		,getString(R.string.virtuous_teach)	
		    		,getString(R.string.good_deed_show)
		    		,getString(R.string.bad_deed_show)
		    		,getString(R.string.love_share)
		    		,getString(R.string.safe_attendance_student)
		         };  
		 
		 
		 
		 //get the number  
		 @Override  
		 public int getCount() {  
		  return imageRes.length;  
		 }  
		 
		 @Override  
		 public Object getItem(int position) {  
		  return position;  
		 }  
		 
		 //get the current selector's id number  
		 @Override  
		 public long getItemId(int position) {  
		  return position;  
		 }  
		 
		 //create view method  
		 @Override  
		 public View getView(int position, View view, ViewGroup viewgroup) {  
		  ImgTextWrapper wrapper;  
		  if(view==null) {  
		   wrapper = new ImgTextWrapper();  
		   LayoutInflater inflater = LayoutInflater.from(context);  
		   view = inflater.inflate(R.layout.fragment_index_item, null);  
		   view.setTag(wrapper);  
		   view.setPadding(15, 15, 15, 15);  //每格的间距  
		  } else {  
		   wrapper = (ImgTextWrapper)view.getTag();  
		  }  
		    
		  wrapper.imageView = (ImageView)view.findViewById(R.id.index_image);  
		  wrapper.imageView.setBackgroundResource(imageRes[position]);  
		  wrapper.textView = (TextView)view.findViewById(R.id.index_text);  
		  wrapper.textView.setText(itemName[position]);  
		  return view;  
		 }  
		}  
		 
		class ImgTextWrapper {  
		 ImageView imageView;  
		 TextView textView;  
		   
		}
}
