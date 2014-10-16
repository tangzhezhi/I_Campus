package com.stinfo.pushme.common;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.common.AppConstant.MessageGroupType;
import com.stinfo.pushme.common.AppConstant.MessageObjectType;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Group;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.SerializeHelper;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;

/**
 * 主存用户信息、所属群组与登陆判断
 * @author lenovo
 *
 */
public final class UserCache {
	private static final String TAG = "UserCache";

	private static UserCache mInstance = null;
	private final static String DB_NAME = "logon.pref";
	private SharedPreferences.Editor mEditor;
	private SharedPreferences mSettings;
	private Context mContext;
	private ArrayList<String> mTagList = new ArrayList<String>();
	private ArrayList<Group> mGroupList = new ArrayList<Group>();
	private HashMap<String, String> mClassMap = new HashMap<String, String>();

	private boolean logon = false;
	private String sessionKey = "";
	private String password = "";
	private UserInfo userInfo = null;
	private ClassInfo currentClass = null;
	private Student currentChild = null;
	private TeacherRole currentTeacherRole = null;
	
	public static String getDbName() {
		return DB_NAME;
	}

	/*
	 * 找到存储介质中的用户信息
	 */
	private UserCache() {
		mContext = MyApplication.getInstance().getApplicationContext();
		mSettings = mContext.getSharedPreferences(DB_NAME, 0);
		mEditor = mSettings.edit();
		initUserCache();
	}

	public static synchronized UserCache getInstance() {
		if (mInstance == null) {
			mInstance = new UserCache();
		}
		return mInstance;
	}
	
	/**
	 * 初始化用户信息
	 */
	private void initUserCache() {
		logon = mSettings.getBoolean("logon", false);
		if (logon) {
			try {
				sessionKey = mSettings.getString("sessionKey", "STINFO01");
				password = mSettings.getString("password", "****");
				userInfo = SerializeHelper.restoreUserInfo(mContext);
				currentClass = SerializeHelper.restoreCurrentClass(mContext);
				currentTeacherRole = SerializeHelper.restoreTeacherRole(mContext);
				if (userInfo instanceof Parent) {
					currentChild = SerializeHelper.restoreCurrentChild(mContext);
				}
				updateClassGroup();
			} catch (Exception e) {
				Log.e(TAG, "Failed to deserialize cache file! " + e.toString());
				logon = false;
			}
		}
	}

	public boolean isLogon() {
		return logon;
	}

	public void setLogon(boolean value) {
		this.logon = value;
		mEditor.putBoolean("logon", value);
		mEditor.commit();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		mEditor.putString("password", password);
		mEditor.commit();
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
		mEditor.putString("sessionKey", sessionKey);
		mEditor.commit();
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		SerializeHelper.saveUserInfo(mContext, userInfo);
		updateClassGroup();
	}

	public ClassInfo getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(ClassInfo currentClass) {
		this.currentClass = currentClass;
		SerializeHelper.saveCurrentClass(mContext, currentClass);
	}
	
	public void setCurrentTeacherRole(TeacherRole currentTeacherRole) {
		this.currentTeacherRole = currentTeacherRole;
		SerializeHelper.saveCurrentTeacherRole(mContext, currentTeacherRole);
	}
	

	public TeacherRole getCurrentTeacherRole() {
		return currentTeacherRole;
	}

	public Student getCurrentChild() {
		return currentChild;
	}

	public void setCurrentChild(Student currentChild) {
		this.currentChild = currentChild;
		SerializeHelper.saveCurrentChild(mContext, currentChild);
	}

	public ArrayList<String> getTagList() {
		return mTagList;
	}

	public ArrayList<Group> getGroupList() {
		return mGroupList;
	}

	public void updateClassGroup() {
		if (userInfo == null) {
			return;
		}

		mTagList.clear();
		mClassMap.clear();
		if (userInfo instanceof Student) {
			Student student = (Student) userInfo;
			mTagList.add("C" + student.getClassId() + "_1");
			mTagList.add("C" + student.getClassId() + "_4");
			mClassMap.put(student.getClassId(), student.getClassName());
		} else if (userInfo instanceof Parent) {
			Parent parent = (Parent) userInfo;
			for (Student child : parent.getChildList()) {
				mTagList.add("C" + child.getClassId() + "_2");
				mTagList.add("C" + child.getClassId() + "_4");
				mClassMap.put(child.getClassId(), child.getClassName());
			}
		} else if (userInfo instanceof Teacher) {
			Teacher teacher = (Teacher) userInfo;
			for (TeacherRole role : teacher.getTeacherRoleList()) {
				mTagList.add("C" + role.getClassId() + "_3");
				mTagList.add("C" + role.getClassId() + "_4");
				mClassMap.put(role.getClassId(), role.getClassName());
			}
		}

		/*
		 * 暂时仅支持班级群
		 */
		mGroupList.clear();
		
		if(this.getCurrentClass()!=null){
			String classId = this.getCurrentClass().getClassId();
			Group group1 = new Group(this.getCurrentClass().getClassName() + "班级群", classId, String.valueOf(MessageObjectType.ALL),
			MessageGroupType.CLASS);
			Group group2 = new Group(this.getCurrentClass().getClassName() + "老师群", classId, String.valueOf(MessageObjectType.TEACHER),
					MessageGroupType.CLASS);
			Group group3 = new Group(this.getCurrentClass().getClassName() + "学生群", classId, String.valueOf(MessageObjectType.STUDENT),
					MessageGroupType.CLASS);
			Group group4 = new Group(this.getCurrentClass().getClassName() + "家长群", classId, String.valueOf(MessageObjectType.PARENT),
					MessageGroupType.CLASS);
			
			mGroupList.add(group1);
			mGroupList.add(group2);
			mGroupList.add(group3);
			mGroupList.add(group4);
		}
		else{
			
			for (String classId : mClassMap.keySet()) {
				String className = mClassMap.get(classId);
				Group group1 = new Group(className + "班级群", classId, String.valueOf(MessageObjectType.ALL),
						MessageGroupType.CLASS);
				Group group2 = new Group(className + "老师群", classId, String.valueOf(MessageObjectType.TEACHER),
						MessageGroupType.CLASS);
				Group group3 = new Group(className + "学生群", classId, String.valueOf(MessageObjectType.STUDENT),
						MessageGroupType.CLASS);
				Group group4 = new Group(className + "家长群", classId, String.valueOf(MessageObjectType.PARENT),
						MessageGroupType.CLASS);
	
				mGroupList.add(group1);
				mGroupList.add(group2);
				mGroupList.add(group3);
				mGroupList.add(group4);
				break;
			}
			
		}
		
		

		
		

	}

	public String getClassName(String classId) {
		String className = mClassMap.get(classId);
		return (className == null) ? "" : className;
	}
}