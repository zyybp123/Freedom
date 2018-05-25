package com.bpz.commonlibrary.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 * Activity 管理类
 */

public class MyActivityManager {
    private static volatile MyActivityManager mInstance;
    private WeakReference<Activity> sTopActivityWeakRef;
    private List<Activity> activityList;

    private MyActivityManager() {
        activityList = new ArrayList<>();
    }

    public static MyActivityManager getInstance() {
        if (mInstance == null) {
            synchronized (MyActivityManager.class) {
                if (mInstance == null) {
                    mInstance = new MyActivityManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!activityList.contains(activity)) {
            activityList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁多个Activity
     *
     * @param activityArray 可变参数，可传入多个Activity
     */
    public void removeActivity(Activity... activityArray) {
        for (Activity anActivity : activityArray) {
            if (activityList.contains(anActivity)) {
                activityList.remove(anActivity);
                anActivity.finish();
            }
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public void setTopActivityWeakRef(Activity activity) {
        if (sTopActivityWeakRef == null || !activity.equals(sTopActivityWeakRef.get())) {
            sTopActivityWeakRef = new WeakReference<>(activity);
        }
    }
}
