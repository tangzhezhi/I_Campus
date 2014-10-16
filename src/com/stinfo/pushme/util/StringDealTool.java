package com.stinfo.pushme.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringDealTool {
	
	/**
	 * 对上传的字符串进行过滤
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String stringFilter(String str)throws PatternSyntaxException{
        String regEx = "[/\\:*?<>(){}%#|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
	
	/**
	 * 对接收的字符串进行过滤
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String serverPostStringFilter(String str)throws PatternSyntaxException{
        String regEx = "[*?%]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
}
