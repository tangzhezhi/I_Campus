package com.stinfo.pushme.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.StudentScoreGridViewAdapter;
import com.stinfo.pushme.adapter.StudentScoreGridViewAdapter.ImgTextWrapper;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.ScoreDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ExamRound;
import com.stinfo.pushme.entity.StudentRoster;

public class ScoreTeacherDetailActivity  extends BaseActionBarFragmentActivity {
	private String TAG = "ScoreTeacherDetailActivity";
	private  ArrayList<ExamRound> examRoundList ;
	private int mIndex = 0;
	private String userType;
	private GridView gridView;
	private StudentScoreGridViewAdapter studentScoreGridViewAdapter;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_teacher_detail);
		examRoundList = (ArrayList<ExamRound>) getIntent().getSerializableExtra("examRoundList");
		mIndex = getIntent().getIntExtra("index", 0);
		
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setTitle(getResources().getString(R.string.score_student_list));
		initView();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void initView() {
		gridView = (GridView) findViewById(R.id.score_teacher_detail_gridview);
		
		ScoreDBAdapter dbAdapter = new ScoreDBAdapter();
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		ArrayList<StudentRoster> list = new ArrayList<StudentRoster>();
		try {
			dbAdapter.open();
			list = dbAdapter.getClassStudentRoster(classInfo.getClassId());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		 
		String examRoundId = examRoundList.get(mIndex).getExamRoundId();
		
		studentScoreGridViewAdapter = new StudentScoreGridViewAdapter(this,list,examRoundId);
		gridView.setAdapter(studentScoreGridViewAdapter);
		studentScoreGridViewAdapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				try {
					ImgTextWrapper itw = (ImgTextWrapper) v.getTag();
					String studentId  = itw.getTextViewUserId().getText().toString();
					Log.d(TAG, "点击了名称：："+itw.getTextView().getText());
					switch (arg0.getId()) {
					case R.id.score_teacher_detail_gridview:
						Intent intent = new Intent();
					    intent.setClass(ScoreTeacherDetailActivity.this, ScoreDetailActivity.class);  
						Bundle bundle = new Bundle();
						bundle.putSerializable("examRoundList", examRoundList);
						bundle.putInt("index", mIndex);
						bundle.putString("studentId", studentId);
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
}
