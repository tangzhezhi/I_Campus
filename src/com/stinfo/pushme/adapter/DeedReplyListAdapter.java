package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.NoticeReply;

/**
 * 对通知数据进行填充、绑定，对应界面位置
 * @author lenovo
 *
 */
public class DeedReplyListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<NoticeReply> mNoticeReplyList;
	private LinearLayout itemBody;
	private RelativeLayout itemReply;
	private Context context;
	
	public DeedReplyListAdapter(Context context, ArrayList<NoticeReply> noticeList) {
		super();
		mNoticeReplyList = noticeList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount() {
		return mNoticeReplyList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mNoticeReplyList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
	
	/**
	 * 显示记录，绑定值，最后返回视图
	 * 通过convertView+ViewHolder来实现，
	 * ViewHolder就是一个静态类，
	 * 使用 ViewHolder 的关键好处是缓存了显示数据的视图（View），加快了 UI 的响应速度。
	 */
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_notice_reply, null);
			holder = new ViewHolder();
			holder.tvAuthorname = (TextView) convertView.findViewById(R.id.tv_publish);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvAnswerTime = (TextView) convertView.findViewById(R.id.tv_answertime);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final NoticeReply notice = mNoticeReplyList.get(pos);
		if (notice != null) {
			holder.tvAuthorname.setText(notice.getAuthorname());
			holder.tvContent.setText(notice.getContent());
			holder.tvAnswerTime.setText(notice.getAnswerTime());
		}

		return convertView;
	}

	final class ViewHolder {
		TextView tvAuthorid;
		TextView tvAuthorname;
		TextView tvContent;
		TextView tvAnswerTime;
	}
}