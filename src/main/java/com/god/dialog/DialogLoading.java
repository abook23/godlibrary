package com.god.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.god.R;

/**
 * 等待dialog
 *
 * @author abook23@163.com
 * 2015-6-15 下午3:42:15
 */
public class DialogLoading {

    public AlertDialog alertDialog;
    private Context mContext;
    public TextView msg;

    public DialogLoading(Context context) {
        this.mContext = context;
    }

    /**
     * DialogLoading loading = new DialogLoading(context)
     * loading.show("123")
     *
     * @param msg 消息
     */
    public AlertDialog show(String msg) {
        alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_loading);
        window.setGravity(Gravity.CENTER);// 此处可以设置dialog显示的位置
        //window.setWindowAnimations(R.style.DialogAnimation);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setDimAmount(0f);//覆盖成透明度

        this.msg = (TextView) window.findViewById(R.id.textView1);
        this.msg.setText(msg);
        //alertDialog.setCancelable(false);// 点击退出
        return alertDialog;
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    /**
     * 点击退出
     *
     * @param b 1
     */
    public void setCancelable(boolean b) {
        alertDialog.setCancelable(b);// 点击退出 f
    }

}
