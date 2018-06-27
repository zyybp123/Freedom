package com.bpz.commonlibrary.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.jetbrains.annotations.Contract;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2018/1/12.
 * 处理字符串的工具类
 */

public class StringUtil {
    private static final String TAG = "StringUtil";

    private StringUtil() {
    }

    /**
     * 获取一个不为null的字符串
     *
     * @param str 字符串
     * @return 如果为null就返回空串
     */
    public static String getNotNullStr(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    /**
     * 获取限字符数的字符串（多余的用省略号替代）
     *
     * @param str                字符串
     * @param length             省略后的长度
     * @param isContainsEllipsis 长度是否算上省略号，true为省略后的长度包含省略号
     * @return 返回省略后的字符串
     */
    public static String getEllipsisStr(String str, int length, boolean isContainsEllipsis) {
        if (isSpace(str)) {
            //如果是空行，返回空串
            return "";
        } else {
            if (length < 0) {
                length = 0;
            }
            if (str.length() <= length) {
                //如果字符串的长度小于等于要省略的长度，直接返回
                return str;
            }
            //长度大于0，且小于字符串的长度
            return str.substring(0, (isContainsEllipsis ? length - 1 : length)) + "…";
        }
    }

    /**
     * 判断是否是空白串
     *
     * @param s 字符串
     * @return true为空白串，false则不是
     */
    @Contract("null -> true")
    public static boolean isSpace(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个字符串是否为空行
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、换行符组成的字符串
     *
     * @param str 字符串
     * @return true 为空行，false 不为空行
     */
    @Deprecated
    public static boolean isSpaceStr(String str) {
        if (TextUtils.isEmpty(str)) {
            //为空直接返回true
            return true;
        }
        //不为空，替换掉所有空格、制表符、回车符、换行符为空串，再判断是否为空
        String replaceAll = str.replaceAll("(?m)^\\\\s*$(\\\\n|\\\\r\\\\n)", "");
        return TextUtils.isEmpty(replaceAll);
    }

    /**
     * 判断一个字符串是否是一个httpUrl
     *
     * @param url 字符串
     * @return 如果是URL返回true，否则返回false
     */
    public static boolean isAHttpUrl(String url) {
        HttpUrl parse = HttpUrl.parse(url);
        LogUtil.e(TAG, "httpUrl parse result is --->" + parse);
        //如果返回的为null则说明其不是一个合法的httpUrl
        return parse != null;
    }

    /**
     * 根据路径获取文件的后缀名
     *
     * @param path URL或文件路径
     * @return 空串或最后一个 . 后的串
     */
    @NonNull
    public static String getFileSuffixName(String path) {
        return getStrOutSthChar(getFileName(path), ".");
    }

    /**
     * 获取某一字符串中最后出现的分离串后的内容
     *
     * @param str      字符串
     * @param splitStr 分离串
     * @return 空串或最后一个 对应串 后的串
     */
    @NonNull
    private static String getStrOutSthChar(String str, String splitStr) {
        if (StringUtil.isSpace(str) || splitStr == null) {
            //如果传入字符串为空行或用来分离的串味null，则返回空串
            return "";
        }
        //最后一个 对应串 的索引
        int index = str.lastIndexOf(splitStr);
        //如果不含 对应串 ，就返回空串，否则就返回 对应串 后的内容
        return (index < 0) ? "" : str.substring(index + 1, str.length());
    }

    /**
     * 根据路径获取文件的名字(路径的最后一个分隔符的串)
     *
     * @param path URL或文件路径
     * @return 空串或最后一个分隔符后的串
     */
    @NonNull
    public static String getFileName(String path) {
        return getStrOutSthChar(path, "/");
    }
}
