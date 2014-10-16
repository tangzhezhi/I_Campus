package com.stinfo.pushme.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PushUtils {
	
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";
	
	public static final String ACTION_BIND_SUCCESS = "com.stinfo.pushme.BIND_SUCCESS";
	public static final String ACTION_INTERNAL_MESSAGE = "com.stinfo.pushme.INTERNAL_MESSAGE";
	public static final String ACTION_NOTIFICATION_ENTRY = "com.stinfo.pushme.NOTIFICATION_ENTRY";
	public static final String ACTION_REFRESH_RECENT = "com.stinfo.pushme.REFRESH_RECENT";
	public static final String ACTION_REFRESH_UNREAD = "com.stinfo.pushme.REFRESH_UNREAD";
	
	public static final String MESSAGE = "message";
	public static final String ACTION_PUSH_OTHER_MESSAGE = "com.stinfo.pushme.other.message"; //除了聊天外的消息：如公告、作业，日常
	public static final String PUSH_USERID = "pushUserId";
	public static final String PUSH_CHANNELID = "pushChannelId";
	public static final String UNREAD_COUNT = "unreadCount"; 
	


	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}
	
	
	   // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }
	
	
	
	public static void dealEditText(final EditText editText,final int maxLength){

		editText.addTextChangedListener(new TextWatcher() {

	            private int cou = 0;

	            int selectionEnd = 0;

	            @Override

	            public void onTextChanged(CharSequence s, int start, int before,

	                    int count) {

	                cou = before + count;

	                String editable = editText.getText().toString();

	                String str = StringDealTool.stringFilter(editable);

	                if (!editable.equals(str)) {

	                	editText.setText(str);

	                }

	                editText.setSelection(editText.length());

	                cou = editText.length();

	            }

	            @Override

	            public void beforeTextChanged(CharSequence s, int start, int count,

	                    int after) {

	            }

	            @Override

	            public void afterTextChanged(Editable s) {

	                if (cou > maxLength) {

	                    selectionEnd = editText.getSelectionEnd();

	                    s.delete(maxLength, selectionEnd);

	                    if(android.os.Build.VERSION.SDK_INT >=4)

	                    {
	                    	editText.setText(s.toString());
	                    }
	                    
	                    MessageBox.showMessage(editText.getContext(), "超过了"+maxLength+"字符");

	                }

	            }

	        });
//		return editText;
	}
	
	
}