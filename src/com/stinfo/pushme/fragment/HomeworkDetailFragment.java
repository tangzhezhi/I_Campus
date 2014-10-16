package com.stinfo.pushme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.util.DateTimeUtil;

public final class HomeworkDetailFragment extends Fragment {
	private Homework mHomework = null;

	public static HomeworkDetailFragment newInstance(Homework homework) {
		HomeworkDetailFragment newFragment = new HomeworkDetailFragment();
		Bundle bundle = new Bundle();

		bundle.putSerializable("homework", homework);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mHomework = (Homework) bundle.getSerializable("homework");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homework_detail, container, false);

		TextView tvAuthor = (TextView) view.findViewById(R.id.tv_homework_author);
		TextView tvPublishTime = (TextView) view.findViewById(R.id.tv_homework_publish_time);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_homework_subject);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_homework_content);

		if (mHomework != null) {
			tvAuthor.setText(mHomework.getAuthorName());
			tvPublishTime.setText(mHomework.getCreateTime());
			tvTitle.setText(mHomework.getSubjectName());
			tvContent.setText(mHomework.getContent());
		}
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
