package com.god.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.god.R;


/**
 * @author abook23
 * @version 1.0
 */
public class DialogMsgBox {

    public AlertDialog alertDialog;
    public TextView but_ok, but_cancel;
    private Context mContext;

    public DialogMsgBox(Context context) {
        mContext = context;
    }

    /**
     * 显示消息提示框
     *
     * @param title
     * @param msg
     */
    public void show(String title, String msg) {
        alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_msgbox);
        window.setGravity(Gravity.CENTER);// 此处可以设置dialog显示的位置
        window.setBackgroundDrawableResource(R.color.transparent);

        but_ok = (TextView) window.findViewById(R.id.but_ok);
        but_cancel = (TextView) window.findViewById(R.id.but_cancel);

        TextView tv_title = (TextView) window.findViewById(R.id.msgbox_title);
        TextView tv_content = (TextView) window
                .findViewById(R.id.msgbox_content);
        tv_title.setText(title);
        tv_content.setText(msg);

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 关闭
     */
    public void dismiss() {
        alertDialog.dismiss();
    }
}
