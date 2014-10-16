package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.NoticeDetailActivity;
import com.stinfo.pushme.activity.ScoreDetailActivity;
import com.stinfo.pushme.activity.ScoreParentDetailActivity;
import com.stinfo.pushme.activity.ScoreTeacherDetailActivity;
import com.stinfo.pushme.activity.VirtuousTeachDetailActivity;
import com.stinfo.pushme.adapter.ExamRoundListAdapter;
import com.stinfo.pushme.adapter.StudentVirtuousTeachGridViewAdapter;
import com.stinfo.pushme.adapter.StudentVirtuousTeachGridViewAdapter.ImgTextWrapper;
import com.stinfo.pushme.common.AppConfig;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.NoticeQueryType;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.ExamRoundDBAdapter;
import com.stinfo.pushme.db.VirtuousTeachDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ExamRound;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.entity.VirtuousTeach;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryExamRoundReq;
import com.stinfo.pushme.rest.entity.QueryExamRoundResp;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

public class VirtuousTeachParentFragment  extends Fragment  {
	private String TAG = "VirtuousTeachParentFragment";
	private View mView;
	private String userType;
	private GridView gridView;
	private StudentVirtuousTeachGridViewAdapter studentVirtuousTeachGridViewAdapter;
	private String endDate;
	private String startDate;
	
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public static Fragment newInstance() {
		VirtuousTeachParentFragment newFragment = new VirtuousTeachParentFragment();
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
		mView = inflater.inflate(R.layout.activity_virtuous_teach_teacher_detail, container, false);
		return mView;
	}
	
	
	/**
	 * 当被其他程序打断也会重新调用进入
	 */
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().finish();
	}
	
	
	/**
	 * 初始化数据，初始化界面
	 */
	private void initData() {
		gridView = (GridView) getView().findViewById(R.id.virtuous_teach_teacher_detail_gridview);
		
		DBAdapter dbAdapter = new DBAdapter();
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		ArrayList<StudentRoster> list = new ArrayList<StudentRoster>();
		try {
			dbAdapter.open();
			list = dbAdapter.getClassStudentRosterOfParent(classInfo.getClassId(),UserCache.getInstance().getUserInfo().getUserId());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		
		studentVirtuousTeachGridViewAdapter = new StudentVirtuousTeachGridViewAdapter(getActivity(),list);
		gridView.setAdapter(studentVirtuousTeachGridViewAdapter);
		studentVirtuousTeachGridViewAdapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				try {
					ImgTextWrapper itw = (ImgTextWrapper) v.getTag();
					String studentId  = itw.getTextViewUserId().getText().toString();
					String studentName = itw.getTextView().getText().toString();
					Log.d(TAG, "点击了名称：："+itw.getTextView().getText());
					switch (arg0.getId()) {
					case R.id.virtuous_teach_teacher_detail_gridview:
						Intent intent = new Intent();
					    intent.setClass(getActivity(), VirtuousTeachDetailActivity.class);  
					    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Bundle bundle = new Bundle();
						bundle.putString("studentId", studentId);
						bundle.putString("studentName", studentName);
						bundle.putString("userType", String.valueOf(UserType.PARENT));
						intent.putExtras(bundle);
						startActivity(intent);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "异常：" + e);
				}
			}
		});
		
	}
	
}
