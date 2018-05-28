package com.bpz.commonlibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class LibApp extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }
}
