package com.stinfo.pushme.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.common.AppConstant.MessageSendStatus;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Daily;
import com.stinfo.pushme.entity.Group;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.NoticeReply;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.RecentUserMessage;
import com.stinfo.pushme.entity.SMS;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.StudentRosterComparator;
import com.stinfo.pushme.entity.TeacherRoster;
import com.stinfo.pushme.entity.UserInfo;

/**
 * 本地数据库操作
 * @author lenovo
 *
 */
@SuppressLint("DefaultLocale")
public class DBAdapter {
	protected static final String TAG = "DBAdapter";
	protected static final String DATABASE_NAME = "pushme.db";
	protected static final int DATABASE_VERSION = 3;
	protected static final int MAX_NUMBER = 200;

	protected SQLiteDatabase db;
	protected Context mContext;
	protected DBHelper dbHelper;
	protected String mUserId = "";

	public DBAdapter(Context context, String userId) {
		this.mContext = context;
		this.mUserId = userId;
		dbHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DBAdapter() {
		this.mContext = MyApplication.getInstance().getApplicationContext();
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		dbHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void updateCache() {
		try {
			this.mContext = MyApplication.getInstance().getApplicationContext();
			this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
			db.execSQL(DBScript.DROP_TABLE_NOTICE);
//			db.execSQL(DBScript.DROP_TABLE_NOTICE_DETAIL);
			db.execSQL(DBScript.DROP_TABLE_HOMEWORK);
			db.execSQL(DBScript.DROP_TABLE_DAILY);
			db.execSQL(DBScript.DROP_TABLE_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_RECENT_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_SMS);
			db.execSQL(DBScript.DROP_TABLE_TIMETABLE);
			db.execSQL(DBScript.DROP_TABLE_EXAMROUND);
			db.execSQL(DBScript.DROP_TABLE_SCORE);
			db.execSQL(DBScript.DROP_VIEW_ROSTER);
			db.execSQL(DBScript.DROP_VIEW_RECENT_USER_MESSAGE);
			
			db.execSQL(DBScript.CREATE_TABLE_NOTICE);
//			db.execSQL(DBScript.CREATE_TABLE_NOTICE_DETAIL);
			db.execSQL(DBScript.CREATE_TABLE_HOMEWORK);
			db.execSQL(DBScript.CREATE_TABLE_DAILY);
			db.execSQL(DBScript.CREATE_TABLE_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_RECENT_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_SMS);
			db.execSQL(DBScript.CREATE_TABLE_TIMETABLE);
			db.execSQL(DBScript.CREATE_TABLE_EXAMROUND);
			db.execSQL(DBScript.CREATE_TABLE_SCORE);
			
			db.execSQL(DBScript.CREATE_VIEW_ROSTER);
			db.execSQL(DBScript.CREATE_VIEW_RECENT_USER_MESSAGE);
		} catch (SQLException e) {
			Log.d(TAG, "updateCache出错::"+e);
		}
	}
	
	
	public void changeUser() {
		try {
			db.execSQL(DBScript.DROP_VIEW_ROSTER);
			db.execSQL(DBScript.DROP_VIEW_RECENT_USER_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_NOTICE);
//			db.execSQL(DBScript.DROP_TABLE_NOTICE_DETAIL);
			db.execSQL(DBScript.DROP_TABLE_HOMEWORK);
			db.execSQL(DBScript.DROP_TABLE_DAILY);
			db.execSQL(DBScript.DROP_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_RECENT_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_SMS);
			db.execSQL(DBScript.DROP_TABLE_TIMETABLE);
			db.execSQL(DBScript.DROP_TABLE_EXAMROUND);
			db.execSQL(DBScript.DROP_TABLE_SCORE);
			db.execSQL(DBScript.DROP_TABLE_VIRTUOUSTEACH);				
			
			db.execSQL(DBScript.CREATE_TABLE_NOTICE);
//			db.execSQL(DBScript.CREATE_TABLE_NOTICE_DETAIL);
			db.execSQL(DBScript.CREATE_TABLE_HOMEWORK);
			db.execSQL(DBScript.CREATE_TABLE_DAILY);
			db.execSQL(DBScript.CREATE_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_RECENT_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_SMS);
			db.execSQL(DBScript.CREATE_TABLE_TIMETABLE);
			db.execSQL(DBScript.CREATE_TABLE_EXAMROUND);
			db.execSQL(DBScript.CREATE_TABLE_SCORE);
			db.execSQL(DBScript.CREATE_TABLE_VIRTUOUSTEACH);		
			db.execSQL(DBScript.CREATE_VIEW_ROSTER);
			db.execSQL(DBScript.CREATE_VIEW_RECENT_USER_MESSAGE);
		} catch (SQLException e) {
			Log.d(TAG, "changeUser出错::"+e);
		}
		
		
	}
	

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}

	public void addNotice(ArrayList<Notice> list) throws SQLException {
		//int detailCount = 0;
		for (Notice notice : list) {
			//判断公告是否已存在
			List<Notice> lh = getNoticeExist(notice.getAuthorId(),
					notice.getType(), notice.getTitle(),notice.getContent());
			//未查看总数
//			if(noticeDetailByNoticeId(notice.getObjectId()) < 1){
//				detailCount++;
//			}
			
			if(lh.size() == 0){
				ContentValues values = new ContentValues();
				
				values.put("cacheUserId", mUserId);
				
				values.put("authorId", notice.getAuthorId());
				values.put("authorName", notice.getAuthorName());
				values.put("type", notice.getType());
				values.put("objectId", notice.getObjectId());
				values.put("title", notice.getTitle());
				values.put("content", notice.getContent());
				values.put("createTime", notice.getCreateTime());
				values.put("isRead", "false");
				db.insertOrThrow("Notice", null, values);
			}
		}
		//return detailCount;
	}

	private Notice fetchNotice(Cursor result) {
		Notice notice = new Notice();

		notice.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		notice.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		notice.setType(result.getInt(result.getColumnIndex("type")));
		notice.setObjectId(result.getString(result.getColumnIndex("objectId")));
		notice.setTitle(result.getString(result.getColumnIndex("title")));
		notice.setContent(result.getString(result.getColumnIndex("content")));
		notice.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return notice;
	}
	
	
	private List<Notice> getNoticeExist(String authorId,
			int type, String title,String content) {
		String where = String.format(" authorId = '%s' AND type = '%d'  AND title = '%s' AND  content = '%s'  ", 
				authorId, type,title,content);
		return getNoticeByWhere(where);
	}
	
	
	
	
	private ArrayList<Notice> getNoticeByWhere(String where) {
		ArrayList<Notice> list = new ArrayList<Notice>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Notice", new String[] { "authorId", "authorName", "type", "objectId",
				"title", "content", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchNotice(result));
			} while (result.moveToNext());
		}

		return list;
	}
	
	//判断isRead状态标志位
	public boolean getNoticeIsReadByWhere(Notice notice) {
		boolean bool = false;
		
		String where = String.format(" objectId = '%s' AND type = '%d'  ",notice.getObjectId(),notice.getType());
		Cursor result = db.query("Notice", new String[] {"isRead"}, where, null, null, null, null, null);
		if (result.moveToFirst()) {
			String str = result.getString(result.getColumnIndex("isRead"));
			if(str.equals("true")){
				bool = true;
			}
		}
		return bool;
	}
	
	//更新isRead状态标志位
	public void updateNoticeIsReadByWhere(Notice notice){
		String where = String.format(" objectId = '%s' AND type = '%d'  ",notice.getObjectId(),notice.getType());
		db.execSQL("update Notice set isRead = 'true' where "+where);
	}
	
	//增加公告详情
	public void addNoticeDetail(ArrayList<NoticeReply> list) throws SQLException {
		
		for (NoticeReply notice : list) {
			//判断详情是否已存在
			int resultCount = getNoticeDetailExist(notice.getId(),
					notice.getNoticeId());
			
			if(resultCount < 1){
				ContentValues values = new ContentValues();
				values.put("id", notice.getId());
				values.put("noticeId", notice.getNoticeId());
				values.put("authorId", notice.getAuthorid());
				values.put("authorName", notice.getAuthorname());
				values.put("content", notice.getContent());
				values.put("answerTime", notice.getAnswerTime());
				db.insertOrThrow("NoticeDetail", null, values);
				//System.out.println(notice.getContent()+"  ---详情新增成功...");
			}
		}
		
	}
	
	//详情是否已存在
	private int getNoticeDetailExist(String id,
			String noticeId) {
		int countResult =  0;
		String where = String.format(" id = '%s' AND noticeId = '%s' ", 
				id, noticeId);
		//查询满足条件的记录数
		Cursor cursorResult = db.rawQuery("select count(*) from NoticeDetail where "+where, null);
		if(cursorResult.moveToFirst()){
			countResult = cursorResult.getInt(0);
		}
		return countResult;
	}
	
	//根据公告Id，查询是否存在详情
	public int noticeDetailByNoticeId(String noticeId){
		int countResult = 0;
		
		String where = String.format("noticeId = '%s' ",noticeId);
		Cursor cursorResult = db.rawQuery("select count(*) from NoticeDetail where "+where, null);
		if(cursorResult.moveToFirst()){
			countResult = cursorResult.getInt(0);
			//System.out.println("id为："+noticeId+" 的详情记录条数为："+countResult);
		}
		return countResult;
	}
	
	
	public ArrayList<Notice> getNotice(String objectId, int type) {
		String where = String.format("cacheUserId = '%s' AND type = %d AND objectId = '%s'", mUserId, type,
				objectId);
		return getNoticeByWhere(where);
	}

	public void addHomework(ArrayList<Homework> list) throws SQLException {
		for (Homework homework : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("authorId", homework.getAuthorId());
			values.put("authorName", homework.getAuthorName());
			values.put("subject", homework.getSubject());
			values.put("classId", homework.getClassId());
			values.put("content", homework.getContent());
			values.put("createTime", homework.getCreateTime());
			
			List<Homework> lh = this.getHomeworkByCondition(homework.getClassId(),homework.getSubject(),homework.getCreateTime());
			if(lh.size()==0){
				db.insertOrThrow("Homework", null, values);
			}
		}
	}

	private Homework fetchHomework(Cursor result) {
		Homework homework = new Homework();

		homework.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		homework.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		homework.setSubject(result.getString(result.getColumnIndex("subject")));
		homework.setClassId(result.getString(result.getColumnIndex("classId")));
		homework.setContent(result.getString(result.getColumnIndex("content")));
		homework.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return homework;
	}

	private ArrayList<Homework> getHomeworkByWhere(String where) {
		ArrayList<Homework> list = new ArrayList<Homework>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Homework", new String[] { "authorId", "authorName", "subject", "classId",
				"content", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchHomework(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Homework> getClassHomework(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getHomeworkByWhere(where);
	}

	public ArrayList<Homework> getSubjectHomework(String classId, String subject) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s' AND subject = '%s'", mUserId,
				classId, subject);
		return getHomeworkByWhere(where);
	}
	
	
	public ArrayList<Homework> getHomeworkByCondition(String classId, String subject,String createTime) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s' AND subject = '%s'  AND createTime = '%s'", mUserId,
				classId, subject,createTime);
		return getHomeworkByWhere(where);
	}
	
	
	
	public void addDaily(ArrayList<Daily> list) throws SQLException {
		for (Daily daily : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("authorId", daily.getAuthorId());
			values.put("authorName", daily.getAuthorName());
			values.put("classId", daily.getClassId());
			values.put("receiverId", daily.getReceiverId());
			values.put("receiverName", daily.getReceiverName());
			values.put("content", daily.getContent());
			values.put("createTime", daily.getCreateTime());
			db.insertOrThrow("Daily", null, values);
		}
	}

	private Daily fetchDaily(Cursor result) {
		Daily daily = new Daily();

		daily.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		daily.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		daily.setClassId(result.getString(result.getColumnIndex("classId")));
		daily.setReceiverId(result.getString(result.getColumnIndex("receiverId")));
		daily.setReceiverName(result.getString(result.getColumnIndex("receiverName")));
		daily.setContent(result.getString(result.getColumnIndex("content")));
		daily.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return daily;
	}

	private ArrayList<Daily> getDailyByWhere(String where) {
		ArrayList<Daily> list = new ArrayList<Daily>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Daily", new String[] { "authorId", "authorName", "receiverId",
				"receiverName", "content", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchDaily(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Daily> getClassDaily(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getDailyByWhere(where);
	}

	public ArrayList<Daily> getDaily(String receiverId) {
		String where = String.format("cacheUserId = '%s' AND receiverId = '%s'", mUserId, receiverId);
		return getDailyByWhere(where);
	}

	public void addSMS(SMS sms) throws SQLException {
		ContentValues values = new ContentValues();

		values.put("cacheUserId", mUserId);
		values.put("userList", sms.getUserList());
		values.put("content", sms.getContent());
		values.put("createTime", sms.getCreateTime());
		db.insertOrThrow("SMS", null, values);
	}

	public void addSMS(ArrayList<SMS> list) throws SQLException {
		for (SMS sms : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("userList", sms.getUserList());
			values.put("content", sms.getContent());
			values.put("createTime", sms.getCreateTime());
			db.insertOrThrow("SMS", null, values);
		}
	}

	private SMS fetchSMS(Cursor result) {
		SMS sms = new SMS();

		sms.setSmsId(result.getInt(result.getColumnIndex("smsId")));
		sms.setUserList(result.getString(result.getColumnIndex("userList")));
		sms.setContent(result.getString(result.getColumnIndex("content")));
		sms.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return sms;
	}

	public ArrayList<SMS> getOutSMS() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		ArrayList<SMS> list = new ArrayList<SMS>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("SMS", new String[] { "smsId", "userList", "content", "createTime", },
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchSMS(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public void addMessage(Message message) throws SQLException {
		ContentValues values = new ContentValues();

		values.put("msgType", message.getMsgType());
		values.put("senderId", message.getSenderId());
		values.put("receiverId", message.getReceiverId());
		values.put("objectType", message.getObjectType());
		values.put("groupType", message.getGroupType());
		values.put("content", message.getContent());
		values.put("outFlag", message.getOutFlag());
		values.put("createTime", message.getCreateTime());
		db.insertOrThrow("Message", null, values);
		replaceRecentMessage(message);
	}

	private Message fetchMessage(Cursor result) {
		Message message = new Message();

		message.setMsgType(result.getInt(result.getColumnIndex("msgType")));
		message.setSenderId(result.getString(result.getColumnIndex("senderId")));
		message.setReceiverId(result.getString(result.getColumnIndex("receiverId")));
		message.setObjectType(result.getString(result.getColumnIndex("objectType")));
		message.setGroupType(result.getInt(result.getColumnIndex("groupType")));
		message.setContent(result.getString(result.getColumnIndex("content")));
		message.setOutFlag(result.getInt(result.getColumnIndex("outFlag")));
		message.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		message.setSendStatus(MessageSendStatus.SUCCESS);
		return message;
	}

	private ArrayList<Message> getMessageByWhere(String where) {
		ArrayList<Message> list = new ArrayList<Message>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Message", new String[] { "msgType", "senderId", "receiverId", "objectType",
				"groupType", "content", "outFlag", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(0, fetchMessage(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Message> getPersonalMessage(String otherId) {
		String where = String.format(" (senderId = '%s' AND  receiverId = '%s' AND outFlag = 0) OR "
				+ "(senderId = '%s' AND receiverId = '%s' AND outFlag = 1)", otherId, mUserId, mUserId, otherId);
		return getMessageByWhere(where);
	}

	public ArrayList<Message> getGroupMessage(Group group) {
		String where = String.format(
				"((senderId <> '%s' AND outFlag = 0) OR (senderId = '%s' AND outFlag = 1)) "
						+ "AND (receiverId = '%s' AND objectType = %s AND groupType = %d)", mUserId, mUserId,
				group.getGroupId(), group.getObjectType(), group.getGroupType());
		return getMessageByWhere(where);
	}

	public void clearUnreadCount(String objectId, String objectType, int groupType) {
		String where = "";
		if (groupType == 0) {
			where = String.format("cacheUserId = '%s' AND objectId = '%s' AND groupType = 0", mUserId,
					objectId);
		} else {
			where = String.format("objectId = '%s' ", objectId
					);
		}

		ContentValues values = new ContentValues();
		values.put("unreadCount", 0);
		db.update("RecentMessage", values, where, null);
	}

	/**
	 * 保存用户以及群组的最新活动消息
	 * 
	 * @param message
	 * @throws SQLException
	 * 
	 *             SQL Example: INSERT OR REPLACE INTO RecentMessage
	 *             (cacheUserId, objectId, objectType, groupType, content,
	 *             unreadCount, updateTime) VALUES ('13975580246',
	 *             '15886553063', 0, 0, '这个函数理解起来有点费劲', COALESCE((SELECT
	 *             unreadCount FROM RecentMessage WHERE cacheUserId =
	 *             '13975580246' AND objectId = '15886553063' AND objectType = 0
	 *             AND groupType = 0), 0) , '20140326111828991');
	 */
	private void replaceRecentMessage(Message message) throws SQLException {
		UserCache userCache = UserCache.getInstance();
		UserInfo userInfo = userCache.getUserInfo();
		String userId = userInfo.getUserId();
		
		String objectId = "";
		int defUnread = (message.getOutFlag() == 0) ? 1 : 0; // 初始未读消息数

		if (message.getGroupType() != 0) {
			// 群组短信-消息列表显示为群组消息
			objectId = message.getReceiverId();
		} else {
			
			if(userId.equals(message.getSenderId())){
				objectId =  message.getReceiverId();
			}
			else{
				objectId =  message.getSenderId();
			}
//			if (message.getOutFlag() == 0) {
//				// 收到的个人消息
//				objectId = message.getSenderId();
//			} else {
//				// 发送的个人消息
//				objectId = message.getReceiverId();
//			}
		}

		String unreadSql;
		if (message.getOutFlag() == 0) {
			// 收到消息自增未读数
			unreadSql = "unreadCount + 1";
		} else {
			// 发送消息不参与统计
			unreadSql = "unreadCount";
		}
		String unreadCOALESCE = String
				.format("COALESCE((SELECT %s FROM RecentMessage "
						+ "WHERE cacheUserId = '%s' AND objectId = '%s'  AND groupType = %d), %d) ",
						unreadSql, mUserId, objectId,  message.getGroupType(),
						defUnread);

		String sql = String.format("INSERT OR REPLACE INTO RecentMessage "
				+ "(cacheUserId, objectId,  groupType, content, unreadCount, updateTime) "
				+ "VALUES ('%s',  %s, %d, '%s', " + unreadCOALESCE + ", '%s'); ", mUserId, objectId,
				 message.getGroupType(), message.getContent(),
				message.getCreateTime());
		
		Log.d(TAG, "replaceRecentMessage sql: \r\n" + sql);
		
		String where2 = String.format("unreadCount = '%d'  and objectId = '%s' ", 0,objectId);
		db.delete("RecentMessage", where2, null);
		
		db.execSQL(sql);
	}

	private RecentUserMessage fetchRecentUserMessage(Cursor result) {
		RecentUserMessage userMsg = new RecentUserMessage();
		userMsg.setCacheUserId(result.getString(result.getColumnIndex("cacheUserId")));
		userMsg.setObjectId(result.getString(result.getColumnIndex("objectId")));
		userMsg.setObjectType(result.getString(result.getColumnIndex("objectType")));
		userMsg.setGroupType(result.getInt(result.getColumnIndex("groupType")));
		userMsg.setContent(result.getString(result.getColumnIndex("content")));
		userMsg.setUnreadCount(result.getInt(result.getColumnIndex("unreadCount")));
		userMsg.setUpdateTime(result.getString(result.getColumnIndex("updateTime")));
		userMsg.setUserName(result.getString(result.getColumnIndex("userName")));
		userMsg.setSex(result.getString(result.getColumnIndex("sex")));
		userMsg.setPhone(result.getString(result.getColumnIndex("phone")));
		userMsg.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		userMsg.setUserType(result.getString(result.getColumnIndex("userType")));
		userMsg.setUservalue(result.getString(result.getColumnIndex("uservalue")));
		return userMsg;
	}

	private ArrayList<RecentUserMessage> getRecentUserMessageByWhere(String where) {
		ArrayList<RecentUserMessage> list = new ArrayList<RecentUserMessage>();
		String orderBy = "updateTime DESC";
		Cursor result = db.query("RecentUserMessage", new String[] {"cacheUserId", "objectId", "objectType","uservalue", "groupType",
				"content", "unreadCount", "updateTime", "userName", "sex", "phone", "picUrl", "userType" },
				where, null, null, null, orderBy, null);
		if (result.moveToFirst()) {
			do {
				list.add(fetchRecentUserMessage(result));
			} while (result.moveToNext());
		}

		return list;
	}

	/**
	 * 查询用户以及班级群组的最新活动消息，暂不显示不在通讯录的个人用户消息
	 * 
	 * @return 活动消息列表
	 */
	public ArrayList<RecentUserMessage> getRecentUserMessage() {
		ArrayList<Group> groupList = UserCache.getInstance().getGroupList();
		String groups = "";
		if (groupList.size() >= 1) {
			groups = groupList.get(0).getGroupId();
			for (int i = 1; i < groupList.size(); i++) {
				groups += ", " + groupList.get(i).getGroupId();
			}
		}
		String where = String.format("(cacheUserId = '%s' AND groupType = 0 AND userName IS NOT NULL  AND ObjectId <> '%s' ) "
				+ "OR (groupType = 2 AND ObjectId IN (%s)  AND ObjectId <> '%s' )", mUserId, mUserId,groups,mUserId);
		return getRecentUserMessageByWhere(where);
	}

	private UserInfo fetchUserInfo(Cursor result) {
		UserInfo userInfo = new UserInfo();

		userInfo.setUserId(result.getString(result.getColumnIndex("userId")));
		userInfo.setUserName(result.getString(result.getColumnIndex("userName")));
		userInfo.setSex(result.getString(result.getColumnIndex("sex")));
		userInfo.setPhone(result.getString(result.getColumnIndex("phone")));
		userInfo.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		userInfo.setUservalue(result.getString(result.getColumnIndex("uservalue")));
		userInfo.setMsgUserType(result.getString(result.getColumnIndex("userType")));
		return userInfo;
	}
	
	
	public UserInfo getUserInfoByUserId(String userId) throws SQLException {
		String where = String.format("cacheUserId = '%s' AND userId = '%s'   ", mUserId, userId);
		Cursor result = db.query("Roster", new String[] { "userId", "userName", "sex", "phone", "picUrl", "uservalue" , "usertype" },
				where, null, null, null, null, null);

		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("Not found! userId: " + userId);
		}

		return fetchUserInfo(result);
	}
	

	public UserInfo getUserInfo(String userId) throws SQLException {
		String where = String.format("cacheUserId = '%s' AND userId = '%s' OR uservalue ='%s'  ", mUserId, userId, userId);
		Cursor result = db.query("Roster", new String[] { "userId", "userName", "sex", "phone", "picUrl", "uservalue","usertype"   },
				where, null, null, null, null, null);

		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("Not found! userId: " + userId);
		}

		return fetchUserInfo(result);
	}
	
	
	public UserInfo getUserInfoByUservalue(String uservalue) throws SQLException {
		String where = String.format("cacheUserId = '%s' AND  uservalue ='%s'  ", mUserId, uservalue);
		Cursor result = db.query("Roster", new String[] { "userId", "userName", "sex", "phone", "picUrl", "uservalue" ,"usertype" },
				where, null, null, null, null, null);

		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("Not found! uservalue: " + uservalue);
		}

		return fetchUserInfo(result);
	}
	
	
	public UserInfo getUserInfoByUservalueAndUserType(String uservalue,String userType) throws SQLException {
		String where = String.format("cacheUserId = '%s' AND  uservalue ='%s'  AND userType = '%s'  ", mUserId, uservalue,userType);
		Cursor result = db.query("Roster", new String[] { "userId", "userName", "sex", "phone", "picUrl", "uservalue" ,"usertype" },
				where, null, null, null, null, null);

		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("Not found! uservalue: " + uservalue);
		}

		return fetchUserInfo(result);
	}

	public void deleteMyRoster() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		db.delete("StudentRoster", where, null);
		db.delete("TeacherRoster", where, null);
		db.delete("ParentRoster", where, null);
	}
	
	public void deleteTeacherRoster() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		db.delete("TeacherRoster", where, null);
	}
	
	public void deleteStudentRoster() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		db.delete("StudentRoster", where, null);
	}
	
	public void deleteParentRoster() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		db.delete("ParentRoster", where, null);
	}

	public void addStudentRoster(ArrayList<StudentRoster> list) throws SQLException {
		try {
			for (StudentRoster student : list) {
				ContentValues values = new ContentValues();

				values.put("cacheUserId", mUserId);
				values.put("userId", student.getUserId());
				values.put("userName", student.getUserName());
				values.put("uservalue", student.getUservalue());
				values.put("business", student.getBusiness());
				
				values.put("schoolId", student.getSchoolId());
				values.put("studentNo", student.getStudentNo());
				values.put("sex", student.getSex());
				values.put("phone", student.getPhone());
				values.put("picUrl", student.getPicUrl());
				values.put("classId", student.getClassId());
				values.put("className", student.getClassName());
				db.insertOrThrow("StudentRoster", null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	private StudentRoster fetchStudentRoster(Cursor result) {
		StudentRoster student = new StudentRoster();

		student.setUserId(result.getString(result.getColumnIndex("userId")));
		student.setUserName(result.getString(result.getColumnIndex("userName")));
		student.setUservalue(result.getString(result.getColumnIndex("uservalue")));
		
		student.setBusiness(result.getString(result.getColumnIndex("business")));
		
		student.setSchoolId(result.getString(result.getColumnIndex("schoolId")));
		student.setStudentNo(result.getString(result.getColumnIndex("studentNo")));
		student.setSex(result.getString(result.getColumnIndex("sex")));
		student.setPhone(result.getString(result.getColumnIndex("phone")));
		student.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		student.setClassId(result.getString(result.getColumnIndex("classId")));
		student.setClassName(result.getString(result.getColumnIndex("className")));

		return student;
	}

	public ArrayList<StudentRoster> getStudentRosterByWhere(String where) {
		ArrayList<StudentRoster> list = new ArrayList<StudentRoster>();
		String limit = String.valueOf(MAX_NUMBER);
		
		Cursor result = db.query("StudentRoster", new String[] { "userId", "userName","uservalue","business", "schoolId",
				"studentNo", "sex", "phone", "picUrl", "classId", "className" }, where, null, null, null,
				null, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchStudentRoster(result));
			} while (result.moveToNext());
		}
		
		Collections.sort(list, new StudentRosterComparator());
		return list;
	}

	public ArrayList<StudentRoster> getClassStudentRoster(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getStudentRosterByWhere(where);
	}

	public void addParentRoster(ArrayList<ParentRoster> list) throws SQLException {
		try {
			for (ParentRoster parent : list) {
				ContentValues values = new ContentValues();

				values.put("cacheUserId", mUserId);
				values.put("userId", parent.getUserId());
				values.put("userName", parent.getUserName());
				values.put("userValue", parent.getUservalue());
				values.put("parentValue", parent.getParentValue());
				values.put("business", parent.getBusiness());
				
				values.put("usercode", parent.getUsercode());
				values.put("schoolId", parent.getSchoolId());
				values.put("sex", parent.getSex());
				values.put("phone", parent.getPhone());
				values.put("picUrl", parent.getPicUrl());
				values.put("classId", parent.getClassId());
				values.put("className", parent.getClassName());
				values.put("childUserId", parent.getChildUserId());
				values.put("childName", parent.getChildName());
				db.insertOrThrow("ParentRoster", null, values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	private ParentRoster fetchParentRoster(Cursor result) {
		ParentRoster parent = new ParentRoster();

		parent.setUserId(result.getString(result.getColumnIndex("userId")));
		parent.setUserName(result.getString(result.getColumnIndex("userName")));
		parent.setUservalue(result.getString(result.getColumnIndex("uservalue")));
		parent.setParentValue(result.getString(result.getColumnIndex("parentvalue")));
		
		parent.setBusiness(result.getString(result.getColumnIndex("business")));
		parent.setUsercode(result.getString(result.getColumnIndex("usercode")));
		
		parent.setSchoolId(result.getString(result.getColumnIndex("schoolId")));
		parent.setSex(result.getString(result.getColumnIndex("sex")));
		parent.setPhone(result.getString(result.getColumnIndex("phone")));
		parent.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		parent.setClassId(result.getString(result.getColumnIndex("classId")));
		parent.setClassName(result.getString(result.getColumnIndex("className")));
		parent.setChildUserId(result.getString(result.getColumnIndex("childUserId")));
		parent.setChildName(result.getString(result.getColumnIndex("childName")));

		return parent;
	}

	public ArrayList<ParentRoster> getParentRosterByWhere(String where) {
		ArrayList<ParentRoster> list = new ArrayList<ParentRoster>();
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("ParentRoster", new String[] { "userId", "userName","uservalue","parentvalue","usercode", "business","schoolId", "sex",
				"phone", "picUrl", "classId", "className", "childUserId", "childName" }, where, null, null,
				null, null, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchParentRoster(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<ParentRoster> getClassParentRoster(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getParentRosterByWhere(where);
	}

	public void addTeacherRoster(ArrayList<TeacherRoster> list) throws SQLException {
		try {
			for (TeacherRoster teacher : list) {
				ContentValues values = new ContentValues();
				values.put("cacheUserId", mUserId);
				values.put("userId", teacher.getUserId());
				values.put("userName", teacher.getUserName());
				
				values.put("schoolId", teacher.getSchoolId());
				values.put("uservalue", teacher.getUservalue());
				values.put("teacherId", teacher.getTeacherId());
				values.put("usercode", teacher.getUsercode());
				values.put("business", teacher.getBusiness());
				
				values.put("sex", teacher.getSex());
				values.put("phone", teacher.getPhone());
				values.put("picUrl", teacher.getPicUrl());
				values.put("role", teacher.getRole());
				
				values.put("rolename", teacher.getRolename());
				values.put("rolecode", teacher.getRolecode());
				values.put("classId", teacher.getClassId());
				values.put("className", teacher.getClassName());
				db.insertOrThrow("TeacherRoster", null, values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	private TeacherRoster fetchTeacherRoster(Cursor result) {
		TeacherRoster teacher = new TeacherRoster();

		teacher.setUserId(result.getString(result.getColumnIndex("userId")));
		teacher.setUserName(result.getString(result.getColumnIndex("userName")));
		
		teacher.setUsercode(result.getString(result.getColumnIndex("usercode")));
		teacher.setUservalue(result.getString(result.getColumnIndex("uservalue")));
		teacher.setBusiness(result.getString(result.getColumnIndex("business")));
		
		teacher.setSchoolId(result.getString(result.getColumnIndex("schoolId")));
		teacher.setTeacherId(result.getString(result.getColumnIndex("teacherId")));
		teacher.setSex(result.getString(result.getColumnIndex("sex")));
		teacher.setPhone(result.getString(result.getColumnIndex("phone")));
		teacher.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		teacher.setRole(result.getString(result.getColumnIndex("role")));
		teacher.setClassId(result.getString(result.getColumnIndex("classId")));
		teacher.setClassName(result.getString(result.getColumnIndex("className")));
		
		teacher.setRolename(result.getString(result.getColumnIndex("rolename")));
		teacher.setRolecode(result.getString(result.getColumnIndex("rolecode")));

		return teacher;
	}

	private ArrayList<TeacherRoster> getTeacherRosterByWhere(String where) {
		ArrayList<TeacherRoster> list = new ArrayList<TeacherRoster>();
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("TeacherRoster", new String[] { "userId", "userName","usercode","uservalue","business", "schoolId",
				"teacherId", "sex", "phone", "picUrl", "role",  "rolename", "rolecode","classId", "className" }, where, null, null,
				null, null, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchTeacherRoster(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<TeacherRoster> getClassTeacherRoster(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getTeacherRosterByWhere(where);
	}

	public ArrayList<TeacherRoster> getSchoolTeacherRoster(String schoolId) {
		String where = String.format("cacheUserId = '%s' AND schoolId = '%s'", mUserId, schoolId);
		return getTeacherRosterByWhere(where);
	}
	
	public ArrayList<StudentRoster> getClassStudentRosterOfParent(String classId,String parentId) {
		
		ArrayList<StudentRoster> slist = new ArrayList<StudentRoster>();
		
		String where = String.format("userId = '%s' AND classId = '%s'", parentId, classId);
		 ArrayList<ParentRoster> parentList = getParentRosterByWhere(where);
		
		 for(ParentRoster parentRoster : parentList){
			 String sUserId = parentRoster.getChildUserId();
			 String sClassId = parentRoster.getClassId();
			 String whereStu = String.format("userId = '%s' AND classId = '%s'", sUserId, sClassId);
			 slist.addAll( getStudentRosterByWhere(whereStu));
			 
		 }
		return slist;
	}
	
	
	

	protected static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context mContext, String name, CursorFactory factory, int version) {
			super(mContext, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DBScript.CREATE_TABLE_NOTICE);
//			db.execSQL(DBScript.CREATE_TABLE_NOTICE_DETAIL);
			db.execSQL(DBScript.CREATE_TABLE_HOMEWORK);
			db.execSQL(DBScript.CREATE_TABLE_DAILY);
			db.execSQL(DBScript.CREATE_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_RECENT_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_SMS);
			db.execSQL(DBScript.CREATE_TABLE_TIMETABLE);
			db.execSQL(DBScript.CREATE_TABLE_EXAMROUND);
			db.execSQL(DBScript.CREATE_TABLE_SCORE);
			db.execSQL(DBScript.CREATE_TABLE_VIRTUOUSTEACH);
			
			db.execSQL(DBScript.CREATE_VIEW_ROSTER);
			db.execSQL(DBScript.CREATE_VIEW_RECENT_USER_MESSAGE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL(DBScript.DROP_TABLE_NOTICE);
//			db.execSQL(DBScript.DROP_TABLE_NOTICE_DETAIL);
			db.execSQL(DBScript.DROP_TABLE_HOMEWORK);
			db.execSQL(DBScript.DROP_TABLE_DAILY);
			db.execSQL(DBScript.DROP_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_RECENT_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_SMS);
			db.execSQL(DBScript.DROP_TABLE_TIMETABLE);
			db.execSQL(DBScript.DROP_TABLE_EXAMROUND);
			db.execSQL(DBScript.DROP_TABLE_SCORE);
			db.execSQL(DBScript.DROP_TABLE_VIRTUOUSTEACH);
			
			db.execSQL(DBScript.DROP_VIEW_ROSTER);
			db.execSQL(DBScript.DROP_VIEW_RECENT_USER_MESSAGE);
			onCreate(db);
		}
	}
}