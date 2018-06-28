package com.bpz.commonlibrary.util;

import android.os.Environment;
import android.os.StatFs;

import com.bpz.commonlibrary.LibApp;

import java.io.File;

/**
 * SDCard相关工具类
 */
public class SDCardUtil {
    private SDCardUtil() {
    }

    /**
     * @return SD卡剩余可用空间
     */
    public static long getSDFreeSize() {
        if (hasSDCard()) {
            return new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .getAvailableBytes();
        }
        return 0;
    }

    /**
     * @return 是否存在SD卡
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @return SD卡的总大小
     */
    public static long getSDAllSize() {
        if (hasSDCard()) {
            return new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .getTotalBytes();
        }
        return 0;
    }
}
