package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * 
 * @author lenovo
 *
 */
public final class CommonPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mFragmentsList;

	public CommonPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
		super(fm);
		mFragmentsList = fragmentsList;
	}
	
	@Override
	public int getCount() {
		return mFragmentsList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentsList.get(position);
	}
	
	@Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
	
	@Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
        // 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象  
        super.destroyItem(container, position, object);  
        Log.i("INFO", "Destroy Item...");  
    }  
}