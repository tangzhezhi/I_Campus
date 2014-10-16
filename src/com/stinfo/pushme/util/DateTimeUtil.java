package com.stinfo.pushme.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class DateTimeUtil {
	
	public static String getCompactTime() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(curDate);
	}
	
	public static String getShortTime() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(curDate);
	}

	public static String getLongTime() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(curDate);
	}

	public static String getToday() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(curDate);
	}
	
	/**
	 * yyyy-mm-dd 转 yyyymmdd
	 * @param str
	 * @return
	 */
	public static String getDateToYmd(String str) {
		if(str.length()==10){
			return str.substring(0,4)+str.substring(5,7)+str.substring(8,10);
		}
		return str;
	}

	public static String getYesterday() {
		Calendar current = Calendar.getInstance();
		Calendar yesterday = Calendar.getInstance();

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(yesterday.getTime());
	}

	public static String getSevenday() {
		Calendar current = Calendar.getInstance();
		Calendar servenday = Calendar.getInstance();

		servenday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		servenday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		servenday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 7);
		servenday.set(Calendar.HOUR_OF_DAY, 0);
		servenday.set(Calendar.MINUTE, 0);
		servenday.set(Calendar.SECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(servenday.getTime());
	}

	public static String toStandardTime(String compactTime) {
		if (compactTime.length() < 14) {
			/** Illegal format look as original time */
			return compactTime;
		}

		return String.format("%s-%s-%s %s:%s:%s", compactTime.substring(0, 4), compactTime.substring(4, 6),
				compactTime.substring(6, 8), compactTime.substring(8, 10), compactTime.substring(10, 12),
				compactTime.substring(12, 14));
	}

	public static boolean isMore15Mins(String time1, String time2) {
		if ((time1.length() < 14) || (time2.length() < 14)) {
			return true;
		}

		return true;
	}

	public static String getWeek(String compactTime) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if(compactTime!=null && compactTime.length()==8){
				compactTime = compactTime+"000000";
			}
			
			Date inputDate = sdf.parse(compactTime.substring(0, 14));
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0) {
				w = 0;
			}
			
			return weekDays[w];
		} catch (ParseException e) {
			return "";
		}
	}
	
	
	public static int getWeekDay(String compactTime) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if(compactTime!=null && compactTime.length()==8){
				compactTime = compactTime+"000000";
			}
			Date inputDate = sdf.parse(compactTime.substring(0, 14));
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0) {
				w = 0;
			}
			
			return w;
		} catch (ParseException e) {
			return 0;
		}
	}

	public static String toMessageTime(String compactTime) {
		if (compactTime.length() < 14) {
			return compactTime;
		}

		String msgDate = compactTime.substring(0, 8);
		if (msgDate.equals(getToday())) {
			return String.format("今天 %s:%s", compactTime.substring(8, 10), compactTime.substring(10, 12));
		} else if (msgDate.equals(getYesterday())) {
			return String.format("昨天 %s:%s", compactTime.substring(8, 10), compactTime.substring(10, 12));
		} else if (msgDate.compareTo(getSevenday()) > 0) {
			return String.format("%s %s:%s", getWeek(compactTime), compactTime.substring(8, 10),
					compactTime.substring(10, 12));
		} else {
			return String.format("%s-%s-%s %s:%s", compactTime.substring(0, 4), compactTime.substring(4, 6),
					compactTime.substring(6, 8), compactTime.substring(8, 10), compactTime.substring(10, 12));
		}
	}
	
	/**
	 * 返回当前周
	 * @return
	 */
	public static int getCurrentWeek(){
		java.util.Date lastweek = new java.util.Date();
		Calendar c = Calendar.getInstance() ;
		c.setTime( lastweek);
		return c.get(Calendar.WEEK_OF_YEAR);
	}
	
	
	public static String getYMD(DateTime dateTime){
		String year = String.valueOf(dateTime.getYear()) ;
		String month =  String.valueOf(dateTime.getMonthOfYear()<10?"0"+dateTime.getMonthOfYear():dateTime.getMonthOfYear()) ;
		String day = String.valueOf(dateTime.getDayOfMonth()<10?"0"+dateTime.getDayOfMonth():dateTime.getDayOfMonth());
		return year+month+day;
	}
	
	/**
	 * 获得第几周，周几的日期
	 * @param selectWeek
	 * @param n
	 * @return
	 */
	public static String getDayOfWeek(int selectWeek,int n){
		int week = DateTime.now().getWeekOfWeekyear();
		String startDateTime = "";
		
		if(selectWeek==0){
			selectWeek = week;
		}
		
		if(week >= selectWeek){
			 startDateTime = getYMD(DateTime.now().minusWeeks(week-selectWeek).dayOfWeek().withMinimumValue().plusDays(n));
		}
		else{
			 startDateTime = getYMD(DateTime.now().plusWeeks(selectWeek-week).dayOfWeek().withMinimumValue().plusDays(n));
		}
		return startDateTime;
	}
	
	
	public static String getStartDateOfWeek(int selectWeek){
		int week = DateTime.now().getWeekOfWeekyear();
		String startDateTime = "";
		
		if(selectWeek==0){
			selectWeek = week;
		}
		
		if(week >= selectWeek){
			 startDateTime = getYMD(DateTime.now().minusWeeks(week-selectWeek).dayOfWeek().withMinimumValue());
		}
		else{
			 startDateTime = getYMD(DateTime.now().plusWeeks(selectWeek-week).dayOfWeek().withMinimumValue());
		}
		return startDateTime;
	}
	
	public static String getEndDateOfWeek(int selectWeek){
		int week = DateTime.now().getWeekOfWeekyear();
		String endDateTime = "";
		
		if(selectWeek==0){
			selectWeek = week;
		}
		
		if(week >= selectWeek){
			endDateTime = getYMD(DateTime.now().minusWeeks(week-selectWeek).dayOfWeek().withMaximumValue());
		}
		else{
			endDateTime = getYMD(DateTime.now().plusWeeks(selectWeek-week).dayOfWeek().withMaximumValue());
		}
		return endDateTime;
	}
}