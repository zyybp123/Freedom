package com.bpz.commonlibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class PackageUtil {
    private PackageUtil() {
    }

    /**
     * @return 版本名称
     */
    public static String getVersionName(@NonNull Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return "1.0";
        }
        return packageInfo.versionName;
    }

    @Nullable
    private static PackageInfo getPackageInfo(@NonNull Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 包名
     */
    public static String getPackageName(@NotNull Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info == null) {
            return "bpz";
        }
        return info.packageName;
    }

    /**
     * @return 版本号
     */
    public static int getVersionCode(@NonNull Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info == null) {
            return 1;
        }
        return info.versionCode;
    }

}
