package com.bpz.commonlibrary.manager;

import android.os.Environment;
import android.util.LruCache;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.interf.ConfigFields;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.MD5Util;
import com.bpz.commonlibrary.util.PackageUtil;
import com.bpz.commonlibrary.util.SDCardUtil;
import com.bpz.commonlibrary.util.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.Sink;

public class NetCacheManager {
    private static final String TAG = "NetCacheManager";
    //max cache size 10mb

    private static volatile NetCacheManager mInstance;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, String> mLruCache;
    private static final int DEFAULT_VALUE_COUNT = 1;

    private NetCacheManager(String cacheDirPath) {
        int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
        int cacheSize = maxMemory / 8;
        //内存缓存
        mLruCache = new LruCache<>(cacheSize);
        //硬盘缓存
        File cacheDir;
        if (StringUtil.isSpace(cacheDirPath)) {
            //为空串则取默认缓存目录
            if (SDCardUtil.hasSDCard()) {
                //有SD卡优先缓存到SD卡
                cacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + ConfigFields.DEFAULT_CACHE_NAME);
            } else {
                //没有就缓存到缓存目录里面
                cacheDir = new File(LibApp.mContext.getCacheDir().getAbsolutePath()
                        + ConfigFields.DEFAULT_CACHE_NAME);
            }
        } else {
            cacheDir = new File(cacheDirPath);

        }
        if (!cacheDir.exists()) {
            //不存在就创建
            boolean mkdirs = cacheDir.mkdirs();
            LogUtil.e(TAG, "mkdirs fail!");
        }
        mDiskLruCache = DiskLruCache.create(FileSystem.SYSTEM, cacheDir,
                PackageUtil.getVersionCode(LibApp.mContext), DEFAULT_VALUE_COUNT,
                ConfigFields.DEFAULT_DISK_CACHE_SIZE);
    }

    public static NetCacheManager getInstance(String cacheDirPath) {
        if (mInstance == null) {
            synchronized (NetCacheManager.class) {
                if (mInstance == null) {
                    mInstance = new NetCacheManager(cacheDirPath);
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
        mLruCache.put(MD5Util.md5(url), data);
    }

    /**
     * 获取内存缓存
     *
     * @param url url
     * @return 返回对应的数据
     */
    public String getInMem(String url) {
        return mLruCache.get(MD5Util.md5(url));
    }

    public void saveInDisk(String url, ResponseBody body) throws Exception {
        LogUtil.d(TAG, url + " : 开始写入缓存...");
        String key = MD5Util.md5(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        InputStream inputStream;
        if (editor != null) {
            Buffer buffer = new Buffer();
            Sink sink = editor.newSink(DEFAULT_VALUE_COUNT);
            byte[] fileReader = new byte[4096];
            inputStream = body.byteStream();
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                buffer.write(fileReader,0,read);
                sink.write(buffer,read);
                buffer.clear();
            }


        }
        /*OutputStream os = editor.newSink();
        os.write(buf);
        os.flush();
        editor.commit();

        mDiskLruCache.flush();

        LogUtil.d(TAG, url + " : 写入缓存完成.");*/
    }
}
