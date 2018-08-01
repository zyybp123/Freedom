package com.bpz.commonlibrary.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.entity.NotifyEntity;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 通知管理器
 */
public class MyNotificationManager {
    private static final String TAG = "MyNotificationManager";
    public static final int MAX_PROGRESS = 100;
    public static final int MIN_PROGRESS = 0;
    public static final int NOTIFY_ID = 2;
    public static final String CHANNEL_ID = "FR_DOWNLOAD";
    private static final String FR_CHANNEL_NAME = "FR_CHANNEL_NAME";

    private static volatile MyNotificationManager mInstance;
    private NotificationCompat.Builder notificationBuilder;
    private Notification mNotification;
    private NotificationManager mNotificationManager;

    private MyNotificationManager(@NotNull Context context) {
        //获取通知管理器
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //适配Android8.0的通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelId(CHANNEL_ID);
        }
        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannelId(String channelId) {
        if (mNotificationManager == null) {
            return;
        }
        NotificationChannelGroup group = new NotificationChannelGroup(channelId, "FreeDom");
        mNotificationManager.createNotificationChannelGroup(group);
        NotificationChannel chan = new NotificationChannel(channelId,
                FR_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        //是否允许振动
        chan.enableVibration(false);
        //是否允许闪烁灯
        chan.enableLights(false);
        //锁屏的时候是否展示通知
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mNotificationManager.createNotificationChannel(chan);
    }

    public static MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MyNotificationManager.class) {
                if (mInstance == null) {
                    mInstance = new MyNotificationManager(context);
                }
            }
        }
        return mInstance;
    }


    /**
     * 发送一个默认的通知
     *
     * @param entity 配置该通知的数据实体
     */
    public void sendDefaultNotification(int notifyId, NotifyEntity entity) {
        if (notificationBuilder == null || entity == null || mNotificationManager == null) {
            return;
        }
        notificationBuilder
                .setWhen(System.currentTimeMillis())
                .setContentTitle(entity.getTitle())
                .setContentText(entity.getContentText())
                //.setDefaults(NotificationCompat.DEFAULT_ALL)
                .setGroup(NotifyEntity.getGroupName(entity.getGroup()))
                //.setGroupSummary(true)
                .setSmallIcon(entity.getIconRes());
        //不为空才设置intent
        PendingIntent intent = entity.getIntent();
        if (intent != null) {
            notificationBuilder.setContentIntent(intent);
        }
        mNotification = notificationBuilder.build();
        mNotification.flags = entity.getFlags();
        mNotificationManager.notify(notifyId, mNotification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int number = getNumberOfNotifications();
            LogUtil.e(TAG,"current number: " + number);
            //判断是否需要发送分组消息
            if (number > 1) {
                //超过一条，要发送分组消息
                sendGroupMsg(entity);
            }
        }
    }

    /**
     * 获取当前状态栏具有统一id的通知的数量
     *
     * @return 数量
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private int getNumberOfNotifications() {
        //查询当前展示的所有通知的状态列表
        StatusBarNotification[] activeNotifications = mNotificationManager.getActiveNotifications();
        return activeNotifications.length;
    }

    public void sendGroupMsg(NotifyEntity entity) {
        notificationBuilder
                .setWhen(System.currentTimeMillis())
                .setContentTitle(entity.getTitle())
                .setContentText(entity.getContentText())
                //.setDefaults(NotificationCompat.DEFAULT_ALL)
                .setGroup(NotifyEntity.getGroupName(entity.getGroup()))
                .setGroupSummary(true)
                .setSmallIcon(entity.getIconRes());
        //判断是否要发分组消息
        mNotificationManager.notify(entity.getGroup() + 1000, notificationBuilder.build());
    }

    public void sendNotification(@NotNull Context context, String title,
                                 String ticker, @DrawableRes int iconRes) {
        title = StringUtil.getNotNullStr(title);
        ticker = StringUtil.getNotNullStr(ticker);


        notificationBuilder.setContentTitle(title)
                .setSmallIcon(iconRes)
                .setProgress(MAX_PROGRESS, MIN_PROGRESS, false)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis());
        mNotification = notificationBuilder.build();
        //发送一个通知
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    /**
     * 进度更新，要注意通知的发送频率
     *
     * @param progress 实时的进度
     */
    public void updateProgress(int progress) {
        if (notificationBuilder != null && mNotificationManager != null) {
            //更新进度条
            notificationBuilder.setProgress(MAX_PROGRESS, progress, false);
            notificationBuilder.setContentText(progress + "%");
            mNotificationManager.notify(NOTIFY_ID, notificationBuilder.build());
        }
    }

    public void hideProgress() {
        if (notificationBuilder != null && mNotificationManager != null) {
            //移除进度条
            notificationBuilder.setProgress(MIN_PROGRESS, MIN_PROGRESS, false);
            notificationBuilder.setContentText("下载完成，点击打开！");
            mNotificationManager.notify(NOTIFY_ID, notificationBuilder.build());
        }
    }

    public void cancelNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFY_ID);
        }
    }
}
