package com.bpz.commonlibrary.manager;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.interf.ConfigFields;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.MD5Util;
import com.bpz.commonlibrary.util.PackageUtil;
import com.bpz.commonlibrary.util.SDCardUtil;
import com.bpz.commonlibrary.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import io.reactivex.internal.schedulers.IoScheduler;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * 网络数据缓存管理器
 * 1.内存缓存响应体
 * 2.硬盘缓存响应体（LruCache）
 */
public class NetCacheManager {
    private static final String TAG = "NetCacheManager";
    //the maximum number of bytes this cache should use to store，一个key可以对应多少个缓存文件
    private static final int DEFAULT_VALUE_COUNT = 1;
    private static final String DATA_MEDIA_TYPE = "application/json";
    private static volatile NetCacheManager mInstance;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, String> mLruCache;

    private NetCacheManager() {
        int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
        int cacheSize = maxMemory / 8;
        //内存缓存
        mLruCache = new LruCache<>(cacheSize);
        //硬盘缓存
        File cacheDir = makeCacheDir();
        mDiskLruCache = DiskLruCache.create(FileSystem.SYSTEM, cacheDir,
                PackageUtil.getVersionCode(LibApp.mContext), DEFAULT_VALUE_COUNT,
                ConfigFields.DEFAULT_DISK_CACHE_SIZE);
    }

    @NonNull
    private static File makeCacheDir() {
        File cacheDir;
            cacheDir = new File(LibApp.mContext.getCacheDir().getAbsolutePath()
                    + File.separator + PackageUtil.getPackageName(LibApp.mContext)
                    + ConfigFields.DEFAULT_CACHE_NAME);
        if (!cacheDir.exists() && cacheDir.isDirectory()) {
            //不存在就创建
            boolean mkdirs = cacheDir.mkdirs();
            LogUtil.e(TAG, "mkdirs fail!");
        }
        return cacheDir;
    }

    @NonNull
    public static Cache getCache() {
        File file = makeCacheDir();
        return new Cache(file, ConfigFields.DEFAULT_DISK_CACHE_SIZE);
    }

    public static NetCacheManager getInstance() {
        if (mInstance == null) {
            synchronized (NetCacheManager.class) {
                if (mInstance == null) {
                    mInstance = new NetCacheManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 缓存至内存
     *
     * @param url  url
     * @param data 对应的数据
     */
    public void saveInMem(String url, String data) {
        if (StringUtil.isSpace(url) || StringUtil.isSpace(data)) {
            //url或数据为空串，则不缓存
            return;
        }
        mLruCache.put(MD5Util.md5(url), data);
    }

    /**
     * 获取内存缓存
     *
     * @param url url
     * @return 返回对应的数据
     */
    @Nullable
    public String getInMem(String url) {
        return mLruCache.get(MD5Util.md5(url));
    }

    /**
     * 缓存到硬盘
     *
     * @param url   路径
     * @param bytes 响应体字节文件
     * @throws IOException 抛出对应异常
     */
    public void saveInDisk(String url, byte[] bytes) throws IOException {
        if (StringUtil.isSpace(url) || bytes == null) {
            //如果url为空或响应体为空，直接返回
            LogUtil.e(TAG, "url: " + url + ", " + Arrays.toString(bytes));
            return;
        }
        DiskLruCache.Editor editor = mDiskLruCache.edit(MD5Util.md5(url));
        if (editor != null) {
            Sink sink = editor.newSink(DEFAULT_VALUE_COUNT - 1);
            Okio.buffer(sink).write(bytes).flush();
            editor.commit();
        }
    }

    /**
     * 从硬盘获取缓存
     *
     * @param url       路径
     * @param mediaType contentType
     * @return 返回响应体
     * @throws IOException 抛出对应异常
     */
    @Nullable
    public ResponseBody getInDisk(String url, String mediaType) throws IOException {
        if (StringUtil.isSpace(url) || StringUtil.isSpace(mediaType)) {
            //如果url为空或类型为空，直接返回null
            LogUtil.e(TAG, "url: " + url + ", media type:" + mediaType);
            return null;
        }

        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Util.md5(url));
        if (snapshot != null) {
            Source source = snapshot.getSource(DEFAULT_VALUE_COUNT - 1);
            if (source != null) {
                byte[] bodyBytes = Okio.buffer(source).readByteArray();
                return ResponseBody.create(MediaType.parse(DATA_MEDIA_TYPE), bodyBytes);
            }
        }
        return null;
    }
}
