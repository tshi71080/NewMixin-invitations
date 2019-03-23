package com.liuniukeji.mixin.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description 通知栏工具类
 * @Author 你真好看
 * @Date 2018年11月27日 11:21:51
 * @CreateBy Android Studio
 */

public class NotificationUtils extends ContextWrapper {
    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public void sendNotification(String title, String content, Class cls) {
        //设置点击通知后执行的动作
        Intent mIntent = new Intent(getApplicationContext(), cls);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) new Date().getTime(), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).setContentIntent(pendingIntent).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(title, content).setContentIntent(pendingIntent).build();
            getManager().notify(1, notification);
        }
    }
}
