package com.god.util;

import android.util.Log;

import java.util.Locale;


/**
 * Log工具 <BR>
 * <p>
 * 输出格式:className.methodName(L:lineNumber)  content。
 */
public class L {

    public static String TAG = "mLogUtil";

    private L() {
    }

    public static boolean allow = true;

    private static String getPrefix() {
        //String prefix = "%s%s(L:%d)";
        String prefix = "%s";
        StringBuffer sb = new StringBuffer();

        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = caller.getClassName();
        sb.append(callerClazzName).append(".").append(caller.getMethodName()).append(" (").append(caller.getFileName()).append(":").append(caller.getLineNumber()).append(")").append("\n");
        //prefix = String.format(Locale.getDefault(), prefix, sb.toString(), caller.getMethodName(), caller.getLineNumber());
        prefix = String.format(Locale.getDefault(), prefix, sb.toString());

        return prefix;
    }

    /**
     * Send a DEBUG log message,蓝色
     */
    public static void d(String content) {
        if (allow) {
            Log.d(TAG, getPrefix() + " --> " + content);
        }
    }

    public static void d(int content) {
        if (allow) {
            Log.d(TAG, getPrefix() + " --> " + content);
        }
    }

    public static void d(String TAG, String content) {
        if (allow) {
            Log.d(TAG, getPrefix() + " --> " + content);
        }
    }

    public static void d(String TAG, int content) {
        if (allow) {
            Log.d(TAG, getPrefix() + " --> " + content);
        }
    }

    /**
     * Send a ERROR log message and log the exception,红色
     */
    public static void e(String content, Throwable e) {
        if (allow) {
            Log.e(TAG, getPrefix() + " --> " + content, e);
        }
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void e(String content) {
        if (allow) {
            Log.e(TAG, getPrefix() + " --> " + content);
        }
    }

    /**
     * Send an INFO log message,绿色
     */
    public static void i(String content) {
        if (allow) {
            Log.i(TAG, getPrefix() + " --> " + content);
        }
    }

    public static void i(String TAG, String content) {
        if (allow) {
            Log.i(TAG, getPrefix() + " --> " + content);
        }
    }

    public static void i(String TAG, int content) {
        if (allow) {
            Log.i(TAG, getPrefix() + " --> " + content);
        }
    }

    /**
     * Send a VERBOSE log message,黑色
     */
    public static void v(String content) {
        if (allow) {
            Log.v(TAG, getPrefix() + " --> " + content);
        }
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void w(String content) {
        if (allow) {
            Log.w(TAG, getPrefix() + " --> " + content);
        }
    }

}
