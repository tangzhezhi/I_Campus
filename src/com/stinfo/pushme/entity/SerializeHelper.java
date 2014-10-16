package com.stinfo.pushme.entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

/**
 * 序列化对象到文件中
 * @author lenovo
 *
 */
public class SerializeHelper {
	private static final String TAG = "SerializeHelper";
	private static final String userFile = "userInfo.dat";
	private static final String classFile = "class.dat";
	private static final String childFile = "child.dat";
	private static final String teacherRoleFile = "teacherRole.dat";

	public static void saveUserInfo(Context context, UserInfo userInfo) {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + userFile;
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(userInfo);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "Failed to saveUserInfo! " + e.toString());
		}
	}

	public static UserInfo restoreUserInfo(Context context) throws Exception {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + userFile;
			FileInputStream fls = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fls);

			UserInfo userInfo = (UserInfo) ois.readObject();
			ois.close();
			fls.close();
			return userInfo;
		} catch (Exception e) {
			Log.e(TAG, "Failed to restoreUserInfo! " + e.toString());
			throw e;
		}
	}
	
	
	public static TeacherRole restoreTeacherRole(Context context) throws Exception {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + teacherRoleFile;
			FileInputStream fls = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fls);

			TeacherRole teacherRole = (TeacherRole) ois.readObject();
			ois.close();
			fls.close();
			return teacherRole;
		} catch (Exception e) {
			Log.e(TAG, "Failed to restoreUserInfo! " + e.toString());
			throw e;
		}
	}
	
	public static void saveCurrentTeacherRole(Context context, TeacherRole currentClass) {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + teacherRoleFile;
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(currentClass);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "Failed to saveCurrentClass! " + e.toString());
		}
	}
	
	
	
	public static void saveCurrentClass(Context context, ClassInfo currentClass) {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + classFile;
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(currentClass);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "Failed to saveCurrentClass! " + e.toString());
		}
	}

	public static ClassInfo restoreCurrentClass(Context context) throws Exception {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + classFile;
			FileInputStream fls = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fls);

			ClassInfo currentClass = (ClassInfo) ois.readObject();
			ois.close();
			fls.close();
			return currentClass;
		} catch (Exception e) {
			Log.e(TAG, "Failed to restoreCurrentClass! " + e.toString());
			throw e;
		}
	}

	public static void saveCurrentChild(Context context, Student child) {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + childFile;
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(child);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "Failed to saveCurrentChild! " + e.toString());
		}
	}

	public static Student restoreCurrentChild(Context context) throws Exception {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + childFile;
			FileInputStream fls = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fls);

			Student child = (Student) ois.readObject();
			ois.close();
			fls.close();
			return child;
		} catch (Exception e) {
			Log.e(TAG, "Failed to restoreCurrentChild! " + e.toString());
			throw e;
		}
	}
}
