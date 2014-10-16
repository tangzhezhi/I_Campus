package com.stinfo.pushme.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.VirtuousTeachDetailActivity;
import com.stinfo.pushme.adapter.StudentVirtuousTeachGridViewAdapter;
import com.stinfo.pushme.adapter.VirtuousTeachListAdapter;
import com.stinfo.pushme.adapter.StudentVirtuousTeachGridViewAdapter.ImgTextWrapper;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.db.VirtuousTeachDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.entity.Virtuous;
import com.stinfo.pushme.entity.VirtuousTeach;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.AddVirtuousTeachReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousRuleReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousRuleResp;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;

/**
 * 教师德育界面
 * @author lenovo
 *
 */
public class VirtuousTeachTeacherFragment  extends Fragment  {
	private String TAG = "VirtuousTeachTeacherFragment";
	private View mView;
	private ProgressDialog prgDialog = null;
	private String userType;
	private GridView gridView;
	private StudentVirtuousTeachGridViewAdapter studentVirtuousTeachGridViewAdapter;
	private String teacherCurrentCoin;
	private String endDate;
	private String startDate;
	private List<Virtuous> virtousList;
	
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
		VirtuousTeachTeacherFragment newFragment = new VirtuousTeachTeacherFragment();
		return newFragment;
	}
	
	private void showProgressDialog() {
		prgDialog = new ProgressDialog(getActivity());
		prgDialog.setMessage("正在获取数据中...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
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
		if(prgDialog!=null){
			prgDialog.hide();
			prgDialog.dismiss();
		}
		super.onDestroy();
		getActivity().finish();
	}
	
	

	private void QueryVirtuousTeachRuleFromRemote() {
		QueryVirtuousRuleReq reqData = new QueryVirtuousRuleReq();
		UserCache userCache = UserCache.getInstance();
		
		reqData.setSchoolno(userCache.getCurrentClass().getSchoolId());
		reqData.setSessionKey(userCache.getSessionKey());
		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							QueryVirtuousRuleResp respData = new QueryVirtuousRuleResp(response);
							if (respData.getAck() == Ack.SUCCESS) {
								virtousList = respData.getVirtuousList();
							} 
							else if (respData.getAck() != Ack.SUCCESS) {
								MessageBox.showAckError(getActivity(), respData.getAck());
							}
						} catch (Exception e) {
							MessageBox.showParserError(getActivity());
							Log.e(TAG, "Failed to parser response data! \r\n" + e);
						}
						
						finally{
							closeProgressDialog();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	
	
	
	
	
	
	private void QueryVirtuousTeachTeacherCoinFromRemote() {
//		prgDialog=ProgressDialog.show(getActivity(), "请稍等", "正在获取老师道德币..",true);
		UserCache userCache = UserCache.getInstance();
		QueryVirtuousTeachReq reqData = new QueryVirtuousTeachReq();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		reqData.setSessionkey(UserCache.getInstance().getSessionKey());
		reqData.setUserId(userInfo.getUserId());
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setSchoolId(userCache.getCurrentClass().getSchoolId());
		reqData.setUserType(String.valueOf(UserType.TEACHER));
		reqData.setFlag("1");
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
//						if(prgDialog!=null){
//							prgDialog.hide();
//							prgDialog.dismiss();
//						}
						MessageBox.showServerError(getActivity());
					}
				});
		
		RequestController.getInstance().addToRequestQueue(req, TAG);
		
	}
	
	
	private void checkResponse(String response) {
		try {
			QueryVirtuousTeachResp respData = new QueryVirtuousTeachResp(response);
			if (respData!=null) {
				doSuccess(respData);
			} 
		} catch (Exception e) {
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryVirtuousTeachResp respData) {
		try {
			
			if(respData!=null && respData.getVirtuousTeachList().size() > 0){
				VirtuousTeach virtuousTeach = respData.getVirtuousTeachList().get(0);
				teacherCurrentCoin = virtuousTeach.getCoin();
				startDate = virtuousTeach.getStartDate();
				endDate = virtuousTeach.getEndDate();
				
				try {
					QueryVirtuousTeachRuleFromRemote();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
//			dbAdapter.close();
		}
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
			list = dbAdapter.getClassStudentRoster(classInfo.getClassId());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		
		try {
			QueryVirtuousTeachTeacherCoinFromRemote();
		} catch (Exception e1) {
			e1.printStackTrace();
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
						bundle.putString("userType", "1");
						bundle.putString("startDate", startDate);
						bundle.putString("endDate", endDate);
						bundle.putString("teacherCurrentCoin", teacherCurrentCoin);
						
						if(virtousList!=null && virtousList.size() > 0){
							
							ArrayList<String> virtousRuleName = new ArrayList<String> ();
							ArrayList<String> virtousRuleId = new ArrayList<String> ();
							ArrayList<String> virtousRemark = new ArrayList<String> ();
							ArrayList<String> virtousVal = new ArrayList<String> ();
							
							for(Virtuous cs : virtousList){
								virtousRuleName.add(cs.getName());
								virtousRuleId.add(cs.getId());
								virtousRemark.add(cs.getRemark());
								virtousVal.add(cs.getVal());
							}
							
							if(virtousRuleName!=null && virtousRuleName.size() > 0){
								bundle.putStringArray("virtousRuleName", virtousRuleName.toArray(new String[0]));
								bundle.putStringArray("virtousRuleId", virtousRuleId.toArray(new String[0]));
								
								bundle.putStringArray("virtousRemark", virtousRemark.toArray(new String[0]));
								bundle.putStringArray("virtousVal", virtousVal.toArray(new String[0]));
							}
							
						}
						
						
					    if(teacherCurrentCoin==null || ("").equals(teacherCurrentCoin)){
						   Toast.makeText(getActivity(), "服务器异常：获取不到老师道德币", Toast.LENGTH_LONG).show();
					    }
					    else if(virtousList==null){
					    	Toast.makeText(getActivity(), "服务器异常：获取不到道德规则信息", Toast.LENGTH_LONG).show();
					    }
					    else{
							intent.putExtras(bundle);
							startActivity(intent);
					    }
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
