package com.god.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.god.R;


/**
 * Created by yzz on 2016/4/7.
 */
public class Notifications {

    private Context context;
    /**
     * Notification构造器
     */
    public NotificationCompat.Builder mBuilder;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;

    public Notifications(Context context) {
        this.context = context;
        initService();
        initNotify();
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("测试标题")
                .setContentText("测试内容")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        //.setSmallIcon(R.mipmap.ic_launcher);
    }

    /**
     * 初始化要用到的系统服务
     */
    private void initService() {
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
    }


    /**
     * 显示通知栏
     *
     * @param title
     * @param content
     * @param ticker
     */

    public void showNotify(String title, String content, String ticker, int notifyId) {
        mBuilder.setContentTitle(title)
                .setContentText(content)
//				.setNumber(number)//显示数量
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setTicker(ticker);//通知首次出现在通知栏，带上升动画效果
        mNotificationManager.notify(notifyId, mBuilder.build());
//		mNotification.notify(getResources().getString(R.string.app_name), notiId, mBuilder.build());
    }


    /**
     * 显示常驻通知栏
     */
    public void showCzNotify(String title, String content, String ticker, int notifyId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//		//PendingIntent 跳转动作
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,Intent.getIntent(), 0);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content);
//                .setContentIntent(pendingIntent);
        Notification mNotification = mBuilder.build();
        //设置通知  消息  图标
        mNotification.icon = R.mipmap.ic_launcher;
        //在通知栏上点击此通知后自动清除此通知
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调
        //设置显示通知时的默认的发声、震动、Light效果
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        //设置发出消息的内容
//        mNotification.tickerText = "通知来了";
        //设置发出通知的时间
        mNotification.when = System.currentTimeMillis();
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
//		mNotification.setLatestEventInfo(this, "常驻测试", "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息  ,这个方法现在已经不用了
        mNotificationManager.notify(notifyId, mNotification);
    }


    /**
     * @param title    标题
     * @param content  内容
     * @param ticker   ticker
     * @param activity 点击跳转 的 activity
     * @param notifyId id
     * @param sound    声音
     */
    public void showIntentActivityNotify(String title, String content, String ticker, Class<?> activity, int notifyId, boolean sound) {
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker);
        if (sound) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
        } else {
            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        }
        if (activity != null) {
            //点击的意图ACTION是跳转到Intent
            Intent resultIntent = new Intent(context, activity);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
//		mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }


    /**
     * 获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @param flags 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * @return
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }
}
