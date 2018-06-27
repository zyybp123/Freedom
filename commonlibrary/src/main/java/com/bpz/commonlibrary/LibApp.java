package com.bpz.commonlibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.bpz.commonlibrary.interf.listener.OnHeaderOptionListener;

import org.jetbrains.annotations.NotNull;

public class LibApp {
    public static int readTimeOut;
    public static int writeTimeOut;
    public static int connectTimeOut;
    public static OnHeaderOptionListener mHeaderOptionListener;
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    public static SimpleArrayMap<String, String> mBaseUrlMap;

    public static void init(@NonNull Context context, @NonNull SimpleArrayMap<String, String> baseUrlMap) {
        mContext = context;
        mBaseUrlMap = baseUrlMap;
    }
    public static void setMBaseUrlMap(@NotNull SimpleArrayMap<String,String> baseUrlMap){
        mBaseUrlMap = baseUrlMap;
    }
}
