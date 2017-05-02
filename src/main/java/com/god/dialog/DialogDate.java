package com.god.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.god.R;

/**
 * 日期
 */
public class DialogDate implements View.OnClickListener {

    public DatePicker datePicker;
    public TimePicker timePicker;
    public boolean timePickerState;
    public Button but_ok;
    private AlertDialog dialog;
    private OnDialogDateListener listener;

    public AlertDialog show(Context context) {
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_date_time);
        window.setGravity(Gravity.CENTER);// 此处可以设置dialog显示的位置
        //window.setWindowAnimations(R.style.in_left_out_right_style);
        window.setBackgroundDrawableResource(R.color.transparent);

        datePicker = (DatePicker) window
                .findViewById(R.id.date_time_datePicker1);
        datePicker.setCalendarViewShown(false);
        timePicker = (TimePicker) window.findViewById(R.id.date_time_timePicker);
        but_ok = (Button) window.findViewById(R.id.date_time_but_ok);
        but_ok.setOnClickListener(this);
        return dialog;
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void showTimePicker(boolean b){
        timePickerState = b;
    }

    public void setOnDialogDateListener(OnDialogDateListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (timePickerState){
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
            timePickerState =false;
        }else {
            if (listener!=null){
                listener.dialogDate(datePicker,timePicker);
            }
            dismiss();
        }
    }
    public interface OnDialogDateListener{
        void dialogDate(DatePicker datePicker,TimePicker timePicker);
    }
}
