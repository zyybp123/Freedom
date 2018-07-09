package com.bpz.commonlibrary.entity;

import android.app.PendingIntent;
import android.support.annotation.DrawableRes;

import com.bpz.commonlibrary.util.StringUtil;

/**
 * 通知栏配置数据实体
 */
public class NotifyEntity {
    private String title;
    @DrawableRes
    private int iconRes;
    private String contentText;
    private PendingIntent intent;
    /**
     * FLAG_SHOW_LIGHTS,显示LED
     * FLAG_ONGOING_EVENT,发起正在运行事件（活动中）
     * FLAG_INSISTENT,让声音、振动无限循环，直到用户响应 （取消或者打开）
     * FLAG_ONLY_ALERT_ONCE,发起Notification后，铃声和震动均只执行一次
     * FLAG_AUTO_CANCEL,用户单击通知后自动消失
     * FLAG_NO_CLEAR,只有全部清除时，Notification才会清除
     * FLAG_FOREGROUND_SERVICE,表示正在运行的服务,Service#startForeground
     * FLAG_LOCAL_ONLY,该通知仅与当前设备相关
     * FLAG_GROUP_SUMMARY,组通知，设置它时，还要先设置组Builder#setGroup
     * //hide
     * FLAG_AUTOGROUP_SUMMARY
     * FLAG_CAN_COLORIZE
     */
    private int flags;

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getTitle() {
        return StringUtil.getNotNullStr(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getContentText() {
        return StringUtil.getNotNullStr(contentText);
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public PendingIntent getIntent() {
        return intent;
    }

    public void setIntent(PendingIntent intent) {
        this.intent = intent;
    }
}
