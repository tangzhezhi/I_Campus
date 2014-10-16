package com.stinfo.pushme.db;

import com.stinfo.pushme.common.AppConstant.UserType;

public final class DBScript {
    
	public static final String CREATE_TABLE_NOTICE = 
    		"CREATE TABLE Notice " +
            "( " +
            	"cacheUserId text , " +
                "authorId text , " +
                "authorName text , " +
                "type integer , " +
                "objectId text , " +
                "title text , " +
                "content text , " +
                "createTime text,  " +
                "isRead text  " +
            "); ";
   
	
	public static final String CREATE_TABLE_NOTICE_DETAIL =
			"CREATE TABLE NoticeDetail " +
			"( " +
				"id text , " +
				"noticeId text , " +
				"authorId text , " +
	            "authorName text , " +
	            "content text , " +
	            "answerTime text  " +
			"); ";
	
    public static final String CREATE_TABLE_HOMEWORK = 
    		"CREATE TABLE Homework " +
            "( " +
            	"cacheUserId text , " +
                "authorId text , " +
                "authorName text , " +
                "subject text , " +
                "classId text , " +
                "content text , " +
                "createTime text  " +
            "); ";
	
    public static final String CREATE_TABLE_DAILY = 
    		"CREATE TABLE Daily " + 
    		"( " +
    			"cacheUserId text , " +
            	"authorId text , " +
	            "authorName text , " +
	            "classId text , " +
	            "receiverId text , " +
	            "receiverName text , " +
	            "content text , " +
	            "createTime text " +
	        "); ";
    
    public static final String CREATE_TABLE_STUDENT_ROSTER = 
    		"CREATE TABLE StudentRoster " +
            "( " +
            	"cacheUserId text , " +
                "userId text , " +
                "userName text , " +
                "uservalue text , " +
                "usercode text , " +
                "business text , " +
                "schoolId text , " +
                "studentNo text , " +
                "sex integer , " +
                "phone text, " +
                "picUrl text, " +
                "classId text , " +
                "className text  " +
            "); ";
    
    public static final String CREATE_TABLE_PARENT_ROSTER = 
    		"CREATE TABLE ParentRoster " +
            "( " +
            	"cacheUserId text , " +
                "userId text , " +
                "uservalue text , " +
                "parentvalue text , " +
                "userName text , " +
                "usercode text , " +
                "business text , " +
                "sex integer , " +
                "phone text, " +
                "picUrl text, " +
                "childUserId text , " +
                "childName text , " +
                "schoolId text , " +
                "classId text , " +
                "className text  " +
            "); ";
	
    public static final String CREATE_TABLE_TEACHER_ROSTER = 
    		"CREATE TABLE TeacherRoster " +
            "( " +
            	"cacheUserId text , " +
                "userId text , " +
                "userName text , " +
                "teacherId text , " +
                "usercode text , " +
                "uservalue text , " +
                "business text , " +
                "sex integer text , " +
                "phone text, " +
                "picUrl text, " +
                "role text, " +
                "rolename text, " +
                "rolecode text, " +
                "schoolId text text , " +
                "classId text text , " +
                "className text text  " +
            "); ";
    
    public static final String CREATE_TABLE_MESSAGE = 
    		"CREATE TABLE Message " +
            "( " +
            	"msgId integer  primary key autoincrement, " +
                "msgType integer , " +
                "senderId text , " +
                "receiverId text , " +
                "objectType text , " +
                "groupType integer , " +
                "content text , " +
                "outFlag integer , " +
                "createTime text  " +
            "); ";
    
    public static final String CREATE_TABLE_RECENT_MESSAGE = 
    		"CREATE TABLE RecentMessage " +
            "( " +
            	"cacheUserId text , " +
                "objectId text , " +
                "objectType text , " +
                "groupType integer , " +
                "content text , " +
                "unreadCount integer , " +
                "updateTime text , " +
                "PRIMARY KEY (cacheUserId, objectId, objectType, groupType) " +
            "); ";
    
    public static final String CREATE_TABLE_SMS = 
    		"CREATE TABLE SMS " +
            "( " +
            	"cacheUserId text , " +
            	"smsId integer  primary key autoincrement, " +
                "userList text , " +
                "content text , " +
                "createTime text  " +
            "); ";
    
