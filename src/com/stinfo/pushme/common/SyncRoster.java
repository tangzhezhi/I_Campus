package com.stinfo.pushme.common;

import java.util.ArrayList;

import android.database.SQLException;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.TeacherRoster;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.ParentListReq;
import com.stinfo.pushme.rest.entity.ParentListResp;
import com.stinfo.pushme.rest.entity.SchoolTeacherListReq;
import com.stinfo.pushme.rest.entity.SchoolTeacherListResp;
import com.stinfo.pushme.rest.entity.StudentListReq;
import com.stinfo.pushme.rest.entity.StudentListResp;
import com.stinfo.pushme.rest.entity.TeacherListReq;
import com.stinfo.pushme.rest.entity.TeacherListResp;

/**
 * 同步花名册--根据用户信息来判断是否是同步学生还是家长还是老师
 * @author lenovo
 *
 */
public class SyncRoster {
	private static final String TAG = "SyncMyRoster";
	private OnTaskListener taskListener = null;

	public interface OnTaskListener {
		public void onSuccess();

		public void onFailure();
	}

	public SyncRoster(OnTaskListener taskListener) {
		this.taskListener = taskListener;
	}

	public void execSync() {
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.deleteMyRoster();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
			taskListener.onFailure();
			return;
		} finally {
			dbAdapter.close();
		}

