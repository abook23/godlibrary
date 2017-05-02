package com.god.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.god.listener.OnNetStatusListener;

/**
 * 时时监听网络状态
 *  2015-5-10 下午8:10:04
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    
    //filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    public static OnNetStatusListener onNetListener;
    private static long oldTime;
    /**
     * -1 NoNetWork; 0 TYPE_MOBILE; 1 TYPE_WIFI; 2 TYPE_ETHERNET; 3 OtherNetWork
     */
    public static int netType;
    public static String netName;


    public static void setOnNetListener(OnNetStatusListener listener) {
        onNetListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        long time = System.currentTimeMillis();
        if (time - oldTime < 1000) {
            return;
        }
        oldTime = time;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()) {
            switch (netInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    netType = 0;
                    netName = netInfo.getSubtypeName();
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    netType = 1;
                    netName = netInfo.getTypeName();
                    break;
                case ConnectivityManager.TYPE_ETHERNET:
                    netType = 2;
                    netName = netInfo.getTypeName();
                    break;
                default:
                    netType = 3;
                    netName = "OtherNetWork";
                    break;
            }
        } else {
            netType = -1;
            netName = "NoNetWork";
        }
        if (onNetListener != null) {
            onNetListener.onNetStatus(netType, netName);
        }
    }
}
