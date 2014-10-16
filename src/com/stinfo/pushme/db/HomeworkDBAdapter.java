package com.stinfo.pushme.db;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class HomeworkDBAdapter extends DBAdapter {
	/**
	 * 删除本地数据库中的记录
	 * @param createtime
	 * @param content
	 */
	@SuppressLint("DefaultLocale")
	public void deleteMyHomework(String createtime,String content) {
		String where = String.format("cacheUserId = '%s' AND createTime = %s AND content = '%s'", mUserId, createtime,
				content);
		this.db.delete("Homework", where, null);
	}
}
