package com.liuniukeji.mixin.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.discover.GroupNoticeActivity;
import com.liuniukeji.mixin.ui.discover.InterestGroupActivity;
import com.liuniukeji.mixin.ui.main.MainActivity;
import com.liuniukeji.mixin.ui.message.ContactListActivity;
import com.liuniukeji.mixin.ui.message.NewFriedsApplyActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.widget.WebViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description
 * @Author (LiShiyang / 845719506 @ qq.com)
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date ${Date}
 * @CreateBy Android Studio
 */
public class JiGuangReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //获取接收的消息
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);//保存服务器推送下来的消息的标题
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);//保存服务器推送下来的消息内容
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA); //保存服务器推送下来的附加字段
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识消息的 ID, 可用于上报统计等
        Log.e("www", "接收到了推送下来的消息"
                + "标题：" + title
                + "消息内容：" + message
                + "附加字段" + extras
                + "唯一标示ID：" + file);
//        postNotification(context, title, message);
        NotificationUtils notificationUtils = new NotificationUtils(context);
        if (title == null || title.isEmpty())
            title = "秘信";
        if (message != null && !message.isEmpty())
            notificationUtils.sendNotification(title, message, MainActivity.class);


        //处理打开通知的操作
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            // 在这里可以自己写代码去定义用户点击后的行为
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            //根据extra 做判断
            try {
                JSONObject jsonObject = new JSONObject(extra);
                int type = jsonObject.optInt("type");
                String content = jsonObject.optString("content");
                switch (type) {
                    case 1:
                        //系统通知
                        String url = UrlUtils.sysNotice + content;
                        intent.putExtra("title", "系统通知");
                        intent.putExtra("url", url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, WebViewActivity.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        //群消息通知
                        Intent i = new Intent(context, InterestGroupActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        break;
                    case 3:
                        //新朋友
                        Intent i2 = new Intent(context, NewFriedsApplyActivity.class);
                        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i2);
                        break;
                    case 4:
                        //通讯录
                        Intent i3 = new Intent(context, ContactListActivity.class);
                        i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i3);
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    NotificationManager manager;

    @SuppressLint("WrongConstant")
    private void postNotification(Context context, String title, String message) {
        if (message == null || message.isEmpty())
            return;
        //获取NotifactionManager对象
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //构建一个Notifaction的Builder对象
        Notification.Builder builder = new Notification.Builder(context);
        //设置通知相关信息
        builder.setTicker("你有一条新的通知");//设置信息提示
        builder.setSmallIcon(R.mipmap.logo1);//设置通知提示图标
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo1));//设置图标
        if (title == null || title.isEmpty())
            builder.setContentTitle("秘信");
        else
            builder.setContentTitle(title);//设置标题
        builder.setContentText(message);//设置文本
        builder.setAutoCancel(true);//查看后自动取消
        builder.setWhen(new Date().getTime());//什么时候发出的通知
        builder.setDefaults(Notification.DEFAULT_LIGHTS);//消息提示模式
        //设置点击通知后执行的动作
        Intent mIntent = new Intent(context, MainActivity.class);
        //用当前时间充当通知的id，这里是为了区分不同的通知，如果是同一个id，前者就会被后者覆盖
        int requestId = (int) new Date().getTime();
        //第一个参数连接上下文的context
        // 第二个参数是对PendingIntent的描述，请求值不同Intent就不同
        // 第三个参数是一个Intent对象，包含跳转目标
        // 第四个参数有4种状态
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        //发出通知，参数是（通知栏的id，设置内容的对象）
        //8.0以上的系统要新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("message", "秘信", NotificationManager.IMPORTANCE_MAX));
        }
        manager.notify(requestId, builder.build());
    }
}
