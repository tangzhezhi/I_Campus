package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.fragment.NoticeDetailFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public final class NoticeDetailAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Notice> mNoticeList;
	private Boolean isReply;
	
	
	public NoticeDetailAdapter(FragmentManager fm, ArrayList<Notice> noticeList) {
		super(fm);
		mNoticeList = noticeList;
	}
	
	public NoticeDetailAdapter(FragmentManager fm, ArrayList<Notice> noticeList,Boolean isReply) {
		super(fm);
		mNoticeList = noticeList;
		this.isReply = isReply;
	}
	
	@Override
	public int getCount() {
		return mNoticeList.size();
	}

	@Override
	public Fragment getItem(int position) {
//		return NoticeDetailFragment.newInstance(mNoticeList.get(position));
		return NoticeDetailFragment.newInstance(mNoticeList.get(position),isReply);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		return super.instantiateItem(arg0, arg1);
	}

	@Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}