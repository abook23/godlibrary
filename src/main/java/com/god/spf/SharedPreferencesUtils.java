package com.god.spf;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.god.util.StringUtils;

import java.util.Map;

/**
 * Android 文件存储 2015年9月6日 10:12:21
 *
 * @author abook23
 * @version 1.0
 */
public class SharedPreferencesUtils {

    private SharedPreferences sharedPreferences;
    private Context context;
    private String name;

    public SharedPreferencesUtils(Context context, String name) {
        this.context = context;
        this.name = name;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_APPEND);
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


    public void updateSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_APPEND);
    }

    public void putString(String key, String value) {
        Editor edt = sharedPreferences.edit();
        edt.putString(key, value);
        edt.commit();
    }

    public void putBoolean(String key, boolean value) {
        Editor edt = sharedPreferences.edit();
        edt.putBoolean(key, value);
        edt.commit();
    }

    public void putInt(String key, int value) {
        Editor edt = sharedPreferences.edit();
        edt.putInt(key, value);
        edt.commit();
    }

    public void putLong(String key, long value) {
        Editor edt = sharedPreferences.edit();
        edt.putLong(key, value);
        edt.commit();
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }


    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }


    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }


    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }


    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }


    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 删除 key --value
     *
     * @param key
     */
    public void remove(String key) {
        Editor edt = sharedPreferences.edit();
        edt.remove(key);
        edt.commit();
    }

    /**
     * 删除 sharedPreferences 文件
     *
     * @return
     */
    public boolean clearData() {
        return sharedPreferences.edit().clear().commit();
    }

}
