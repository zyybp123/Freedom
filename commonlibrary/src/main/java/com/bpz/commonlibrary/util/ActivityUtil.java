package com.bpz.commonlibrary.util;

import android.app.Activity;
import android.content.Intent;

/**
 * activity的工具类
 */
public class ActivityUtil {
    private ActivityUtil() {
    }

    /**
     * 启动Activity的静态方法
     *
     * @param activity 启动的Activity
     * @param clazz    目标Activity的class
     */
    public void startActivity(Activity activity, Class<? extends Activity> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
    }


}
