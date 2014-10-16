package com.stinfo.pushme.view;

import com.stinfo.pushme.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

public class LoadingDialog {

	private Context mContext = null;
	private Dialog loadingDialog = null;

	public LoadingDialog(Context context, boolean cancelable) {
		this.mContext = context;

		loadingDialog = new Dialog(mContext, R.style.CustomProgressDialog);
		loadingDialog.setTitle("");
		loadingDialog.setCancelable(cancelable);
		loadingDialog.setContentView(R.layout.dialog_loading);
	}

	public void show() {
		if ((loadingDialog != null) && (!loadingDialog.isShowing()) && (mContext != null)
				&& (!((Activity) mContext).isFinishing())) {
			loadingDialog.show();
		}
	}

	public void close() {
		if ((loadingDialog != null) && (loadingDialog.isShowing()) && (mContext != null)
				&& (!((Activity) mContext).isFinishing())) {
			loadingDialog.dismiss();
		}
	}
}
