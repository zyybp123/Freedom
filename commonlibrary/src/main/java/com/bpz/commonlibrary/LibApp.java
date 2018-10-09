package com.bpz.commonlibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.bpz.commonlibrary.interf.listener.OnHeaderOptionListener;
import com.bpz.commonlibrary.util.ChineseToHanYuPYTest;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.jetbrains.annotations.NotNull;

/**
 * 基础库全局配置入口
 */
public class LibApp {
    public static int readTimeOut;
    public static int writeTimeOut;
    public static int connectTimeOut;
    public static OnHeaderOptionListener mHeaderOptionListener;
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    public static SimpleArrayMap<String, String> mBaseUrlMap;
    public static boolean needCache;
    public static ChineseToHanYuPYTest chineseToHanYuPYTest;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.fr_colorPrimary, android.R.color.white);
                // 指定为经典Header，默认是 贝塞尔雷达Header
                //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private LibApp() {
    }

    public static void init(@NonNull Context context, @NonNull SimpleArrayMap<String, String> baseUrlMap) {
        mContext = context;
        mBaseUrlMap = baseUrlMap;
        //初始化加载多音字字典
        chineseToHanYuPYTest = new ChineseToHanYuPYTest("duoyinzi_dic.txt");

    }

    public static void setMBaseUrlMap(@NotNull SimpleArrayMap<String, String> baseUrlMap) {
        mBaseUrlMap = baseUrlMap;
    }
}
