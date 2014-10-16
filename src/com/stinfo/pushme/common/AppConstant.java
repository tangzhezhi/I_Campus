package com.stinfo.pushme.common;

public class AppConstant {
//	public static final String BASE_URL = "http://175.6.1.151:8090/";
//	 public static final String BASE_URL = "http://113.247.250.200:8090/";
	 public static final String BASE_URL = "http://192.168.2.202:8085/";
	 
//	public static final String School_Platform_BASE_URL = "http://192.168.2.127:8080/eos-web/httpService/zsxy/";
	 
	 
	public static final String School_Platform_BASE_URL = "http://192.168.2.211:8080/eos/httpService/zsxy/";
	
//	public static final String School_Platform_BASE_URL = "http://www.iucai.cn/eos/httpService/zsxy/";
	 
	
	public static final String School_Platform_Photo_URL = "http://192.168.2.211:8888/upload4ck";
	public static final String School_Platform_Photo_QUERY_URL = "http://192.168.2.211:8899";
	
//	public static final String School_Platform_Photo_QUERY_URL = "http://113.247.250.200:9007";
//	public static final String School_Platform_Photo_URL = "http://113.247.250.200:9006/upload4ck";
	
	
	public static final int PUSH_MESSAGE_ID = 1;
	public static final int INTERNAL_MESSAGE_ID = 2;
	public static final int UPDATE_ID = 1001;
	

	
	public static final class PhotoType {
		public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
		public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
		public static final int PHOTO_REQUEST_CUT = 3;// 裁剪结果
	}
	
	public static final class DeedType {
		public static final String good = "1";
		public static final String bad = "2";
		public static final String lover_share = "3";
	}
	
	public static final class DeedSort {
		public static final String id = "id";
		public static final String lastComment = "lastComment";
		public static final String clicknum = "clicknum";
		public static final String iselite = "iselite";
	}
	
	public static final class SchoolLeader {
		public static final String yes = "1";
		public static final String no = "2";
	}
	
	public static final class Classmaster {
		public static final String yes = "1";
	}
	
	public static final class InOrOutDoorFlag {
		public static final String inDoor = "20";
		public static final String outDoor = "10";
	}
	
	
	
	
	public static final class UserType {
		public static final String SYSTEM = "00";
		public static final String STUDENT = "01";
		public static final String PARENT = "03";
		public static final String TEACHER = "02";
		public static final String ALL = "05";
	}

	public static final class Ack {
		public static final int SUCCESS = 0;
		public static final int NOT_FOUND = 100;
	}

	public static final class Sex {
		public static final String FEMALE = "0";
		public static final String MALE = "1";
	}

	public static final class NoticeQueryType {
		public static final int QUERY_ALL = 0;
		public static final int QUERY_SCHOOL = 1;
		public static final int QUERY_SOMEONE = 2;
		public static final int QUERY_CLASS = 3;
	}

	public static final class NoticeType {
		public static final int SCHOOL = 1;
		public static final int TEACHER = 2;
		public static final int CLASS = 3;
		
	}

	public static final class HomeworkQueryType {
		public static final int QUERY_CLASS = 1;
		public static final int QUERY_SOMEONE = 2;
	}
	
	public static final class DailyQueryType {
		public static final int QUERY_CLASS = 1;
		public static final int QUERY_AUTHOR = 2;
		public static final int QUERY_RECEIVER = 3;
	}

	public static final class MessageType {
		public static final int NORMAL = 1;
		public static final int NOTICE = 2;
		public static final int HOMEWORK = 3;
		public static final int DAILY = 4;
	}

	public static final class MessageObjectType {
		public static final int PERSONAL = 0;
		public static final int STUDENT = 1;
		public static final int PARENT = 2;
		public static final int TEACHER = 3;
		public static final int ALL = 4;
	}

	public static final class MessageGroupType {
		public static final int PERSONAL = 0;
		public static final int SCHOOL = 1;
		public static final int CLASS = 2;
	}

	public static final class MessageSendStatus {
		public static final int SENDING = 1;
		public static final int SUCCESS = 2;
		public static final int FAILED = 3;
	}
}