    public static final String CREATE_TABLE_TIMETABLE= 
    		"CREATE TABLE Timetable " +
            "( " +
            	"cacheUserId text , " +
            	"timetableId integer  primary key autoincrement, " +
                "subject text , " +
                "content text , " +
                "classId text ," +
                "section text ," +
                "classDate text ," +
                "weekDay text ," +
                "createTime text  " +
            "); ";
    
    
    public static final String CREATE_TABLE_EXAMROUND= 
    		"CREATE TABLE ExamRound " +
            "( " +
            	"cacheUserId text , " +
            	"classId text  ," +
            	"examRoundId text  primary key , " +
                "examRoundTitle text , " +
                "examDate text , " +
                "createTime text ," +
                "totalScore text ," +
                "classTotalRanking text ," +
                "gradeTotalRanking text ," +
                "classTotalAverage text , " +
                "gradeTotalAverage text " +
            "); ";
    
    
    public static final String CREATE_TABLE_SCORE= 
    		"CREATE TABLE score " +
            "( " +
            	"cacheUserId text , " +
            	"examRoundId text ,"+
            	"userId text  ," +
            	"subject text , " +
                "examPaperId text   primary key , " +
                "examPaperTitle text , " +
                "examDate text ," +
                "createTime text ," +
                "examPaperScore text ," +
                "classRanking text , " +
                "gradeRanking text " +
            "); ";
    
    
    public static final String CREATE_TABLE_VIRTUOUSTEACH= 
    		"CREATE TABLE VIRTUOUSTEACH " +
            "( " +
            	"id text   primary key , " +
            	"classId text ,"+
            	"startDate text  ," +
            	"endDate text  ," +
            	"createTime text  ," +
            	"userId text , " +
                "coin text , " +
                "userType text  " +
            "); ";
    
    public static final String CREATE_VIEW_ROSTER = 
    		"CREATE VIEW Roster AS " +
    		"SELECT cacheUserId, userId,uservalue, userName, sex, phone, picUrl,'"+ UserType.STUDENT+"' as userType FROM StudentRoster " +
    			"UNION " +
    		"SELECT cacheUserId, userId,uservalue, userName, sex, phone, picUrl, '"+ UserType.PARENT+"' as userType FROM ParentRoster " +
    			"UNION " +
    		"SELECT cacheUserId, userId,uservalue, userName, sex, phone, picUrl, '"+ UserType.TEACHER+"' as userType FROM TeacherRoster; ";
    
    public static final String CREATE_VIEW_RECENT_USER_MESSAGE = 
    		"CREATE VIEW RecentUserMessage AS " +
    		"SELECT RecentMessage.*, Roster.userName AS userName, Roster.sex AS sex, Roster.phone AS phone, " +
    				"Roster.picUrl AS picUrl, Roster.userType AS userType, Roster.uservalue AS uservalue    " +
    		" FROM RecentMessage " +
    		" LEFT JOIN Roster " +
    		" ON RecentMessage.objectId = Roster.userId AND RecentMessage.cacheUserId = Roster.cacheUserId; ";
    
    
    public static final String DROP_TABLE_NOTICE = "DROP TABLE IF EXISTS Notice; ";
    public static final String DROP_TABLE_NOTICE_DETAIL = "DROP TABLE IF EXISTS NoticeDetail; ";
    public static final String DROP_TABLE_HOMEWORK = "DROP TABLE IF EXISTS Homework; ";
    public static final String DROP_TABLE_DAILY = "DROP TABLE IF EXISTS Daily; ";
    public static final String DROP_TABLE_STUDENT_ROSTER = "DROP TABLE IF EXISTS StudentRoster; ";
    public static final String DROP_TABLE_PARENT_ROSTER = "DROP TABLE IF EXISTS ParentRoster; ";
    public static final String DROP_TABLE_TEACHER_ROSTER = "DROP TABLE IF EXISTS TeacherRoster; ";
    public static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS Message; ";
    public static final String DROP_TABLE_RECENT_MESSAGE = "DROP TABLE IF EXISTS RecentMessage; ";
    public static final String DROP_TABLE_SMS = "DROP TABLE IF EXISTS SMS; ";
    public static final String DROP_VIEW_ROSTER = "DROP VIEW IF EXISTS Roster; ";
    public static final String DROP_VIEW_RECENT_USER_MESSAGE = "DROP VIEW IF EXISTS RecentUserMessage; ";
    
    public static final String DROP_TABLE_TIMETABLE = "DROP TABLE IF EXISTS Timetable ; ";
    public static final String DROP_TABLE_EXAMROUND = "DROP TABLE IF EXISTS ExamRound ; ";
    public static final String DROP_TABLE_SCORE = "DROP TABLE IF EXISTS score ; ";
    public static final String DROP_TABLE_VIRTUOUSTEACH = "DROP TABLE IF EXISTS virtuousteach ; ";
}
