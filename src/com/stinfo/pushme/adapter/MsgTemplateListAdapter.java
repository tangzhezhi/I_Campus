package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.MsgTemplate;

/**
 * @author lenovo
 *
 */
public class MsgTemplateListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<MsgTemplate> mMsgTemplateList;

	public MsgTemplateListAdapter(Context context, ArrayList<MsgTemplate> msgTemplateList) {
		super();
		mMsgTemplateList = msgTemplateList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mMsgTemplateList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mMsgTemplateList.get(pos);
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
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_msg_template, null);

			holder = new ViewHolder();
			holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_msg_template_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MsgTemplate msgTemplate = mMsgTemplateList.get(pos);
		if (msgTemplate != null) {
			holder.tvContent.setText(msgTemplate.getSmsContent());
		}

		return convertView;
	}

	public class ViewHolder {
		CheckBox cbSelect;
		TextView tvContent;
		public CheckBox getCbSelect() {
			return cbSelect;
		}
		public void setCbSelect(CheckBox cbSelect) {
			this.cbSelect = cbSelect;
		}
		public TextView getTvContent() {
			return tvContent;
		}
		public void setTvContent(TextView tvContent) {
			this.tvContent = tvContent;
		}
		
	}
}