package com.god.spf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Map;

/**
 * Created by abook23 on 2016/8/22.
 * E-mail abook23@163.com
 */
public class Preference {
    private Context context;
    private String defaultSpfName;

    public Context getContext() {
        return context;
    }

    public String getDefaultSpfName() {
        return defaultSpfName;
    }

    public Preference(Context context, String spfName) {
        this.context = context;
        this.defaultSpfName = spfName;
    }

    public static Preference newInstance(Context context, String spfName) {
        return new Preference(context, spfName);
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(defaultSpfName, Context.MODE_PRIVATE);
    }

    public boolean putParam(String key, Object value) {
        SharedPreferences spf = getSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        String type = value.getClass().getSimpleName();
        if ("String".equals(type)) {
            editor.putString(key, (String) value);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) value);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) value);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) value);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) value);
        }
        return editor.commit();
    }

    public void putParam(Map<String, Object> params) {

        SharedPreferences spf = getSharedPreferences(context);
        SharedPreferences.Editor editor = spf.edit();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String type = value.getClass().getSimpleName();
            if ("String".equals(type)) {
                editor.putString(key, (String) value);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) value);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) value);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) value);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) value);
            }
        }
        editor.apply();
    }


    /**
     * 删除 sharedPreferences 文件
     *
     * @return
     */
    public boolean clearData() {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.edit().clear().commit();
    }

    public String getParam(String key) {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.getString(key, null);
    }

    public String getParam(String key, String defValue) {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.getString(key, defValue);
    }

    public int getParam(String key, int defValue) {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.getInt(key, defValue);
    }

    public boolean getParam(String key, boolean defValue) {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.getBoolean(key, defValue);
    }

    public float getParam(String key, float defValue) {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.getFloat(key, defValue);
    }

    public long getParam(String key, long defValue) {
        SharedPreferences spf = getSharedPreferences(context);
        return spf.getLong(key, defValue);
    }

    /**
     * 删除 key --value
     *
     * @param key
     */
    public void remove(String key) {
        SharedPreferences spf = getSharedPreferences(context);
        SharedPreferences.Editor edt = spf.edit();
        edt.remove(key);
        edt.commit();
    }

    public static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }
}
