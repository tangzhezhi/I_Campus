package com.stinfo.pushme.activity;

import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends BaseActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.about_me));
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.about_exit_item, menu);
		//this.getMenuInflater().inflate(R.menu.contact, menu);
		//MenuItem mitemExit = menu.findItem(R.id.action_search);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.action_exit_item:
			this.finish();
		}
		return true;
	}

	private void initView() {
	}
}