		step1_syncStudent();
	}
	
	/**
	 * 老师切换班级的时候，当前classid需要更换，更新花名册人员
	 */
	private void step1_syncStudent() {
		UserCache userCache = UserCache.getInstance();
		StudentListReq reqData = new StudentListReq();
		
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response);
						try {
							UserCache userCache = UserCache.getInstance();
							ClassInfo classInfo = userCache.getCurrentClass();
							StudentListResp resp = new StudentListResp(response, classInfo);
							if ((resp.getAck() != Ack.SUCCESS) && resp.getAck() != Ack.NOT_FOUND) {
								taskListener.onFailure();
								return;
							}

							if (resp.getAck() == Ack.SUCCESS) {
								updateStudent(resp.getStudentList());
							}
							step2_syncParent();
						} catch (Exception e) {
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void step2_syncParent() {
		UserCache userCache = UserCache.getInstance();
		ParentListReq reqData = new ParentListReq();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		//更新老师所属班级的所有家长信息
		UserInfo userInfo = userCache.getUserInfo();
		if(userInfo instanceof Teacher){
			final Teacher teacher = (Teacher) userInfo;
			if (teacher.getTeacherRoleList().size() > 0) {
				for(int i = 0;i < teacher.getTeacherRoleList().size(); i++){
					reqData.setClassId( teacher.getTeacherRoleList().get(i).getClassId());
					final TeacherRole teacherRole = teacher.getTeacherRoleList().get(i);
					MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
									Log.d(TAG, "Response: " + response);
									try {
										UserCache userCache = UserCache.getInstance();
										userCache.getCurrentClass();
										ClassInfo classInfo = new ClassInfo();
										classInfo.setSchoolId(userCache.getCurrentClass().getSchoolId());
										classInfo.setClassId(teacherRole.getClassId());
										classInfo.setClassName(teacherRole.getClassName());
										//ClassInfo classInfo = teacher.getTeacherRoleList().get(i);
										ParentListResp resp = new ParentListResp(response, classInfo);
										if ((resp.getAck() != Ack.SUCCESS) && resp.getAck() != Ack.NOT_FOUND) {
											taskListener.onFailure();
											return;
										}
										if (resp.getAck() == Ack.SUCCESS) {
											updateParent(resp.getParentList());
										}
									} catch (Exception e) {
										taskListener.onFailure();
									}
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									taskListener.onFailure();
								}
							});
					RequestController.getInstance().addToRequestQueue(req, TAG);
				}
				//更新老师电话簿
				step3_syncSchoolTeacher();
			}
		}else{
			reqData.setClassId(userCache.getCurrentClass().getClassId());
			MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.d(TAG, "Response: " + response);
							try {
								UserCache userCache = UserCache.getInstance();
								ClassInfo classInfo = userCache.getCurrentClass();
	
								ParentListResp resp = new ParentListResp(response, classInfo);
								if ((resp.getAck() != Ack.SUCCESS) && resp.getAck() != Ack.NOT_FOUND) {
									taskListener.onFailure();
									return;
								}
								if (resp.getAck() == Ack.SUCCESS) {
									updateParent(resp.getParentList());
								}
								if (userCache.getUserInfo() instanceof Teacher) {
									step3_syncSchoolTeacher();
								} else {
									step3_syncClassTeacher();
								}
							} catch (Exception e) {
								taskListener.onFailure();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							taskListener.onFailure();
						}
					});
			RequestController.getInstance().addToRequestQueue(req, TAG);
		}
		
	}

	private void step3_syncClassTeacher() {
		UserCache userCache = UserCache.getInstance();
		TeacherListReq reqData = new TeacherListReq();
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());

		MyStringRequest req;
		try {
			req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.d(TAG, "Response: " + response);
							try {
								UserCache userCache = UserCache.getInstance();
								ClassInfo classInfo = userCache.getCurrentClass();
								TeacherListResp resp = new TeacherListResp(response, classInfo);
								if ((resp.getAck() != Ack.SUCCESS) && resp.getAck() != Ack.NOT_FOUND) {
									taskListener.onFailure();
									return;
								}

								if (resp.getAck() == Ack.SUCCESS) {
									updateTeacher(resp.getTeacherList());
								}
								taskListener.onSuccess();
							} catch (Exception e) {
								taskListener.onFailure();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							taskListener.onFailure();
						}
					});
			RequestController.getInstance().addToRequestQueue(req, TAG);
		} catch (Exception e) {
			Log.e(TAG, "服务器请求异常：："+e);
			e.printStackTrace();
		}
	}

	private void step3_syncSchoolTeacher() {
		UserCache userCache = UserCache.getInstance();
		SchoolTeacherListReq reqData = new SchoolTeacherListReq();
		reqData.setSchoolId(userCache.getCurrentClass().getSchoolId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		
		
		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.length());
						try {
							UserCache userCache = UserCache.getInstance();
							ClassInfo classInfo = userCache.getCurrentClass();
							SchoolTeacherListResp resp = new SchoolTeacherListResp(response,
									classInfo.getSchoolId());
							if ((resp.getAck() != Ack.SUCCESS) && resp.getAck() != Ack.NOT_FOUND) {
								taskListener.onFailure();
								return;
							}

							if (resp.getAck() == Ack.SUCCESS) {
								updateTeacher(resp.getTeacherList());
							}
							taskListener.onSuccess();
						} catch (Exception e) {
							Log.d(TAG, "Exception: " + e);
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "VolleyError: " + error);
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void updateStudent(ArrayList<StudentRoster> list) {
		Log.d("[SyncRoster] updateStudent: ", String.valueOf(list.size()));
//		for (StudentRoster roster : list) {
//			Log.d("[user: ]", roster.getUserName());
//		}

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addStudentRoster(list);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			dbAdapter.close();
		}
	}

	private void updateParent(ArrayList<ParentRoster> list) {
		Log.d("[SyncRoster] updateParent: ", String.valueOf(list.size()));
//		for (ParentRoster roster : list) {
//			Log.d("[user: ]", roster.getUserName());
//		}

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addParentRoster(list);
		} finally {
			dbAdapter.close();
		}
	}

	private void updateTeacher(ArrayList<TeacherRoster> list) {
		Log.d("[SyncRoster] updateTeacher: ", String.valueOf(list.size()));
//		for (TeacherRoster roster : list) {
//			Log.d("[user: ]", roster.getUserName());
//		}

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addTeacherRoster(list);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			dbAdapter.close();
		}
	}
}
