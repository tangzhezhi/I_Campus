package com.stinfo.pushme.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.ParentListReq;
import com.stinfo.pushme.rest.entity.ParentListResp;
import com.stinfo.pushme.rest.entity.PostHomeworkReq;
import com.stinfo.pushme.rest.entity.PostNoticeReq;
import com.stinfo.pushme.rest.entity.PushAccountReq;
import com.stinfo.pushme.rest.entity.QueryHomeworkReq;
import com.stinfo.pushme.rest.entity.QueryHomeworkResp;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryNoticeResp;
import com.stinfo.pushme.rest.entity.StudentListReq;
import com.stinfo.pushme.rest.entity.StudentListResp;
import com.stinfo.pushme.rest.entity.TeacherListReq;
import com.stinfo.pushme.rest.entity.TeacherListResp;
import com.stinfo.pushme.util.MessageBox;

public class Test {
	private final static String TAG = "TEST";

	public static void testPostHomeWork() {
		PostHomeworkReq reqData = new PostHomeworkReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setContent("掌上校园 吞吞吐吐");
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setSubject("语文");

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testGetTeacherList() {
		TeacherListReq reqData = new TeacherListReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId("121");

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							ClassInfo classInfo = new ClassInfo();
							classInfo.setClassId("121");
							classInfo.setClassName("fff");
							classInfo.setSchoolId("111");

							TeacherListResp resp = new TeacherListResp(response, classInfo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testGetStudentList() {
		StudentListReq reqData = new StudentListReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							ClassInfo classInfo = new ClassInfo();
							classInfo.setClassId("301");
							classInfo.setClassName("fff");
							classInfo.setSchoolId("111");

							StudentListResp resp = new StudentListResp(response, classInfo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testGetParentList() {
		ParentListReq reqData = new ParentListReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setClassId(userCache.getCurrentClass().getClassId());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							ClassInfo classInfo = new ClassInfo();
							classInfo.setClassId("121");
							classInfo.setClassName("fff");
							classInfo.setSchoolId("111");

							ParentListResp resp = new ParentListResp(response, classInfo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testPostNotice() {
		PostNoticeReq reqData = new PostNoticeReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setContent("掌上校园 吞吞吐吐");
		reqData.setObjectId(userCache.getCurrentClass().getClassId());
		reqData.setType(2);
		reqData.setTitle("我爱你");

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testQueryNotice() {
		QueryNoticeReq reqData = new QueryNoticeReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setQueryType(0);
		reqData.setQueryValue(userCache.getCurrentClass().getClassId());
		reqData.setLastCreateTime("201100000000");

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							QueryNoticeResp resp = new QueryNoticeResp(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testQueryHomeWork() {
		QueryHomeworkReq reqData = new QueryHomeworkReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setQueryType(1);
		reqData.setQueryValue(userCache.getCurrentClass().getClassId());
		reqData.setSubject("语文");
		reqData.setLastCreateTime("201100000000");

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						try {
							QueryHomeworkResp resp = new QueryHomeworkResp(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	public static void testPushAccount() {
		PushAccountReq reqData = new PushAccountReq();
		UserCache userCache = UserCache.getInstance();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setPushChannelId("3783279064268446300");
		reqData.setPushUserId("953949255552562703");
		reqData.setDeviceType(3);

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(MyApplication.getInstance().getApplicationContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
}
