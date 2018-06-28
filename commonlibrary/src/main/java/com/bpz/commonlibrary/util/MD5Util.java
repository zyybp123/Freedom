package com.bpz.commonlibrary.util;

import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * MD5相关工具类
 */
public class MD5Util {
    public static final String MD5 = "MD5";
    private static final String TAG = "MD5Util";
    private static final int FR_RADIX = 16;
    private static final int FR_SIGN_NUM = 1;

    private MD5Util() {

    }

    /**
     * 将字符串用md5加密
     *
     * @param str 字符串
     * @return 如果字符串为空串，则原样返回
     */
    public static String md5(String str) {
        if (StringUtil.isSpace(str)) {
            return str;
        }
        String hexString = null;
        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance(MD5);
            byte[] digest = md.digest(str.getBytes());
            for (byte b : digest) {
                int temp = b & 0xFF;
                hexString = Integer.toHexString(temp);
                if (hexString.length() < 2) {
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "get md5 exception: " + e);
            return str;
        }
    }

    private void getFileSHA1(){

    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param filePath 文件的路径
     * @return 空串或不是文件会返回null
     */

    @Nullable
    public static String getFileMD5(String filePath) {
        if (StringUtil.isSpace(filePath)) {
            //为空串，直接返回null
            return null;
        }
        File file = new File(filePath);
        if (!file.isFile()) {
            //不是文件，直接返回null
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance(MD5);
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "get md5 exception: " + e);
            return null;
        }
        BigInteger bigInt = new BigInteger(FR_SIGN_NUM, digest.digest());
        return bigInt.toString(FR_RADIX);
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param filePath  文件夹的绝对路径
     * @param listChild 是否递归子目录中的文件 true为是
     * @param map       存MD5的map
     * @return 返回对应文件的路径和对应的MD5值的map
     */
    @Nullable
    public static SimpleArrayMap<String, String> getDirMD5(
            String filePath,
            @NotNull SimpleArrayMap<String, String> map, boolean listChild) {
        if (StringUtil.isSpace(filePath)) {
            //为空串直接返回null
            return null;
        }
        File file = new File(filePath);
        if (!file.isDirectory()) {
            //不是文件夹，返回null
            return null;
        }
        String md5;
        File files[] = file.listFiles();
        for (File f : files) {
            if (f.isDirectory() && listChild) {
                getDirMD5(f.getAbsolutePath(), map, true);
            } else {
                md5 = getFileMD5(f.getAbsolutePath());
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }
}
