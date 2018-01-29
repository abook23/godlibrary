package com.god.spf;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.god.util.StringUtils;

import java.util.Map;

/**
 * Created by abook23 on 2016/8/22.
 * E-mail abook23@163.com
 */
public class SharedPreferencesUtils {
    private static SharedPreferencesManger PREFERENCE;

    private SharedPreferencesUtils() {
    }

    /**
     * 只初始化一次就可以了，建议在 baseApplication 初始化
     *
     * @param baseApplicationContent c
     */
    public static void initialize(Context baseApplicationContent) {
        if (PREFERENCE == null) {
            initialize(baseApplicationContent, SharedPreferencesManger.getDefaultSharedPreferencesName(baseApplicationContent));
        } else {
            Log.d("PreferenceUtils", "已经被初始化化了,若果想 打开多 spf,请使用 Preference ");
        }
    }

    /**
     * 只初始化一次就可以了，建议在 baseApplication 初始化
     *
     * @param baseApplicationContent c
     */
    public static void initialize(Context baseApplicationContent, String defaultName) {
        if (PREFERENCE == null) {
            PREFERENCE = SharedPreferencesManger.newInstance(baseApplicationContent, defaultName);
        } else {
            Log.d("PreferenceUtils", "已经被初始化化了,若果想 打开多 spf,请使用 Preference ");
        }
    }

    private static void nullPointerException() {
        if (PREFERENCE == null) {
            throw new NullPointerException("not initialize ---  PreferenceUtils.initialize(context, Constants.SP_NAME);");
        }
    }


    public static boolean putParam(String key, Object value) {
        nullPointerException();
        return PREFERENCE.putParam(key, value);
    }

    public static void putParam(Map<String, Object> params) {
        nullPointerException();
        PREFERENCE.putParam(params);
    }


    public static String getParam(String key) {
        nullPointerException();
        return PREFERENCE.getParam(key);
    }

    public static String getParam(String key, String defValue) {
        nullPointerException();
        return PREFERENCE.getParam(key, defValue);
    }

    public static int getParam(String key, int defValue) {
        nullPointerException();
        return PREFERENCE.getParam(key, defValue);
    }

    public static boolean getParam(String key, boolean defValue) {
        nullPointerException();
        return PREFERENCE.getParam(key, defValue);
    }

    public static float getParam(String key, float defValue) {
        nullPointerException();
        return PREFERENCE.getParam(key, defValue);
    }

    public static long getParam(String key, long defValue) {
        nullPointerException();
        return PREFERENCE.getParam(key, defValue);
    }

    /**
     * 删除 key --value
     *
     * @param key
     */
    public static void remove(String key) {
        nullPointerException();
        PREFERENCE.remove(key);
    }

    public static boolean clearData() {
        nullPointerException();
        return PREFERENCE.clearData();
    }

    /**
     * 删除 sharedPreferences 文件
     *
     * @return
     */
    public boolean clearData(Context context, String spf_name) {
        if (StringUtils.isEmpty(spf_name))
            spf_name = SharedPreferencesManger.getDefaultSharedPreferencesName(context);
        SharedPreferences spf = context.getSharedPreferences(spf_name, Context.MODE_PRIVATE);
        return spf.edit().clear().commit();
    }
}
