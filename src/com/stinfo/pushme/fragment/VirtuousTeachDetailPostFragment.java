package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.UserType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.VirtuousTeachDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.entity.VirtuousTeach;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.AddVirtuousTeachReq;
import com.stinfo.pushme.rest.entity.AddVirtuousTeachResp;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachReq;
import com.stinfo.pushme.rest.entity.QueryVirtuousTeachResp;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;

/**
 * 道德币提交页面
 * @author lenovo
 *
 */
public final class VirtuousTeachDetailPostFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "VirtuousTeachDetailPostFragment";
	private String userType;
	private String studentId;
	private String teacherCurrentCoin="";
	private Button addVirtuous;
	private EditText addEditText;
	private EditText scoreEditText;
	private EditText reasonEditText;
	private Button currentCoinBtn;
	private String studentName;
	private View mView;
	private String virtousType="1";
	private String startDate="";
	private String endDate="";
	
	private static final String[] m_virtousType = { "劳动小能手", "学习小明星" };   //定义数组
	
	private  String[] m_virtousName = null;   //定义数组
	private  String[] m_virtousCode = null;   //定义数组
	private String[] m_virtousRemark = null;   //定义数组
	private  String[] m_virtousVal = null;   //定义数组
	
	private int selectIndex = 0;
	
	
	
	public String getVirtousType() {
		return virtousType;
	}

	public void setVirtousType(String virtousType) {
		this.virtousType = virtousType;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	public static VirtuousTeachDetailPostFragment newInstance() {
		VirtuousTeachDetailPostFragment newFragment = new VirtuousTeachDetailPostFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		studentId =  getActivity().getIntent().getStringExtra("studentId");
		studentName =  getActivity().getIntent().getStringExtra("studentName");
		userType =  getActivity().getIntent().getStringExtra("userType");
		teacherCurrentCoin =  getActivity().getIntent().getStringExtra("teacherCurrentCoin");
		
		
		startDate=  getActivity().getIntent().getStringExtra("startDate");
		endDate=  getActivity().getIntent().getStringExtra("endDate");
		
		m_virtousName =  getActivity().getIntent().getStringArrayExtra("virtousRuleName");
		m_virtousCode =  getActivity().getIntent().getStringArrayExtra("virtousRuleId");
		m_virtousRemark =  getActivity().getIntent().getStringArrayExtra("virtousRemark");
		m_virtousVal =  getActivity().getIntent().getStringArrayExtra("virtousVal");
		
		mView = inflater.inflate(R.layout.fragment_virtuous_teach_post, container, false);
		return mView;
	}
	
	private void initView() {
		final UserCache userCache = UserCache.getInstance();
		final UserInfo userInfo = UserCache.getInstance().getUserInfo();
	    
		 final Spinner spinner =(Spinner)getActivity().findViewById(R.id.sp_virtuous_type);//下拉菜单的ID  
			spinner.setPrompt("请选择" ); 
			ArrayAdapter<?> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, m_virtousName);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		    spinner.setAdapter(adapter);  
		    spinner.setSelection(0,true);
		    spinner.performClick(); 
		    spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 

		    	@Override 
		    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
		    		 Log.d(TAG,"点击了："+ position+":::"+ spinner.getSelectedItem().toString());
		    		 virtousType = String.valueOf(position+1);
		    		 selectIndex = position;
		    		 
	    			if(m_virtousVal!=null && m_virtousVal.length > 0){
	    				scoreEditText.setText(m_virtousVal[selectIndex]);
	    				reasonEditText.setText(m_virtousRemark[selectIndex]);
	    			}
		    		 
		    		 spinner.setSelection(position);
		    		 parent.setVisibility(View.VISIBLE);
		    	} 

		    	@Override 
		    	public void onNothingSelected(AdapterView<?> arg0) { 
		    		 	spinner.setSelection(0);
		    		} 
		    	}); 
		
		
		addVirtuous = (Button) getActivity().findViewById(R.id.btn_virtuous_add);  
		currentCoinBtn = (Button) getActivity().findViewById(R.id.btn_virtuous_coin);  
		addEditText=  (EditText) getActivity().findViewById(R.id.et_virtuous_add_content);  
		addEditText.clearFocus();
		InputMethodManager imm = (InputMethodManager)addEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(addEditText.getWindowToken(),0);
		
		
		scoreEditText =  (EditText) getActivity().findViewById(R.id.et_virtuous_add_score);  
		reasonEditText =  (EditText) getActivity().findViewById(R.id.et_virtuous_add_reason_content);  
		
		currentCoinBtn.setText(userInfo.getUserName()+":"+getString(R.string.virtuous_coin)+":"+teacherCurrentCoin);
		
		
		if(m_virtousVal!=null && m_virtousVal.length > 0){
			scoreEditText.setText(m_virtousVal[0]);
			reasonEditText.setText(m_virtousRemark[0]);
		}
		
		
