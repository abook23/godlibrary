package com.god.listener;

/**
 * Created by abook23 on 2016/3/25.
 */
public interface OnNetStatusListener {
    /**
     * -1 NoNetWork; 0 TYPE_MOBILE; 1 TYPE_WIFI; 2 TYPE_ETHERNET; 3 OtherNetWork
     *
     * @param netType
     * @param netName
     */
    void onNetStatus(int netType, String netName);
}
