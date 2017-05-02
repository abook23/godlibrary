package com.god.util;

import android.content.Context;
import android.widget.Toast;

import com.god.BuildConfig;

public class ToastUtils {
    private static Toast toast;

    public static void show(Context context, String msg) {
        makeText(context, msg);
    }

    public static void show(Context context, int msg) {
        makeText(context, msg);
    }

    public static void debugShow(Context context, String text) {
        if (BuildConfig.DEBUG)
            makeText(context, text);
    }

    private static void makeText(Context context, Object text) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, String.valueOf(text), Toast.LENGTH_SHORT);
        toast.show();
    }
}
