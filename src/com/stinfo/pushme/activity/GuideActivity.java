package com.stinfo.pushme.activity;

import java.util.ArrayList;

import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.GuidePagerAdapter;
import com.stinfo.pushme.BaseActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GuideActivity extends BaseActionBarActivity implements OnPageChangeListener {
	private static final String TAG = "GuideActivity";
	private ViewPager mViewPager = null;
	private ArrayList<View> mViews = null;
	private GuidePagerAdapter mAdapter = null;
	private ImageView[] mPoints = null;
	private int mCurrentIndex;
	private boolean mLastPageScrolling = false;

	private static final int[] mPics = { R.drawable.user_guide1, R.drawable.user_guide2,
			R.drawable.user_guide3, R.drawable.user_guide4 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		initView();
		initData();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		mViews = new ArrayList<View>();
		mAdapter = new GuidePagerAdapter(mViews);
	}

	private void initData() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		for (int i = 0; i < mPics.length; i++) {
			ImageView guideView = new ImageView(this);
			guideView.setLayoutParams(params);
			guideView.setScaleType(ScaleType.FIT_XY);
			guideView.setImageResource(mPics[i]);
			mViews.add(guideView);
		}

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		initPoint();
	}

	private void initPoint() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_points);
		mPoints = new ImageView[mPics.length];

		for (int i = 0; i < mPics.length; i++) {
			mPoints[i] = (ImageView) linearLayout.getChildAt(i);
			mPoints[i].setEnabled(true);
			mPoints[i].setTag(i);

			mPoints[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					setCurView(position);
					setCurDot(position);
				}
			});
		}

		mCurrentIndex = 0;
		mPoints[mCurrentIndex].setEnabled(false);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Log.d(TAG, "[onPageScrollStateChanged] arg0=" + arg0);
		if ((mCurrentIndex == (mViews.size() - 1)) && (arg0 == 1)) {
			mLastPageScrolling = true;
		} else {
			mLastPageScrolling = false;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.d(TAG, "[onPageScrolled] arg0=" + arg0 + "|arg1=" + arg1 + "|arg2=" + arg2);

		if (mLastPageScrolling && (arg0 == (mViews.size() - 1))) {
			Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
			GuideActivity.this.startActivity(intent);
			GuideActivity.this.finish();
		}
	}

	@Override
	public void onPageSelected(int position) {
		setCurDot(position);
	}

	private void setCurView(int position) {
		if (position < 0 || position >= mPics.length) {
			return;
		}
		mViewPager.setCurrentItem(position);
	}

	private void setCurDot(int positon) {
		if (positon < 0 || positon > mPics.length - 1 || mCurrentIndex == positon) {
			return;
		}

		mPoints[positon].setEnabled(false);
		mPoints[mCurrentIndex].setEnabled(true);
		mCurrentIndex = positon;
	}
}