//		VirtuousTeachDBAdapter dbAdapter = new VirtuousTeachDBAdapter();
		ClassInfo classInfo = userCache.getCurrentClass();
		addVirtuous.setOnClickListener(new OnClickListener() {  
	          @Override  
	          public void onClick(View v) {  
	        	  
	        	  if(("1").equals(userCache.getCurrentTeacherRole().getClassmaster())){
	        		  onAddVirtuousTeach();
	        	  }
	        	  else{
	        		  Toast.makeText(getActivity(), "您不是班主任，不能分配", Toast.LENGTH_LONG).show();
	        	  }
	               
	          }

          } );
	}
	
	
	private void onAddVirtuousTeach() {
		String virtuousCoin = addEditText.getText().toString();
		String virtuousType = this.virtousType;
		String virtuousScore = scoreEditText.getText().toString();
		String virtuousReason = reasonEditText.getText().toString();
		
		if(virtuousCoin==null || ("").equals(virtuousCoin) ){
			Toast.makeText(getActivity(), "您没有添加道德币", Toast.LENGTH_LONG).show();
		}
		else if(Integer.valueOf(DateTimeUtil.getDateToYmd(endDate))<Integer.valueOf(DateTimeUtil.getToday())){
			Toast.makeText(getActivity(), "已经超过截止分配时间，请联系管理员", Toast.LENGTH_LONG).show();
		}
		else{
			addVirtuousTeachDetailToRemote(virtuousCoin,virtuousType,virtuousScore,virtuousReason);
		}
	} 
	
	
	private void addVirtuousTeachDetailToRemote(String virtuousCoin,String virtousType,String virtousScore,String virtousReason) {
		UserCache userCache = UserCache.getInstance();
		AddVirtuousTeachReq reqData = new AddVirtuousTeachReq();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		
		reqData.setSessionkey(UserCache.getInstance().getSessionKey());
		
		reqData.setUserId(userInfo.getUserId());
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setSchoolId(userCache.getCurrentClass().getSchoolId());
		reqData.setUserType(String.valueOf(UserType.TEACHER)); //老师
		reqData.setCoin(virtuousCoin);
		reqData.setStudentId(this.studentId);
		reqData.setVirtousType(virtousType);
		reqData.setVirtousScore(virtousScore);
		reqData.setVirtousReason(virtousReason);
		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkAddCoinResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(getActivity());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkAddCoinResponse(String response) {
		try {
			AddVirtuousTeachResp respData = new AddVirtuousTeachResp(response);
			if (respData.getAck()== Ack.SUCCESS) {
				 Toast.makeText(getActivity(), "分配成功", Toast.LENGTH_LONG).show();
				 updateTeacherCurrentCoin();
				 
			} else if (respData.getAck()!= Ack.SUCCESS) {
				 Toast.makeText(getActivity(), "服务器异常：分配不成功", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void updateTeacherCurrentCoin(){
		VirtuousTeachDBAdapter dbAdapter = new VirtuousTeachDBAdapter();
		try {
			dbAdapter.open();
			ArrayList<VirtuousTeach> listTeacher = new ArrayList<VirtuousTeach>();
			UserCache userCache = UserCache.getInstance();
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			
			VirtuousTeach vt = new VirtuousTeach();
			vt.setUserId(userInfo.getUserId());
			
			if(teacherCurrentCoin==null || ("").equals(teacherCurrentCoin)){
				teacherCurrentCoin = "0";
			}
			
			String tempAdd = addEditText.getText().toString();
			
			if(tempAdd==null || ("").equals(tempAdd)){
				tempAdd = "0";
			}
			else{
				int currentCoin = Integer.valueOf(teacherCurrentCoin) - Integer.valueOf(tempAdd);
				vt.setCoin(String.valueOf(currentCoin));
				vt.setCreateTime(DateTimeUtil.getCompactTime());
				vt.setUserType(String.valueOf(UserType.TEACHER));
				vt.setClassId(userCache.getCurrentClass().getClassId());
				vt.setStartDate(startDate);
				vt.setEndDate(endDate);
				listTeacher.add(vt);
				dbAdapter.addVirtuousTeach(listTeacher);
				currentCoinBtn.setText(userInfo.getUserName()+":"+getString(R.string.virtuous_coin)+":"+currentCoin);
				teacherCurrentCoin = String.valueOf(currentCoin);
				addEditText.setText("");
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	/*
	private void loadVirtuousTeachDetailFromRemote(String userId,String classId,String schoolId,String userType) {
		UserCache userCache = UserCache.getInstance();
		QueryVirtuousTeachReq reqData = new QueryVirtuousTeachReq();
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		reqData.setUserId(userId);
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setSchoolId(schoolId);
		reqData.setUserType(userType);
		
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
		VirtuousTeachDBAdapter dbAdapter = new VirtuousTeachDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addVirtuousTeach(respData.getVirtuousTeachList());
			
			ArrayList<VirtuousTeach> listTeacher = new ArrayList<VirtuousTeach>();
			UserCache userCache = UserCache.getInstance();
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			
			VirtuousTeach vt = new VirtuousTeach();
			vt.setUserId(userInfo.getUserId());
			
			if(teacherCurrentCoin==null || ("").equals(teacherCurrentCoin)){
				teacherCurrentCoin = "0";
			}
			
			String tempAdd = addEditText.getText().toString();
			
			if(tempAdd==null || ("").equals(tempAdd)){
				tempAdd = "0";
			}
			else{
				int currentCoin = Integer.valueOf(teacherCurrentCoin) - Integer.valueOf(tempAdd);
				vt.setCoin(String.valueOf(currentCoin));
				vt.setCreateTime(DateTimeUtil.getCompactTime());
				vt.setUserType(String.valueOf(UserType.TEACHER));
				vt.setClassId(userCache.getCurrentClass().getClassId());
				vt.setStartDate(startDate);
				vt.setEndDate(endDate);
				listTeacher.add(vt);
				dbAdapter.addVirtuousTeach(listTeacher);
				currentCoinBtn.setText(userInfo.getUserName()+":"+getString(R.string.virtuous_coin)+":"+currentCoin);
				teacherCurrentCoin = String.valueOf(currentCoin);
				addEditText.setText("");
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	*/
	
	
	/**
	 * 当被其他程序打断也会重新调用进入
	 */
	@Override
	public void onResume() {
		super.onResume();
		initView();
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	
	/**
	 * 点击记录时，跳转到详细界面
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
	
		}
	}
}
