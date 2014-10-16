package com.stinfo.pushme.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerBinder {

	private final static String TAG = "SpinnerBinder";

	public static ArrayAdapter<MapItem> bindData(Context context, Spinner spinner, int strResId) {
		Resources res = context.getResources();
		ArrayList<MapItem> list = new ArrayList<MapItem>();

		String[] items = res.getStringArray(strResId);
		for (int i = 0; i < items.length; i++) {
			try {
				MapItem mapItem = new MapItem(items[i]);
				list.add(mapItem);
			} catch (Exception ex) {
				Log.e(TAG, "Invalid pair string fromat " + strResId);
				continue;
			}
		}

		ArrayAdapter<MapItem> adapter = new ArrayAdapter<MapItem>(context,
				android.R.layout.simple_spinner_item, list);
		spinner.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
}
