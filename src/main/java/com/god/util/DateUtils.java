package com.god.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDate(long dataTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(dataTime);
    }

    /**
     *
     * @param dateStr
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String format(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(dateStr);
    }

    /**
     *
     * @param dataTime
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYMDDate(long dataTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(dataTime);
    }

    /**
     *
     * @param dateType d
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDate(String dateType) {
        SimpleDateFormat format = new SimpleDateFormat(dateType);
        return format.format(new Date());
    }
}
