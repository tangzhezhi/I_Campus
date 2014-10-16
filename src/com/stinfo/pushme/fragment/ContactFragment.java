package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRoster;
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
import com.stinfo.pushme.util.FragmentSwitcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener {
	private static final String TAG = "ContactFragment";
	private TeacherListFragment mTeacherFrag;
	private StudentListFragment mStudentFrag;
	private ParentListFragment mParentFrag;
	private GroupListFragment mGroupFrag;
	private Fragment mCurrentFrag;
	private RadioGroup mRadioGroup;
	private MenuItem mSearchItem;
	private MenuItem mRefreshItem;
	private View mView;

	public static ContactFragment newInstance() {
		ContactFragment newFragment = new ContactFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_contact, container, false);
		initView();
		return mView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.contact, menu);
		mSearchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
		searchView.setOnQueryTextListener(this);
		
		mRefreshItem= menu.findItem(R.id.action_refresh);
		
		mRefreshItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				if(mCurrentFrag instanceof TeacherListFragment){
					Log.d("mCurrentFrag", "当前是TeacherListFragment");
					UserCache userCache = UserCache.getInstance();
					if (userCache.getUserInfo() instanceof Teacher) {
						refreshSchoolTeacher();
					}
					else{
						refreshClassTeacher();
					}
					
				}
				else if(mCurrentFrag instanceof StudentListFragment){
					Log.d("mCurrentFrag", "当前是StudentListFragment");
					refreshStudent();
				}
				else if(mCurrentFrag instanceof ParentListFragment){
					Log.d("mCurrentFrag", "当前是ParentListFragment");
					refreshParent();
					
				}
				else if(mCurrentFrag instanceof GroupListFragment){
					Log.d("mCurrentFrag", "当前是GroupListFragment");
				}
//				getChildFragmentManager().beginTransaction().remove(mCurrentFrag).commit();
//				
//				getChildFragmentManager().beginTransaction().add(R.id.fragment_container, mCurrentFrag).commit();
				return false;
			}
			
		});
		
	}

	private void initView() {
		mTeacherFrag = TeacherListFragment.newInstance();
//		mStudentFrag = StudentListFragment.newInstance();
		mParentFrag = ParentListFragment.newInstance();
//		mGroupFrag = GroupListFragment.newInstance();

		mRadioGroup = (RadioGroup) mView.findViewById(R.id.group_contact);
		mRadioGroup.setOnCheckedChangeListener(listener);

		getChildFragmentManager().beginTransaction().add(R.id.fragment_container, mTeacherFrag).commit();
		mCurrentFrag = mTeacherFrag;
	}

	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (group.getCheckedRadioButtonId()) {
			case R.id.rb_teacher:
				mSearchItem.setVisible(true);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mTeacherFrag);
				mCurrentFrag = mTeacherFrag;
				break;
//			case R.id.rb_student:
//				mSearchItem.setVisible(true);
//				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mStudentFrag);
//				mCurrentFrag = mStudentFrag;
//				break;
			case R.id.rb_parent:
				mSearchItem.setVisible(true);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mParentFrag);
				mCurrentFrag = mParentFrag;
				break;
//			case R.id.rb_group:
//				mSearchItem.setVisible(false);
//				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mGroupFrag);
//				mCurrentFrag = mGroupFrag;
//				break;
			}
		}
	};

	@Override
	public boolean onQueryTextChange(String query) {
		if (mTeacherFrag.isAdded()) {
			mTeacherFrag.onUserFilter(query);
		}
//		if (mStudentFrag.isAdded()) {
//			mStudentFrag.onUserFilter(query);
//		}
		if (mParentFrag.isAdded()) {
			mParentFrag.onUserFilter(query);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	private void refreshParent() {
		UserCache userCache = UserCache.getInstance();
		ParentListReq reqData = new ParentListReq();
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

							ParentListResp resp = new ParentListResp(response, classInfo);
							if ((resp.getAck() != Ack.SUCCESS) && resp.getAck() != Ack.NOT_FOUND) {
								return;
							}

							if (resp.getAck() == Ack.SUCCESS) {
								updateParent(resp.getParentList());
							}
						} catch (Exception e) {
							Log.d(TAG, "Exception: " + e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "VolleyError: " + error);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	
	
	
	private void refreshSchoolTeacher(){
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
								return;
							}

							if (resp.getAck() == Ack.SUCCESS) {
								updateTeacher(resp.getTeacherList());
							}
						} catch (Exception e) {
							Log.d(TAG, "Exception: " + e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "VolleyError: " + error);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	private void updateTeacher(ArrayList<TeacherRoster> list) {
		Log.d("[ContactFragment] updateTeacher: ", String.valueOf(list.size()));

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.deleteTeacherRoster();
			dbAdapter.addTeacherRoster(list);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			dbAdapter.close();
		}
	}
	
	
	private void refreshClassTeacher() {
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
									return;
								}

								if (resp.getAck() == Ack.SUCCESS) {
									updateTeacher(resp.getTeacherList());
								}
							} catch (Exception e) {
								Log.d(TAG, "refreshClassTeacher错误：："+e);
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d(TAG, "refreshClassTeacher VolleyError错误：："+error);
						}
					});
			RequestController.getInstance().addToRequestQueue(req, TAG);
		} catch (Exception e) {
			Log.e(TAG, "服务器请求异常：："+e);
			e.printStackTrace();
		}
	}

	
	private void updateParent(ArrayList<ParentRoster> list) {

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.deleteParentRoster();
			dbAdapter.addParentRoster(list);
		} finally {
			dbAdapter.close();
		}
	}

	private void updateStudent(ArrayList<StudentRoster> list) {

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.deleteStudentRoster();
			dbAdapter.addStudentRoster(list);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			dbAdapter.close();
		}
	}
	
	
	private void refreshStudent() {
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
								return;
							}

							if (resp.getAck() == Ack.SUCCESS) {
								updateStudent(resp.getStudentList());
							}
						} catch (Exception e) {
							Log.d(TAG, "refreshStudent错误：："+e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "refreshStudentVolleyError：："+error);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
}