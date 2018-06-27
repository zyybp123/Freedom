package com.bpz.commonlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.bpz.commonlibrary.LibApp;

import java.util.Map;


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
     * 默认保存的文件名
     */
    public static final String DEFAULT_SP_NAME = "bpzChangeConfig";
    /**
     * 保存sp实例的map
     */
    private static final SimpleArrayMap<String, SPUtil> SP_MAP = new SimpleArrayMap<>();
    /**
     * 单例
     */
    private static volatile SPUtil mInstance;
    /**
     * sp实例
     */
    private SharedPreferences mSharedPreferences;

    private SPUtil(String fileName) {
        mSharedPreferences = LibApp.mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 实例获取
     *
     * @param fileName 文件名
     * @return 返回唯一实例
     */
    public static SPUtil getInstance(String fileName) {
        //传入的文件名为空串，则用默认的文件名
        if (StringUtil.isSpace(fileName)) {
            fileName = DEFAULT_SP_NAME;
        }
        SPUtil spUtil = SP_MAP.get(fileName);
        if (spUtil == null) {
            spUtil = new SPUtil(fileName);
            SP_MAP.put(fileName, spUtil);
        }
        return spUtil;
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

    /**
     * 移除数据
     *
     * @param key 键
     */
    public void removeA(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    /**
     * 移除数据
     *
     * @param key 键
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        return mSharedPreferences.edit().remove(key).commit();
    }

    /**
     * 清空数据
     */
    public void clearA() {
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * 清空数据
     *
     * @return 返回是否成功
     */
    public boolean clear() {
        return mSharedPreferences.edit().clear().commit();
    }

    /**
     * 获取所有值
     *
     * @return 返回键值对的Map
     */
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    /**
     * 判断是否包含此键
     *
     * @param key 键
     * @return Returns true if the preference exists in the preferences,otherwise false.
     */
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

}
