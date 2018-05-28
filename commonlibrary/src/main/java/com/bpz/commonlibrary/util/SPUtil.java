package com.bpz.commonlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.SimpleArrayMap;

import com.bpz.commonlibrary.LibApp;


/**
 * sharedPreferences的工具类
 * <p>
 * 分别有commit方式和apply方式，其中如果不考虑操作结果建议使用apply
 * apply方法写入快，效率高些，不会阻塞调用线程，但可能将等待的时间转嫁到主线程造成ANR
 * commit方法效率低些，会造成调用线程阻塞，有返回值，更保险，如果数据量大，其读写操作最好放在单一线程池里进行
 * 使用sp时最好不要做大数据的存储，而应该考虑其它的方式来持久化或缓存数据
 */
public class SPUtil {
    /**
     * sp实例
     */
    private SharedPreferences mSharedPreferences;
    /**
     * 保存的文件名
     */
    public static final String DEFAULT_SP_NAME = "bpzChangeConfig";
    /**
     * 单例
     */
    private static volatile SPUtil mInstance;
    /**
     * 保存sp实例的map
     */
    private SimpleArrayMap<String, SharedPreferences> spMap;

    private SPUtil(String fileName) {
        spMap = new SimpleArrayMap<>();
        //传入的文件名为空串，则用默认的文件名
        if (StringUtil.isSpaceStr(fileName)) {
            fileName = DEFAULT_SP_NAME;
        }
        //根据文件名获取sp实例
        mSharedPreferences = getMSharedPreferences(fileName);
        if (mSharedPreferences == null) {
            //如果拿到的是空值，说明没有创建过,就新建实例并添加至map
            mSharedPreferences = LibApp.mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            spMap.put(fileName, mSharedPreferences);
        }
    }

    /**
     * 单例获取
     *
     * @param fileName 文件名
     * @return 返回唯一实例
     */
    public static SPUtil getInstance(String fileName) {
        if (mInstance == null) {
            synchronized (SPUtil.class) {
                if (mInstance == null) {
                    mInstance = new SPUtil(fileName);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据文件名获取sp实例
     *
     * @param fileName 文件名
     * @return 返回sp实例
     */
    private SharedPreferences getMSharedPreferences(String fileName) {
        return spMap.get(fileName);
    }

    /**
     * 保存boolean值commit方式
     *
     * @param key   键
     * @param value 要保存的值
     */
    public boolean put(String key, boolean value) {
        return mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 保存boolean值apply方式
     *
     * @param key   键
     * @param value 要保存的值
     */
    public void putA(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取boolean值
     *
     * @param key      键
     * @param defValue 默认值
     */
    public boolean get(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 保存字符串值
     *
     * @param key   键
     * @param value 要保存的值
     */
    public boolean put(String key, String value) {
        return mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 保存字符串值
     *
     * @param key   键
     * @param value 要保存的值
     */
    public void putA(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 获取字符串值
     *
     * @param key      键
     * @param defValue 默认的值
     */
    public String get(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * 保存long类型的数据
     *
     * @param key   键
     * @param value 要保存的值
     */
    public boolean put(String key, long value) {
        return mSharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * 保存long类型的数据
     *
     * @param key   键
     * @param value 要保存的值
     */
    public void putA(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    /**
     * 获取long类型的值
     *
     * @param key      键
     * @param defValue 默认值
     */
    public long get(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }


    /**
     * 保存int类型的数据
     *
     * @param key   键
     * @param value 要保存的值
     */
    public boolean put(String key, int value) {
        return mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 保存int类型的数据
     *
     * @param key   键
     * @param value 要保存的值
     */
    public void putA(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 获取int类型的值
     *
     * @param key      键
     * @param defValue 默认值
     */
    public long get(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * 保存float类型的数据
     *
     * @param key   键
     * @param value 要保存的值
     */
    public boolean put(String key, float value) {
        return mSharedPreferences.edit().putFloat(key, value).commit();
    }

    /**
     * 保存float类型的数据
     *
     * @param key   键
     * @param value 要保存的值
     */
    public void putA(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    /**
     * 获取float类型的值
     *
     * @param key      键
     * @param defValue 默认值
     */
    public float get(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

}
