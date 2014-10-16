package com.stinfo.pushme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Daily;


public class DailyDetailActivity extends BaseActionBarFragmentActivity  {
	private Daily mDaily;
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_detail);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setTitle(getResources().getString(R.string.view_content));
		initView();
		
	}
	
	private void initView() {
		mDaily = (Daily) getIntent().getSerializableExtra("daily");
		TextView tvAuthor = (TextView) findViewById(R.id.tv_author);
		TextView tvPublishTime = (TextView) findViewById(R.id.tv_publish_time);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		TextView tvContent = (TextView)findViewById(R.id.tv_content);
		
		tvAuthor.setText(mDaily.getAuthorName());
		tvPublishTime.setText(mDaily.getCreateTime());
		tvTitle.setText(mDaily.getReceiverName());
		tvContent.setText(mDaily.getContent());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
}
