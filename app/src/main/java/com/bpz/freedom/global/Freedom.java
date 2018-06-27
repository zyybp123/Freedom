package com.bpz.freedom.global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.manager.MyActivityManager;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.net.KaiHost;
import com.bpz.freedom.net.TzqHost;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;
import java.util.Map;

public class Freedom extends Application {
    /**
     * 静态的上下文，全局可用
     */
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    public static SimpleArrayMap<String, String> mBaseUrlMap = new SimpleArrayMap<>();
    /**
     * Activity管理器
     */
    private MyActivityManager mActivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        mContext = this;
        //初始化配置各种ServerHost
        initBaseUrl();
        //初始化lib
        LibApp.init(this, mBaseUrlMap);
        //管理Activity
        mActivityManager = MyActivityManager.getInstance();
        activityLifeManage();

    }

    private void initBaseUrl() {
        mBaseUrlMap.put(TzqHost.TAG_TZQ, TzqHost.BASE_URL_TZQ);
        mBaseUrlMap.put(KaiHost.TAG_KAI_SHU, KaiHost.BASE_URL_KAI_SHU_STORY_TEST);
    }

    private void activityLifeManage() {
        //注册Activity生命周期回调
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtil.e(activity, "onActivityCreated");
                // 添加Activity到集合
                mActivityManager.addActivity(activity);
                mActivityManager.setTopActivityWeakRef(activity);
            }


            @Override
            public void onActivityStarted(Activity activity) {
                LogUtil.e(activity, "onActivityStarted");
                mActivityManager.setTopActivityWeakRef(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtil.e(activity, "onActivityResumed");
                mActivityManager.setTopActivityWeakRef(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtil.e(activity, "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtil.e(activity, "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtil.e(activity, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtil.e(activity, "onActivityDestroyed");
                // 结束Activity&从集合中移除
                mActivityManager.removeActivity(activity);
            }
        });
    }
}
