package com.stinfo.pushme.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentHelper {
	public static void startDialActivity(Context context, String telephone) {
		Uri uri = Uri.parse("tel:" + telephone);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(intent);
	}
}
