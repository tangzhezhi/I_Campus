package com.stinfo.pushme.fragment;

import java.util.ArrayList;
import org.joda.time.DateTime;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.TimetableWeekAdapter;
import com.stinfo.pushme.adapter.TimetableWeekAdapter.ImgTextWrapper;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.TimetableDBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Timetable;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryTimetableReq;
import com.stinfo.pushme.rest.entity.QueryTimetableResp;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;

public class TimetableWeekFragment  extends Fragment {
	private String TAG = "TimetableWeekFragment";
	private View mView;
    private GridView gridView;  
    private ArrayList<Timetable> timetableList ;
	private TimetableWeekAdapter timetableWeekAdapter;
	private ProgressDialog prgDialog = null;
	
	public static Fragment newInstance() {
		TimetableWeekFragment newFragment = new TimetableWeekFragment();
		return newFragment;
	}
	
	
	public TimetableWeekAdapter getTimetableWeekAdapter() {
		return timetableWeekAdapter;
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


	public void setTimetableWeekAdapter(TimetableWeekAdapter timetableWeekAdapter) {
		this.timetableWeekAdapter = timetableWeekAdapter;
	}


	public ArrayList<Timetable> getTimetableList() {
		return timetableList;
	}


	public void setTimetableList(ArrayList<Timetable> timetableList) {
		this.timetableList = timetableList;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showProgressDialog();
	}
	
	
	/**
	 * 实例化并渲染视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_timetable_week, container, false);
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
	}
	
	
	/**
	 * 初始化数据，初始化界面
	 */
	private void initData() {
		gridView = (GridView) mView.findViewById(R.id.timetable_week_gridview);  
		TimetableDBAdapter dbAdapter = new TimetableDBAdapter();
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		ArrayList<Timetable> list = new ArrayList<Timetable>();
		try {
			dbAdapter.open();
			loadTimetableWeekFromRemote(0);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		 
		timetableWeekAdapter = new TimetableWeekAdapter(this.getActivity(),list);
		gridView.setAdapter(timetableWeekAdapter);
		timetableWeekAdapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				Log.d(TAG, "点击了"+pos);
				ImgTextWrapper itw = (ImgTextWrapper) v.getTag();
				Log.d(TAG, "点击了名称：："+itw.getTextView().getText());
				switch (pos) {
				}
			}
		});
	}
	
	
	
	/**
	 * 向服务器发起请求，获得数据
	 * @param week
	 */
	private void loadTimetableWeekFromRemote(int week){
		UserCache userCache = UserCache.getInstance();
		QueryTimetableReq reqData = new QueryTimetableReq();
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());

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
			QueryTimetableResp respData = new QueryTimetableResp(response);
			if (respData.getAck() == Ack.SUCCESS) {
				doSuccess(respData);
				closeProgressDialog();
			} else if (respData.getAck() != Ack.SUCCESS) {
				closeProgressDialog();
				MessageBox.showAckError(getActivity(), respData.getAck());
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryTimetableResp respData) {
		gridView = (GridView) mView.findViewById(R.id.timetable_week_gridview);  
		TimetableDBAdapter dbAdapter = new TimetableDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addTimetable(respData.getTimetableEntityList());
			ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
			ArrayList<Timetable> list = new ArrayList<Timetable>();
			list = dbAdapter.getTimetableOfWeek(classInfo.getClassId(),  DateTime.now().getWeekOfWeekyear());
			timetableWeekAdapter = new TimetableWeekAdapter(this.getActivity(),list);
			gridView.setAdapter(timetableWeekAdapter);
			timetableWeekAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	
}
