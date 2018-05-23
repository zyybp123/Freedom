package com.bpz.commonlibrary.util;

import android.util.Log;

import com.bpz.commonlibrary.BuildConfig;

/**
 * Created by Administrator on 2018/5/18.
 * 控制日志打印
 */

public class LogUtil {


    public static final boolean IS_SHOW_LOG = BuildConfig.HAS_LOG;
    private static String TAG = LogUtil.class.getSimpleName();

    private LogUtil() {
    }

    public static void d(String tag, String msg) {
        if (IS_SHOW_LOG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_SHOW_LOG) {
            Log.e(tag, msg);
        }
    }

    public static void d(Object object, String msg) {
        if (IS_SHOW_LOG) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }

    public static void e(Object object, String msg) {
        if (IS_SHOW_LOG) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static String getLogTag(Object o) {
        return o == null ? TAG : o.getClass().getSimpleName();
    }
}
