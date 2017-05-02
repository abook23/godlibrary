package com.god.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

/**
 * GPS 网络判断
 * 
 */
public class DialogGpsNet {

	private Context mContext;
	private Dialog dialog;

	public DialogGpsNet(Context context) {
		this.mContext = context;
	}

	public void show(final boolean exitApp) {
		dialog = new AlertDialog.Builder(mContext)
				.setIcon(android.R.drawable.btn_dialog).setTitle("定位提示")
				.setMessage("定位失败")
				.setPositiveButton("开启GPS", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				}).setNeutralButton("开起网络定位", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						mContext.startActivity(intent);
					}
				}).setNegativeButton("退出", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (exitApp) {
							System.exit(0);
						}
					}
				}).create();
		if (exitApp) {
			dialog.setCancelable(false);// 点击不可消除
		}
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}
