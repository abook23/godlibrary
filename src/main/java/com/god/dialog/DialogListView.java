package com.god.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.god.R;


/**
 * Created by abook23 on 2015/9/20.
 */
public class DialogListView {
    public ListView listView;
    public Button submit;
    public AlertDialog dialog;

    public AlertDialog show(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_listview_select, null);
        listView = (ListView) v.findViewById(R.id.listView);
        submit = (Button) v.findViewById(R.id.but_submit);
        dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(R.color.transparent);

        return dialog;
    }

    /**
     * 点击退出 默认 true
     *
     * @param flag 设定文件
     */
    public void setCancelable(boolean flag) {
        dialog.setCancelable(flag);
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
