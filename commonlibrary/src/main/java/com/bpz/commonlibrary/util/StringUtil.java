package com.bpz.commonlibrary.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.URLUtil;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/12.
 * 处理字符串的工具类
 */

public class StringUtil {
    public static final String QUESTION_MARK = "?";
    public static final String POINTER = ".";
    public static final String SHARP = "#";
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
     * 根据路径获取文件的后缀名
     *
     * @param path URL或文件路径
     * @return 空串或最后一个 . 后的串
     */
    @NonNull
    public static String getFileSuffixName(String path) {
        return getStrOutSthChar(getFileName(path), POINTER);
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
            //如果传入字符串为空行或用来分离的串为null，则返回空串
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
        //路径，可以是url(url带参，则去掉参数)，也可以是本地的绝对路径
        return getStrOutSthChar(removeUrlParams(path), File.separator);
    }

    /**
     * get请求的url会在后面带参数，移除 ? 后面的参数
     *
     * @return 不带参数的url
     */
    public static String removeUrlParams(String url) {
        if (isSpace(url)) {
            //空串直接返回
            return url;
        }
        if (url.contains(QUESTION_MARK)) {
            //截取 ? 前面的内容
            return url.substring(0, url.indexOf(QUESTION_MARK));
        }
        //不含 ? ，直接返回
        return url;

    }

    /**
     * 从url里解析文件后缀名
     * <p>
     * http://www.aspxfans.com:8080/news/index.asp?boardID=5&ID=24618&page=1#name
     * scheme：协议部分 http:
     * 分隔符： //
     * 域名：www.aspxfans.com
     * 分隔符： :
     * 端口： 8080
     * 路径分隔符：  /
     * <p>
     * 从域名后的第一个“/”开始到最后一个“/”为止，是虚拟目录部分。
     * 虚拟目录也不是一个URL必须的部分。本例中的虚拟目录是“/news/”
     * <p>
     * 从域名后的最后一个“/”开始到“？”为止，是文件名部分，
     * 如果没有“?”,则是从域名后的最后一个“/”开始到“#”为止，是文件部分，
     * 如果没有“？”和“#”，那么从域名后的最后一个“/”开始到结束，都是文件名部分。
     * 本例中的文件名是“index.asp”。文件名部分也不是一个URL必须的部分，如果省略该部分，则使用默认的文件名
     * <p>
     * 从“#”开始到最后，都是锚部分。本例中的锚部分是“name”。锚部分也不是一个URL必须的部分
     * <p>
     * 从“？”开始到“#”为止之间的部分为参数部分，又称搜索部分、查询部分。
     * 本例中的参数部分为“boardID=5&ID=24618&page=1”。
     * 参数可以允许有多个参数，参数与参数之间用“&”作为分隔符
     * <p>
     * uri
     * foo://username:password@example.com:8042/over/there/index.dtb?type=animal&name=narwhal#nose
     * \_/ \________________/ \_________/ \__/\____________________/\____________________/ \__/
     * |           |              |        |            |                      |            |
     * scheme   userinfo          host     port         path                  query       fragment
     * name
     *
     * @param url url
     * @return 返回文件后缀名，如果没有后缀名会返回空串
     */
    @NonNull
    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf(SHARP);
            //有锚，舍弃锚
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            //有查询参数
            int query = url.lastIndexOf(QUESTION_MARK);
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf(File.separator);
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            //处理掉非法字符
            if (!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf(POINTER);
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }
        return "";
    }


    /**
     * 获取某字符串在另一字符串中出现的次数
     *
     * @param strDest 源字符串
     * @param target  要查找的字符串
     * @return /如果源字符串和要查找的字符串为空串，则返回-1，否则返回实际出现的次数
     */
    public static int getCount(String strDest, String target) {
        if (StringUtil.isSpace(strDest) || StringUtil.isSpace(target)) {
            //如果源字符串和要查找的字符串为空串，则返回-1
            return -1;
        }
        //替换所有要查找的串
        String newStr = strDest.replaceAll(target, "");
        //返回字符串差值除以目标字符串长度
        return (strDest.length() - newStr.length()) / target.length();
    }

    /**
     * 获取子字符串在主字符串中开始索引集合，集合长度也为出现次数
     *
     * @param text   主字符串
     * @param change 子字符串
     * @return 返回集合, 集合长度为0，则说明text或change为空或主字符串不包含子字符串
     */
    public static List<Integer> getIndexList(String text, String change) {
        List<Integer> indexList = new ArrayList<>();
        if (StringUtil.isSpace(text) || StringUtil.isSpace(change)) {
            LogUtil.e("text or change is empty !");
            return indexList;
        }
        int realIndex = 0;
        while (true) {
            int index = text.indexOf(change);
            if (index == -1) {
                return indexList;
            }
            realIndex = realIndex + index + change.length();
            indexList.add(realIndex - change.length());
            text = text.substring(index + change.length(), text.length());
        }
    }
}
