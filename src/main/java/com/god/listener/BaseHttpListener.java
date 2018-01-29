package com.god.listener;

/**
 * Created by abook23 on 2016/7/11.
 */
public interface BaseHttpListener<T>  {
    /**
     * 成功
     */
    void onHttpSuccess(T t, int requestCode);

    /**
     * 失败
     */
    void onHttpError(String msg, int resultCode, int requestCode);
}
